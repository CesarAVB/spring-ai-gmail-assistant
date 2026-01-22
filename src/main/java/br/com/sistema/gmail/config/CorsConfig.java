package br.com.sistema.gmail.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * Configuração CORS (Cross-Origin Resource Sharing) para Gmail Assistant.
 * 
 * Permite que o frontend acesse a API de diferentes origens.
 * 
 * Configuração adaptada para operações de email que podem
 * ser sensíveis (envio, leitura, exclusão).
 * 
 * @author César Augusto
 * @version 1.0.0
 */
@Configuration
public class CorsConfig {
    
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // ============================================
        // ORIGENS PERMITIDAS
        // ============================================
        
        // DESENVOLVIMENTO
        config.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:*",
                "http://127.0.0.1:*",
                "https://localhost:*"
        ));
        
        // PRODUÇÃO: Restrinja a origens específicas
        // config.setAllowedOrigins(Arrays.asList(
        //     "https://webmail.sua-empresa.com",
        //     "https://app.sua-empresa.com"
        // ));
        
        // ============================================
        // MÉTODOS HTTP PERMITIDOS
        // ============================================
        
        config.setAllowedMethods(Arrays.asList(
                "GET",      // Listar emails
                "POST",     // Enviar emails
                "PUT",      // Atualizar (marcar lido)
                "DELETE",   // Deletar emails
                "OPTIONS"   // Preflight
        ));
        
        // ============================================
        // HEADERS PERMITIDOS
        // ============================================
        
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With",
                "X-API-Key",
                "Cache-Control"
        ));
        
        // ============================================
        // HEADERS EXPOSTOS
        // ============================================
        
        config.setExposedHeaders(Arrays.asList(
                "Authorization",
                "X-Total-Emails",      // Total de emails
                "X-Unread-Count",      // Emails não lidos
                "X-Page-Number"
        ));
        
        // ============================================
        // CONFIGURAÇÕES ADICIONAIS
        // ============================================
        
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        
        // ============================================
        // APLICAR CONFIGURAÇÃO
        // ============================================
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}