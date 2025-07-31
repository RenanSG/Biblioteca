package br.com.biblioteca.biblioteca_api.livro;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    public ResponseEntity<Livro> criarLivro(@RequestBody LivroDTO dto) {
        Livro novoLivro = livroService.salvar(dto);
        return new ResponseEntity<>(novoLivro, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizarLivro(@PathVariable Long id, @RequestBody LivroDTO dto) {
        Livro livroAtualizado = livroService.atualizar(id, dto);
        return ResponseEntity.ok(livroAtualizado);
    }

    @GetMapping
    public ResponseEntity<List<Livro>> buscarLivros(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String isbn) {
        return ResponseEntity.ok(livroService.buscar(titulo, autor, isbn));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable Long id) {
        return livroService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivro(@PathVariable Long id) {
        livroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}