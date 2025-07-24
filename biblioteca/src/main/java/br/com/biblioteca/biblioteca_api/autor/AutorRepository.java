// Arquivo: src/main/java/br/com/biblioteca/biblioteca_api/autor/AutorRepository.java

package br.com.biblioteca.biblioteca_api.autor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, Long> {
}