// Arquivo: src/main/java/br/com/biblioteca/biblioteca_api/categoria/CategoriaRepository.java

package br.com.biblioteca.biblioteca_api.categoria;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}