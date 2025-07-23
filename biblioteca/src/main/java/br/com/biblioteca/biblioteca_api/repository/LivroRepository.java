package br.com.biblioteca.biblioteca_api.repository;

import br.com.biblioteca.biblioteca_api.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByTituloContaining(String titulo);

    List<Livro> findByAutorContaining(String autor);

    Livro findByIsbn(String isbn);
}