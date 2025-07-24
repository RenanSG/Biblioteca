// Arquivo: src/main/java/br/com/biblioteca/biblioteca_api/livro/Livro.java

package br.com.biblioteca.biblioteca_api.livro;

// Adicione a importação da nova entidade
import br.com.biblioteca.biblioteca_api.autor.Autor;
import jakarta.persistence.*; // Garanta que esta importação existe
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

    // Altere o campo 'autor' para o seguinte
    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    private String editora;
    private String isbn;
    private String tipo;
    private boolean disponivel = true;
}