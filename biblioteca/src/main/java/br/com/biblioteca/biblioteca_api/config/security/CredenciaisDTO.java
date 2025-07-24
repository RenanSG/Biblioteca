package br.com.biblioteca.biblioteca_api.config.security;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CredenciaisDTO {
    private String email;
    private String senha;
}