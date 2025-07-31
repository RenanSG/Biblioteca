package br.com.biblioteca.biblioteca_api.emprestimo;

import br.com.biblioteca.biblioteca_api.config.security.UserSS;
import br.com.biblioteca.biblioteca_api.exceptions.DataIntegrityException;
import br.com.biblioteca.biblioteca_api.exceptions.ObjectNotFoundException;
import br.com.biblioteca.biblioteca_api.livro.Livro;
import br.com.biblioteca.biblioteca_api.livro.LivroRepository;
import br.com.biblioteca.biblioteca_api.usuario.Roles;
import br.com.biblioteca.biblioteca_api.usuario.Usuario;
import br.com.biblioteca.biblioteca_api.usuario.UsuarioRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class EmprestimoService {

    private static final int DIAS_LIMITE_EMPRESTIMO = 15;
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
        UserSS user = getAuthenticatedUser();
        if (user.hasRole(Roles.CLIENTE) && !Objects.equals(user.getId(), dto.usuarioId())) {
            throw new AccessDeniedException("Acesso negado. Você só pode criar empréstimos para si mesmo.");
        }

        Livro livro = livroRepository.findById(dto.livroId())
                .orElseThrow(() -> new ObjectNotFoundException("Livro não encontrado!"));

        if (!livro.isDisponivel()) {
            throw new DataIntegrityException("Livro já está emprestado!");
        }

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado!"));

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
        UserSS user = getAuthenticatedUser();
        Emprestimo emprestimo = findById(emprestimoId);

        if (user.hasRole(Roles.CLIENTE) && !Objects.equals(user.getId(), emprestimo.getUsuario().getId())) {
            throw new AccessDeniedException("Acesso negado. Você não pode finalizar um empréstimo de outro usuário.");
        }

        if (emprestimo.getDataDevolucao() != null) {
            throw new DataIntegrityException("Este empréstimo já foi finalizado.");
        }

        Livro livro = emprestimo.getLivro();
        livro.setDisponivel(true);
        livroRepository.save(livro);

        emprestimo.setDataDevolucao(LocalDate.now());
        return emprestimoRepository.save(emprestimo);
    }

    public List<Emprestimo> listarEmprestimos(Long usuarioId, LocalDate data) {
        UserSS user = getAuthenticatedUser();
        if (user.hasRole(Roles.CLIENTE) && !Objects.equals(user.getId(), usuarioId)) {
            throw new AccessDeniedException("Acesso negado. Você só pode listar seus próprios empréstimos.");
        }

        if (usuarioId != null) {
            return emprestimoRepository.findByUsuarioId(usuarioId);
        }
        if (data != null && user.hasRole(Roles.CLIENTE)) {
            throw new AccessDeniedException("Acesso negado. A busca por data é permitida apenas para administradores.");
        }
        if (data != null) {
            return emprestimoRepository.findByDataEmprestimo(data);
        }

        if (user.hasRole(Roles.CLIENTE)) {
            return emprestimoRepository.findByUsuarioId(user.getId());
        }
        return emprestimoRepository.findAll();
    }

    public Map<String, Long> obterEstatisticas() {
        long totalEmprestimos = emprestimoRepository.count();
        return Map.of("totalEmprestimos", totalEmprestimos);
    }

    public EmprestimoStatusDTO verificarStatus(Long emprestimoId) {
        UserSS user = getAuthenticatedUser();
        Emprestimo emprestimo = findById(emprestimoId);

        if (user.hasRole(Roles.CLIENTE) && !Objects.equals(user.getId(), emprestimo.getUsuario().getId())) {
            throw new AccessDeniedException("Acesso negado. Você não pode verificar o status de um empréstimo de outro usuário.");
        }

        long diasDeEmprestimo = ChronoUnit.DAYS.between(emprestimo.getDataEmprestimo(), LocalDate.now());

        String status;
        if (emprestimo.getDataDevolucao() != null) {
            status = "Finalizado";
        } else if (diasDeEmprestimo > DIAS_LIMITE_EMPRESTIMO) {
            status = "Atrasado";
        } else {
            status = "Em Andamento";
        }

        return new EmprestimoStatusDTO(emprestimoId, status);
    }

    private Emprestimo findById(Long emprestimoId) {
        return emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new ObjectNotFoundException("Empréstimo não encontrado!"));
    }

    private UserSS getAuthenticatedUser() {
        try {
            return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new AccessDeniedException("Acesso negado. Nenhum usuário autenticado encontrado.");
        }
    }
}
