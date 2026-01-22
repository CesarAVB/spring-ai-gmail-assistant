package br.com.sistema.gmail.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response com detalhes completos de um email")
public record EmailDetailResponse(
        
    @Schema(description = "ID do email")
    String id,
    
    @Schema(description = "Remetente")
    EmailListResponse.Sender sender,
    
    @Schema(description = "Assunto")
    String subject,
    
    @Schema(description = "Corpo completo")
    String body,
    
    @Schema(description = "Data")
    String date,
    
    @Schema(description = "Se não lido")
    Boolean unread,
    
    @Schema(description = "Se importante")
    Boolean important
    
) {}