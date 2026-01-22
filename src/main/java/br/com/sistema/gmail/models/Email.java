package br.com.sistema.gmail.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Email {
    
    private String id;
    private Integer numero;
    private Remetente remetente;
    private String assunto;
    private String corpo;
    private String data;
    private Boolean naoLido;
    private Boolean importante;
    private Boolean estrela;
}