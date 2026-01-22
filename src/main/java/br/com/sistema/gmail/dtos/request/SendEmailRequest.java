package br.com.sistema.gmail.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request para enviar email")
public record SendEmailRequest(
        
    @Schema(description = "Email do destinatário", example = "usuario@email.com", required = true)
    String to,
    
    @Schema(description = "Assunto do email", example = "Reunião importante", required = true)
    String subject,
    
    @Schema(description = "Corpo do email", example = "Olá, gostaria de marcar uma reunião...", required = true)
    String body
    
) {
    public boolean isValid() {
        return to != null && !to.trim().isEmpty()
                && subject != null && !subject.trim().isEmpty()
                && body != null && !body.trim().isEmpty();
    }
}