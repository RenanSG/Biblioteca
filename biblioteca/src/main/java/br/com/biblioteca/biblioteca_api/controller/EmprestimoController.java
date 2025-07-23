package br.com.biblioteca.biblioteca_api.controller;

import br.com.biblioteca.biblioteca_api.dto.CriarEmprestimoDTO;
import br.com.biblioteca.biblioteca_api.model.Emprestimo;
import br.com.biblioteca.biblioteca_api.service.EmprestimoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    public ResponseEntity<Emprestimo> criar(@RequestBody CriarEmprestimoDTO dto) {
        try {
            Emprestimo novoEmprestimo = emprestimoService.criarEmprestimo(dto);
            return new ResponseEntity<>(novoEmprestimo, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/devolver")
    public ResponseEntity<Emprestimo> devolver(@PathVariable Long id) {
        try {
            Emprestimo emprestimoFinalizado = emprestimoService.finalizarEmprestimo(id);
            return ResponseEntity.ok(emprestimoFinalizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}