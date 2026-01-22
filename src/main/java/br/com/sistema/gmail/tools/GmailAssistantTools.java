package br.com.sistema.gmail.tools;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.ModifyMessageRequest;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.UserCredentials;

import dev.langchain4j.agent.tool.Tool;
import jakarta.mail.Session;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GmailAssistantTools {

    private static final String APPLICATION_NAME = "Gmail AI Assistant";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Arrays.asList(
            GmailScopes.GMAIL_READONLY,
            GmailScopes.GMAIL_SEND,
            GmailScopes.GMAIL_MODIFY,
            GmailScopes.GMAIL_LABELS
    );

    private Gmail gmailService;

    @Value("${gmail.client-id}")
    private String clientId;

    @Value("${gmail.client-secret}")
    private String clientSecret;

    @Value("${gmail.refresh-token}")
    private String refreshToken;

    private Gmail getGmailService() {
        if (gmailService == null) {
            try {
                log.info("🔌 Conectando ao Gmail API (headless)");

                NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

                UserCredentials credentials = UserCredentials.newBuilder()
                        .setClientId(clientId)
                        .setClientSecret(clientSecret)
                        .setRefreshToken(refreshToken)
                        .build();

                gmailService = new Gmail.Builder(httpTransport, JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                        .setApplicationName(APPLICATION_NAME)
                        .build();

                log.info("✅ Conectado ao Gmail (headless)");
            } catch (Exception e) {
                log.error("❌ Erro ao conectar Gmail", e);
            }
        }
        return gmailService;
    }

    @Tool("Lista os últimos emails da caixa de entrada")
    public String listEmails(Integer maxResults) {
        try {
            log.info("📬 Listando emails (max: {})", maxResults);

            Gmail service = getGmailService();
            String user = "me";

            int limit = maxResults != null && maxResults > 0 ? maxResults : 10;

            ListMessagesResponse response = service.users().messages()
                    .list(user)
                    .setMaxResults((long) limit)
                    .setQ("in:inbox")
                    .execute();

            List<Message> messages = response.getMessages();

            if (messages == null || messages.isEmpty()) {
                return "📭 Caixa de entrada vazia.";
            }

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("✅ Total: %d emails\n\n", messages.size()));

            int count = 1;
            for (Message message : messages) {
                Message fullMessage = service.users().messages()
                        .get(user, message.getId())
                        .setFormat("full")
                        .execute();

                String from = getHeader(fullMessage, "From");
                String subject = getHeader(fullMessage, "Subject");
                String date = getHeader(fullMessage, "Date");
                boolean unread = fullMessage.getLabelIds() != null &&
                        fullMessage.getLabelIds().contains("UNREAD");

                sb.append(String.format("📧 #%d%s\n", count, unread ? " 🔵 NÃO LIDO" : ""));
                sb.append(String.format("   De: %s\n", from));
                sb.append(String.format("   Assunto: %s\n", subject != null ? subject : "(sem assunto)"));
                sb.append(String.format("   Data: %s\n", date));
                sb.append(String.format("   ID: %s\n\n", message.getId()));

                count++;
            }

            return sb.toString();

        } catch (Exception e) {
            log.error("❌ Erro ao listar emails", e);
            return formatError(e);
        }
    }

    @Tool("Lista emails não lidos")
    public String listUnreadEmails(Integer maxResults) {
        try {
            log.info("📬 Listando emails não lidos");

            Gmail service = getGmailService();
            String user = "me";

            int limit = maxResults != null && maxResults > 0 ? maxResults : 10;

            ListMessagesResponse response = service.users().messages()
                    .list(user)
                    .setMaxResults((long) limit)
                    .setQ("is:unread in:inbox")
                    .execute();

            List<Message> messages = response.getMessages();

            if (messages == null || messages.isEmpty()) {
                return "✅ Nenhum email não lido!";
            }

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("🔵 Total de não lidos: %d\n\n", messages.size()));

            int count = 1;
            for (Message message : messages) {
                Message fullMessage = service.users().messages()
                        .get(user, message.getId())
                        .setFormat("full")
                        .execute();

                String from = getHeader(fullMessage, "From");
                String subject = getHeader(fullMessage, "Subject");
                String date = getHeader(fullMessage, "Date");

                sb.append(String.format("📧 #%d 🔵\n", count));
                sb.append(String.format("   De: %s\n", from));
                sb.append(String.format("   Assunto: %s\n", subject != null ? subject : "(sem assunto)"));
                sb.append(String.format("   Data: %s\n", date));
                sb.append(String.format("   ID: %s\n\n", message.getId()));

                count++;
            }

            return sb.toString();

        } catch (Exception e) {
            log.error("❌ Erro ao listar não lidos", e);
            return formatError(e);
        }
    }

    @Tool("Busca emails por palavra-chave no assunto ou corpo")
    public String searchEmails(String query, Integer maxResults) {
        try {
            log.info("🔍 Buscando emails: {}", query);

            Gmail service = getGmailService();
            String user = "me";

            int limit = maxResults != null && maxResults > 0 ? maxResults : 10;

            ListMessagesResponse response = service.users().messages()
                    .list(user)
                    .setMaxResults((long) limit)
                    .setQ(query)
                    .execute();

            List<Message> messages = response.getMessages();

            if (messages == null || messages.isEmpty()) {
                return String.format("❌ Nenhum email encontrado para: %s", query);
            }

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("✅ Encontrados %d emails para '%s':\n\n", messages.size(), query));

            int count = 1;
            for (Message message : messages) {
                Message fullMessage = service.users().messages()
                        .get(user, message.getId())
                        .setFormat("full")
                        .execute();

                String from = getHeader(fullMessage, "From");
                String subject = getHeader(fullMessage, "Subject");
                String date = getHeader(fullMessage, "Date");

                sb.append(String.format("📧 #%d\n", count));
                sb.append(String.format("   De: %s\n", from));
                sb.append(String.format("   Assunto: %s\n", subject));
                sb.append(String.format("   Data: %s\n", date));
                sb.append(String.format("   ID: %s\n\n", message.getId()));

                count++;
            }

            return sb.toString();

        } catch (Exception e) {
            log.error("❌ Erro ao buscar emails", e);
            return formatError(e);
        }
    }

    @Tool("Obtém o conteúdo completo de um email específico por ID")
    public String getEmailContent(String emailId) {
        try {
            log.info("📖 Lendo email: {}", emailId);

            Gmail service = getGmailService();
            String user = "me";

            Message message = service.users().messages()
                    .get(user, emailId)
                    .setFormat("full")
                    .execute();

            String from = getHeader(message, "From");
            String to = getHeader(message, "To");
            String subject = getHeader(message, "Subject");
            String date = getHeader(message, "Date");
            String body = getEmailBody(message);

            return String.format("""
                    ✅ Email ID: %s

                    📤 De: %s
                    📥 Para: %s
                    📋 Assunto: %s
                    📅 Data: %s

                    📄 Conteúdo:
                    ─────────────────────────────────
                    %s
                    ─────────────────────────────────
                    """, emailId, from, to, subject, date, body);

        } catch (Exception e) {
            log.error("❌ Erro ao ler email", e);
            return formatError(e);
        }
    }

    @Tool("Envia um novo email")
    public String sendEmail(String to, String subject, String body) {
        try {
            log.info("Enviando email para: {}", to);

            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage email = new MimeMessage(session);

            email.setFrom("me");
            email.addRecipients(RecipientType.TO, to);
            email.setSubject(subject);
            email.setText(body);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            email.writeTo(buffer);

            byte[] emailBytes = buffer.toByteArray();
            String encodedEmail = Base64.getUrlEncoder().encodeToString(emailBytes);

            Message message = new Message();
            message.setRaw(encodedEmail);

            Message sentMessage = getGmailService().users()
                    .messages()
                    .send("me", message)
                    .execute();

            log.info("✅ Email enviado com sucesso para: {} com ID: {}", to, sentMessage.getId());

            return String.format(
                    "✅ **EMAIL ENVIADO COM SUCESSO**\n\n" +
                    "**Para:** %s\n" +
                    "**Assunto:** %s\n" +
                    "**ID da Mensagem:** %s\n\n" +
                    "Conteúdo:\n%s",
                    to, subject, sentMessage.getId(), body
            );

        } catch (Exception e) {
            log.error("Erro ao enviar email para: {}", to, e);
            return "❌ Erro ao enviar email: " + e.getMessage();
        }
    }

    @Tool("Marca um email como lido")
    public String markAsRead(String emailId) {
        try {
            Gmail service = getGmailService();
            String user = "me";

            ModifyMessageRequest mods = new ModifyMessageRequest()
                    .setRemoveLabelIds(Arrays.asList("UNREAD"));

            service.users().messages().modify(user, emailId, mods).execute();

            return String.format("✅ Email %s marcado como lido!", emailId);

        } catch (Exception e) {
            log.error("❌ Erro ao marcar como lido", e);
            return formatError(e);
        }
    }

    @Tool("Marca um email como não lido")
    public String markAsUnread(String emailId) {
        try {
            Gmail service = getGmailService();
            String user = "me";

            ModifyMessageRequest mods = new ModifyMessageRequest()
                    .setAddLabelIds(Arrays.asList("UNREAD"));

            service.users().messages().modify(user, emailId, mods).execute();

            return String.format("🔵 Email %s marcado como não lido!", emailId);

        } catch (Exception e) {
            log.error("❌ Erro ao marcar como não lido", e);
            return formatError(e);
        }
    }

    @Tool("Deleta um email permanentemente")
    public String deleteEmail(String emailId) {
        try {
            Gmail service = getGmailService();
            String user = "me";

            service.users().messages().delete(user, emailId).execute();

            return String.format("🗑️ Email %s deletado com sucesso!", emailId);

        } catch (Exception e) {
            log.error("❌ Erro ao deletar email", e);
            return formatError(e);
        }
    }

    @Tool("Move um email para a lixeira")
    public String trashEmail(String emailId) {
        try {
            Gmail service = getGmailService();
            String user = "me";

            service.users().messages().trash(user, emailId).execute();

            return String.format("🗑️ Email %s movido para lixeira!", emailId);

        } catch (Exception e) {
            log.error("❌ Erro ao mover para lixeira", e);
            return formatError(e);
        }
    }

    private String getHeader(Message message, String name) {
        if (message.getPayload() == null || message.getPayload().getHeaders() == null) {
            return "";
        }
        return message.getPayload().getHeaders().stream()
                .filter(header -> header.getName().equalsIgnoreCase(name))
                .map(MessagePartHeader::getValue)
                .findFirst()
                .orElse("");
    }

    private String getEmailBody(Message message) {
        try {
            if (message.getPayload() == null) return "(corpo vazio)";

            MessagePart payload = message.getPayload();

            if (payload.getBody() != null && payload.getBody().getData() != null) {
                return new String(Base64.getUrlDecoder().decode(payload.getBody().getData()));
            }

            if (payload.getParts() != null) {
                for (MessagePart part : payload.getParts()) {
                    if ("text/plain".equals(part.getMimeType()) &&
                        part.getBody() != null &&
                        part.getBody().getData() != null) {
                        return new String(Base64.getUrlDecoder().decode(part.getBody().getData()));
                    }
                }
            }

            return "(corpo não disponível)";

        } catch (Exception e) {
            return "(erro ao ler corpo)";
        }
    }

    private String formatError(Exception e) {
        String errorMsg = e.getMessage() != null ? e.getMessage() : "Erro desconhecido";

        if (errorMsg.contains("401") || errorMsg.contains("Unauthorized")) {
            return "❌ Erro de autenticação: verifique o refresh token";
        }
        if (errorMsg.contains("404") || errorMsg.contains("Not Found")) {
            return "❌ Email não encontrado";
        }
        if (errorMsg.contains("403") || errorMsg.contains("Forbidden")) {
            return "❌ Acesso negado: permissões insuficientes";
        }

        return "❌ Erro: " + errorMsg;
    }
}
