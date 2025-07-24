// Arquivo: src/main/java/br/com/biblioteca/biblioteca_api/livro/LivroDTO.java

package br.com.biblioteca.biblioteca_api.livro;

// Atualize o record com os novos campos
public record LivroDTO(
        String titulo,
        Long autorId,
        Long categoriaId, // Adicionado
        String editora,
        String isbn,
        Integer anoPublicacao, // Adicionado
        Integer numeroPaginas, // Adicionado
        TipoLivro tipo, // Mude para o Enum
        boolean disponivel
) {
}