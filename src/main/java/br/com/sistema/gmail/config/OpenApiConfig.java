package br.com.sistema.gmail.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do OpenAPI/Swagger para Gmail Assistant.
 * 
 * Fornece documentação interativa da API em:
 * - Swagger UI: http://localhost:8082/swagger-ui.html
 * - OpenAPI JSON: http://localhost:8082/api-docs
 * 
 * @author César Augusto
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("📧 Gmail AI Assistant API")
                        .version("1.0.0")
                        .description("""
                                API REST para assistente inteligente de Gmail com IA (Google Gemini).
                                
                                ## 🎯 Funcionalidades
                                
                                - **Listagem de Emails**: Visualização inteligente da caixa de entrada
                                - **Busca Avançada**: Busca por palavras-chave, remetentes, datas
                                - **Envio de Emails**: Envio automatizado com templates
                                - **Organização**: Marcar como lido, deletar, arquivar
                                - **Análise de Conteúdo**: Resumos e análise de emails
                                - **Chat com IA**: Interação em linguagem natural
                                
                                ## 🔧 Tecnologias
                                
                                - Spring Boot 3.2.5
                                - Java 21
                                - LangChain4j 1.7.1
                                - Google Gemini AI
                                - Gmail API
                                
                                ## 🚀 Como Usar
                                
                                1. Configure `GEMINI_API_KEY` e `credentials.json` do Gmail
                                2. Envie requisições POST para `/api/v1/gmail/chat`
                                3. Use linguagem natural: "Quantos emails não lidos eu tenho?"
                                
                                ## 🔐 Autenticação
                                
                                Requer autenticação OAuth2 com Gmail API.
                                Configure `credentials.json` em `src/main/resources/`.
                                
                                ## 📚 Documentação
                                
                                Para mais informações, visite o [GitHub](https://github.com/seu-usuario/spring-ai-gmail-assistant)
                                """)
                        .contact(new Contact()
                                .name("César Augusto")
                                .email("cesar.augusto.rj1@gmail.com")
                                .url("https://portfolio.cesaravb.com.br"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8082")
                                .description("🖥️ Servidor Local de Desenvolvimento"),
                        new Server()
                                .url("https://gmail-assistant.sua-empresa.com")
                                .description("🌐 Servidor de Produção")
                ))
                .tags(List.of(
                        new Tag()
                                .name("Gmail Assistant")
                                .description("Endpoints do assistente de IA para Gmail"),
                        new Tag()
                                .name("Emails")
                                .description("Operações de listagem e gerenciamento de emails"),
                        new Tag()
                                .name("Busca")
                                .description("Busca e filtros de emails"),
                        new Tag()
                                .name("Envio")
                                .description("Envio de novos emails"),
                        new Tag()
                                .name("Organização")
                                .description("Marcar lido, deletar, arquivar"),
                        new Tag()
                                .name("Health")
                                .description("Endpoints de saúde e status do serviço")
                ));
    }
}