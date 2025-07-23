package br.com.biblioteca.biblioteca_api.service;

import br.com.biblioteca.biblioteca_api.dto.CriarEmprestimoDTO;
import br.com.biblioteca.biblioteca_api.model.Emprestimo;
import br.com.biblioteca.biblioteca_api.model.Livro;
import br.com.biblioteca.biblioteca_api.model.Usuario;
import br.com.biblioteca.biblioteca_api.repository.EmprestimoRepository;
import br.com.biblioteca.biblioteca_api.repository.LivroRepository;
import br.com.biblioteca.biblioteca_api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, LivroRepository livroRepository, UsuarioRepository usuarioRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Emprestimo criarEmprestimo(CriarEmprestimoDTO dto) {
        Livro livro = livroRepository.findById(dto.livroId())
                .orElseThrow(() -> new RuntimeException("Livro não encontrado!"));

        if (!livro.isDisponivel()) {
            throw new RuntimeException("Livro já está emprestado!");
        }

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        livro.setDisponivel(false);
        livroRepository.save(livro);

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setUsuario(usuario);
        emprestimo.setDataEmprestimo(LocalDate.now());

        return emprestimoRepository.save(emprestimo);
    }

    @Transactional
    public Emprestimo finalizarEmprestimo(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado!"));

        if (emprestimo.getDataDevolucao() != null) {
            throw new RuntimeException("Este empréstimo já foi finalizado.");
        }

        Livro livro = emprestimo.getLivro();
        livro.setDisponivel(true);
        livroRepository.save(livro);

        emprestimo.setDataDevolucao(LocalDate.now());
        return emprestimoRepository.save(emprestimo);
    }

    public List<Emprestimo> listarEmprestimos(Long usuarioId, LocalDate data) {
        if (usuarioId != null) {
            return emprestimoRepository.findByUsuarioId(usuarioId);
        }
        if (data != null) {
            return emprestimoRepository.findByDataEmprestimo(data);
        }
        return emprestimoRepository.findAll();
    }



}