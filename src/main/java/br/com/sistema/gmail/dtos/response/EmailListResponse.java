package br.com.sistema.gmail.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Response com lista de emails")
public record EmailListResponse(
        
    @Schema(description = "Total de emails", example = "25")
    Integer total,
    
    @Schema(description = "Número de não lidos", example = "5")
    Integer unreadCount,
    
    @Schema(description = "Lista de emails")
    List<EmailInfo> emails
    
) {
    @Schema(description = "Informações de um email")
    public record EmailInfo(
            
        @Schema(description = "ID do email")
        String id,
        
        @Schema(description = "Número sequencial")
        Integer number,
        
        @Schema(description = "Remetente")
        Sender sender,
        
        @Schema(description = "Assunto")
        String subject,
        
        @Schema(description = "Preview do corpo (primeiros 100 caracteres)")
        String preview,
        
        @Schema(description = "Data do email")
        String date,
        
        @Schema(description = "Se não foi lido")
        Boolean unread,
        
        @Schema(description = "Se é importante")
        Boolean important
        
    ) {}
    
    @Schema(description = "Informações do remetente")
    public record Sender(
            
        @Schema(description = "Nome do remetente")
        String name,
        
        @Schema(description = "Email do remetente")
        String email
        
    ) {}
}