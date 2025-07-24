// Arquivo: src/main/java/br/com/biblioteca/biblioteca_api/livro/Livro.java

package br.com.biblioteca.biblioteca_api.livro;

import br.com.biblioteca.biblioteca_api.autor.Autor;
import br.com.biblioteca.biblioteca_api.categoria.Categoria; // Importe Categoria
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    // Adicione a relação com Categoria
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    private String editora;
    private String isbn;
    private Integer anoPublicacao; // Novo campo
    private Integer numeroPaginas; // Novo campo

    // Mude o tipo para o Enum e anote
    @Enumerated(EnumType.STRING)
    private TipoLivro tipo;

    private boolean disponivel = true;
}