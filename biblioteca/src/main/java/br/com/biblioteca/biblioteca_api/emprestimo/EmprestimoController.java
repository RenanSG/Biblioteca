package br.com.biblioteca.biblioteca_api.emprestimo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    public ResponseEntity<Emprestimo> criarEmprestimo(@Valid @RequestBody CriarEmprestimoDTO dto) {
        Emprestimo novoEmprestimo = emprestimoService.criarEmprestimo(dto);
        return new ResponseEntity<>(novoEmprestimo, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/devolver")
    public ResponseEntity<Emprestimo> finalizarEmprestimo(@PathVariable Long id) {
        Emprestimo emprestimoFinalizado = emprestimoService.finalizarEmprestimo(id);
        return ResponseEntity.ok(emprestimoFinalizado);
    }

    @GetMapping
    public ResponseEntity<List<Emprestimo>> listarEmprestimos(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) LocalDate data) {
        List<Emprestimo> emprestimos = emprestimoService.listarEmprestimos(usuarioId, data);
        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Long>> estatisticas() {
        return ResponseEntity.ok(emprestimoService.obterEstatisticas());
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<EmprestimoStatusDTO> verificarStatus(@PathVariable Long id) {
        return ResponseEntity.ok(emprestimoService.verificarStatus(id));
    }
}
