# 📧 Gmail AI Assistant

Um microserviço inteligente que oferece operações avançadas do Gmail através de uma interface de chat com IA (Google Gemini). Permite gerenciar emails de forma natural e intuitiva usando linguagem comum.

## 🎯 Funcionalidades

- **📬 Listar Emails** - Visualize os últimos emails da sua caixa de entrada
- **🔵 Emails Não Lidos** - Acesso rápido aos emails que ainda não foram lidos
- **🔍 Busca Avançada** - Busque emails por palavra-chave, remetente ou assunto
- **📖 Ler Email Completo** - Visualize o conteúdo completo de um email específico
- **✉️ Enviar Emails** - Envie novos emails automaticamente
- **✅ Marcar como Lido/Não Lido** - Organize seus emails
- **🗑️ Deletar/Mover para Lixeira** - Remova emails indesejados
- **💬 Chat com IA** - Interaja em linguagem natural com o assistente Gemini

## 🏗️ Arquitetura

```
Gmail AI Assistant
├── Controller (REST API)
├── Service (Lógica de negócio + LangChain4j)
├── Tools (Integração Gmail API)
└── Config (Gemini, CORS, OpenAPI)
```

## 🚀 Tecnologias

| Tecnologia | Versão | Descrição |
|-----------|--------|-----------|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.2.5 | Framework web |
| LangChain4j | 1.7.1 | Framework para integração com IA |
| Google Gemini | 2.5-flash | Modelo de IA para processamento |
| Gmail API | v1 | Acesso aos emails |
| Swagger/OpenAPI | 3.0 | Documentação interativa |
| Lombok | - | Redução de boilerplate |
| SLF4J/Logback | - | Logging |

## 📋 Pré-requisitos

- **Java 21+** instalado
- **Maven 3.8+** configurado
- **Conta Google** com Gmail habilitado
- **Google Cloud Project** criado
- **Chave de API do Google Gemini**

## ⚙️ Configuração

### 1. Variáveis de Ambiente

```bash
# Chave do Google Gemini (obrigatório)
export GEMINI_API_KEY="sua-chave-aqui"

# Configurações Gmail (obrigatório)
export GMAIL_CLIENT_ID="seu-client-id"
export GMAIL_CLIENT_SECRET="seu-client-secret"
export GMAIL_REFRESH_TOKEN="seu-refresh-token"

# Porta da aplicação (opcional, padrão: 8082)
export SERVER_PORT=8082
```

### 2. Arquivo de Credenciais Gmail

