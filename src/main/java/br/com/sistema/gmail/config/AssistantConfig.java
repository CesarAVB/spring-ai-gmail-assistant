package br.com.sistema.gmail.config;

import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AssistantConfig {
    
    // ===============================
    // Propriedades de Configuração
    // ===============================
    
    @Value("${spring.langchain4j.google-ai.gemini.api-key}")
    private String geminiApiKey;
    
    @Value("${spring.langchain4j.google-ai.gemini.model-name:gemini-2.5-flash}")
    private String modelName;
    
    @Value("${spring.langchain4j.google-ai.gemini.temperature:0.7}")
    private Double temperature;
    
    // ===============================
    // Criar Bean GoogleAiGeminiChatModel
    // ===============================
    
    @Bean
    public GoogleAiGeminiChatModel googleAiGeminiChatModel() {
        log.info("========================================");
        log.info("🤖 Inicializando GoogleAiGeminiChatModel");
        log.info("========================================");
        log.info("   Modelo: {}", modelName);
        log.info("   Temperatura: {}", temperature);
        log.info("========================================");
        
        if (geminiApiKey == null || geminiApiKey.trim().isEmpty()) {
            log.error("❌ ERRO CRÍTICO: API Key do Google Gemini não configurada!");
            log.error("   Adicione a variável de ambiente GOOGLE_AI_API_KEY");
            throw new IllegalArgumentException("Google Gemini API Key não está configurada!");
        }
        
        try {
            GoogleAiGeminiChatModel model = GoogleAiGeminiChatModel.builder()
                    .apiKey(geminiApiKey)
                    .modelName(modelName)
                    .temperature(temperature)
                    .build();
            
            log.info("✅ GoogleAiGeminiChatModel criado com sucesso!");
            return model;
            
        } catch (Exception e) {
            log.error("❌ Erro ao criar GoogleAiGeminiChatModel: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao criar GoogleAiGeminiChatModel", e);
        }
    }
}