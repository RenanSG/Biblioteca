package br.com.biblioteca.biblioteca_api.autor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AutorDTO(
        @NotBlank(message = "O nome do autor não pode ser vazio.")
        @Size(min = 2, max = 100, message = "O nome do autor deve ter entre 2 e 100 caracteres.")
        String nome,

        LocalDate dataNascimento,

        @Size(max = 1000, message = "A biografia não pode exceder 1000 caracteres.")
        String biografia
) {
}
