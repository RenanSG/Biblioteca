package br.com.biblioteca.biblioteca_api.repository;

import br.com.biblioteca.biblioteca_api.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {


    List<Emprestimo> findByUsuarioId(Long usuarioId);

    List<Emprestimo> findByDataEmprestimo(LocalDate data);

}