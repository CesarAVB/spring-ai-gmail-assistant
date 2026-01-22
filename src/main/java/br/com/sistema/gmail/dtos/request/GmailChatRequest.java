package br.com.sistema.gmail.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request para chat com Gmail Assistant")
public record GmailChatRequest(
        
    @Schema(
        description = "Mensagem em linguagem natural",
        example = "Liste meus últimos 10 emails",
        required = true
    )
    String message
    
) {
    public boolean isValid() {
        return message != null && !message.trim().isEmpty();
    }
}