package br.com.sistema.gmail.controller;

import br.com.sistema.gmail.dtos.request.GmailChatRequest;
import br.com.sistema.gmail.dtos.request.SendEmailRequest;
import br.com.sistema.gmail.dtos.response.AssistantResponse;
import br.com.sistema.gmail.service.GmailAssistantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/gmail")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Gmail Assistant", description = "Assistente de IA para Gmail")
public class GmailController {
    
    private final GmailAssistantService assistantService;
    
    @PostMapping("/chat")
    @Operation(
        summary = "Chat com assistente Gmail",
        description = "Envie uma mensagem em linguagem natural para o assistente processar"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Mensagem processada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AssistantResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "assistant": "GmailAssistant",
                      "type": "chat",
                      "question": "Liste meus últimos 5 emails",
                      "data": "✅ Total: 5 emails...",
                      "error": null,
                      "timestamp": "2025-01-22T15:30:00"
                    }
                    """)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Request inválido"
        )
    })
    public ResponseEntity<AssistantResponse> chat(@RequestBody GmailChatRequest request) {
        log.info("💬 Chat recebido");
        
        if (!request.isValid()) {
            return ResponseEntity.badRequest()
                    .body(AssistantResponse.error(
                            request.message(),
                            "Mensagem não pode ser vazia"
                    ));
        }
        
        try {
            String response = assistantService.processMessage(request.message());
            return ResponseEntity.ok(AssistantResponse.success(request.message(), response));
            
        } catch (Exception e) {
            log.error("❌ Erro ao processar chat", e);
            return ResponseEntity.internalServerError()
                    .body(AssistantResponse.error(
                            request.message(),
                            "Erro ao processar mensagem: " + e.getMessage()
                    ));
        }
    }
    
    @PostMapping("/send")
    @Operation(
        summary = "Enviar email",
        description = "Envia um email diretamente sem usar chat"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Email enviado com sucesso"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Request inválido"
        )
    })
    public ResponseEntity<AssistantResponse> sendEmail(@RequestBody SendEmailRequest request) {
        log.info("✉️ Enviando email para: {}", request.to());
        
        if (!request.isValid()) {
            return ResponseEntity.badRequest()
                    .body(AssistantResponse.error(
                            "Enviar email",
                            "Todos os campos são obrigatórios"
                    ));
        }
        
        try {
            String prompt = String.format(
                    "Envie um email para %s com assunto '%s' e corpo '%s'",
                    request.to(), request.subject(), request.body()
            );
            
            String response = assistantService.processMessage(prompt);
            return ResponseEntity.ok(AssistantResponse.success("Enviar email", response));
            
        } catch (Exception e) {
            log.error("❌ Erro ao enviar email", e);
            return ResponseEntity.internalServerError()
                    .body(AssistantResponse.error(
                            "Enviar email",
                            "Erro: " + e.getMessage()
                    ));
        }
    }
    
    @GetMapping("/health")
    @Operation(
        summary = "Health check",
        description = "Verifica se o serviço está online"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Serviço online"
    )
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("✅ Gmail Assistant Online");
    }
}