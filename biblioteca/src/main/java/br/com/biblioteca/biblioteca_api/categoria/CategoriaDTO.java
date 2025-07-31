package br.com.biblioteca.biblioteca_api.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaDTO(
        @NotBlank(message = "O nome da categoria não pode ser vazio.")
        @Size(min = 3, max = 100, message = "O nome da categoria deve ter entre 3 e 100 caracteres.")
        String nome,

        @Size(max = 255, message = "A descrição não pode exceder 255 caracteres.")
        String descricao
) {
}
