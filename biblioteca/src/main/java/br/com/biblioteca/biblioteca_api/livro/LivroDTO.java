package br.com.biblioteca.biblioteca_api.livro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public record LivroDTO(
        @NotBlank(message = "O título não pode ser vazio.")
        String titulo,

        @NotNull(message = "O ID do autor é obrigatório.")
        Long autorId,

        @NotNull(message = "O ID da categoria é obrigatório.")
        Long categoriaId,

        @NotBlank(message = "A editora não pode ser vazia.")
        String editora,

        @NotBlank(message = "O ISBN não pode ser vazio.")
        String isbn,

        @NotNull(message = "O ano de publicação é obrigatório.")
        @PastOrPresent(message = "O ano de publicação não pode ser no futuro.")
        Integer anoPublicacao,

        @NotNull(message = "O número de páginas é obrigatório.")
        @Positive(message = "O número de páginas deve ser positivo.")
        Integer numeroPaginas,

        @NotNull(message = "O tipo de livro é obrigatório.")
        TipoLivro tipo,

        boolean disponivel
) {
}
