package br.com.biblioteca.biblioteca_api.livro;

public record LivroDTO(
        String titulo,
        Long autorId,
        String editora,
        String isbn,
        String tipo,
        boolean disponivel
) {
}