1. Acesse [Google Cloud Console](https://console.cloud.google.com/)
2. Crie um novo projeto
3. Ative a **Gmail API**
4. Crie credenciais OAuth 2.0 (Desktop Application)
5. Baixe o arquivo JSON e nomeie como `credentials.json`
6. Copie para: `src/main/resources/credentials.json`

### 3. Arquivo de Configuração

**application.properties:**

```properties
# Server
server.port=8082

# Gemini
spring.langchain4j.google-ai.gemini.api-key=${GEMINI_API_KEY}
spring.langchain4j.google-ai.gemini.model-name=gemini-2.5-flash
spring.langchain4j.google-ai.gemini.temperature=0.7

# Gmail
gmail.client-id=${GMAIL_CLIENT_ID}
gmail.client-secret=${GMAIL_CLIENT_SECRET}
gmail.refresh-token=${GMAIL_REFRESH_TOKEN}

# Logging
logging.level.root=INFO
logging.level.br.com.sistema.gmail=DEBUG
```

## 🔧 Instalação

```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/spring-ai-gmail-assistant.git
cd spring-ai-gmail-assistant

# 2. Configure as variáveis de ambiente
export GEMINI_API_KEY="sua-chave"
export GMAIL_CLIENT_ID="seu-id"
export GMAIL_CLIENT_SECRET="seu-secret"
export GMAIL_REFRESH_TOKEN="seu-token"

# 3. Copie o credentials.json
cp /caminho/para/credentials.json src/main/resources/

# 4. Compile e rode
mvn clean install
mvn spring-boot:run
```

## 📡 Endpoints da API

### Base URL
```
http://localhost:8082/api/v1/gmail
```

### 1. Chat com Assistente
**Endpoint:** `POST /api/v1/gmail/chat`

**Request:**
```json
{
  "message": "Liste meus últimos 5 emails"
}
```

**Response:**
```json
{
  "success": true,
  "assistant": "GmailAssistant",
  "type": "chat",
  "question": "Liste meus últimos 5 emails",
  "data": "✅ Total: 5 emails\n\n📧 #1\n   De: maria@example.com\n...",
  "error": null,
  "timestamp": "2025-01-22T15:30:00"
}
```

### 2. Enviar Email
**Endpoint:** `POST /api/v1/gmail/send`

**Request:**
```json
{
  "to": "destinatario@example.com",
  "subject": "Assunto do Email",
  "body": "Conteúdo do email"
}
```

**Response:**
```json
{
  "success": true,
  "assistant": "GmailAssistant",
  "type": "send",
  "data": "✅ EMAIL ENVIADO COM SUCESSO\n\n**Para:** destinatario@example.com\n**Assunto:** Assunto do Email\n**ID da Mensagem:** 123abc...",
  "error": null,
  "timestamp": "2025-01-22T15:30:00"
}
```

### 3. Health Check
**Endpoint:** `GET /api/v1/gmail/health`

**Response:**
```json
"✅ Gmail Assistant Online"
```

## 📚 Documentação Interativa

Acesse a documentação Swagger em:

```
http://localhost:8082/swagger-ui.html
```

Ou veja a especificação OpenAPI em:

```
http://localhost:8082/v3/api-docs
```

## 🔐 Segurança

### Autenticação

- **Gmail:** OAuth 2.0 com refresh token
- **Gemini:** API Key (armazenada em variável de ambiente)

### CORS

Configurado para aceitar requisições locais em desenvolvimento:

```
http://localhost:*
http://127.0.0.1:*
https://localhost:*
```

⚠️ **Produção:** Altere `CorsConfig.java` para origens específicas.

### Melhores Práticas

- ✅ Nunca commite credenciais no Git
- ✅ Use variáveis de ambiente para secrets
- ✅ Configure CORS adequadamente para produção
- ✅ Implemente autenticação JWT para a API
- ✅ Use HTTPS em produção

## 🛠️ Desenvolvimento

### Estrutura do Projeto

```
src/main/
├── java/br/com/sistema/gmail/
│   ├── config/              # Configurações
│   │   ├── AssistantConfig.java
│   │   ├── CorsConfig.java
│   │   ├── GeminiConfig.java
│   │   └── OpenApiConfig.java
│   ├── controller/          # REST Controllers
│   │   └── GmailController.java
│   ├── dtos/                # Data Transfer Objects
│   │   ├── request/
│   │   └── response/
│   ├── models/              # Modelos de domínio
│   │   ├── Email.java
│   │   └── Remetente.java
│   ├── service/             # Lógica de negócio
│   │   ├── GmailAssistantService.java
│   │   └── GmailAssistantService.GmailAiService (interface)
│   └── tools/               # Ferramentas (Tools do LangChain4j)
│       └── GmailAssistantTools.java
└── resources/
    ├── application.properties
    └── credentials.json
```

## 📊 Tools Disponíveis (LangChain4j)

O assistente tem acesso aos seguintes tools:

| Tool | Descrição |
|------|-----------|
| `listEmails(maxResults)` | Lista os últimos N emails |
| `listUnreadEmails(maxResults)` | Lista apenas emails não lidos |
| `searchEmails(query, maxResults)` | Busca emails por palavra-chave |
| `getEmailContent(emailId)` | Obtém conteúdo completo de um email |
| `sendEmail(to, subject, body)` | Envia novo email |
| `markAsRead(emailId)` | Marca como lido |
| `markAsUnread(emailId)` | Marca como não lido |
| `deleteEmail(emailId)` | Deleta permanentemente |
| `trashEmail(emailId)` | Move para lixeira |
