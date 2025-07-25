package br.com.biblioteca.biblioteca_api.emprestimo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    public ResponseEntity<Emprestimo> criar(@RequestBody CriarEmprestimoDTO dto) {
        // Remova o try-catch
        Emprestimo novoEmprestimo = emprestimoService.criarEmprestimo(dto);
        return new ResponseEntity<>(novoEmprestimo, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/devolver")
    public ResponseEntity<Emprestimo> devolver(@PathVariable Long id) {
        // Remova o try-catch
        Emprestimo emprestimoFinalizado = emprestimoService.finalizarEmprestimo(id);
        return ResponseEntity.ok(emprestimoFinalizado);
    }

    @GetMapping
    public ResponseEntity<List<Emprestimo>> relatorio(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) LocalDate data) {
        List<Emprestimo> emprestimos = emprestimoService.listarEmprestimos(usuarioId, data);
        return ResponseEntity.ok(emprestimos);
    }

}