package br.com.sistema.gmail.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response genérico do Gmail Assistant")
public record AssistantResponse(
        
    @Schema(description = "Se operação foi bem-sucedida")
    Boolean success,
    
    @Schema(description = "Nome do assistente")
    String assistant,
    
    @Schema(description = "Tipo de resposta")
    String type,
    
    @Schema(description = "Pergunta original")
    String question,
    
    @Schema(description = "Dados da resposta")
    String data,
    
    @Schema(description = "Mensagem de erro")
    String error,
    
    @Schema(description = "Timestamp")
    String timestamp
    
) {
    public static AssistantResponse success(String question, String data) {
        return new AssistantResponse(
                true,
                "GmailAssistant",
                "chat",
                question,
                data,
                null,
                java.time.LocalDateTime.now().toString()
        );
    }
    
    public static AssistantResponse error(String question, String error) {
        return new AssistantResponse(
                false,
                "GmailAssistant",
                "error",
                question,
                null,
                error,
                java.time.LocalDateTime.now().toString()
        );
    }
}