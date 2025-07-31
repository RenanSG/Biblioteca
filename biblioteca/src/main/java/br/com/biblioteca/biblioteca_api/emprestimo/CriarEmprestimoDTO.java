package br.com.biblioteca.biblioteca_api.emprestimo;

import jakarta.validation.constraints.NotNull;

public record CriarEmprestimoDTO(
        @NotNull(message = "O ID do livro é obrigatório.")
        Long livroId,

        @NotNull(message = "O ID do usuário é obrigatório.")
        Long usuarioId
) {
}
