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
    public ResponseEntity<Livro> criarLivro(@RequestBody Livro livro) {
        Livro novoLivro = livroService.salvar(livro);
        return new ResponseEntity<>(novoLivro, HttpStatus.CREATED); // Retorna o livro criado e o status 201 Created.
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
                .map(ResponseEntity::ok) // Se encontrar, retorna o livro com status 200 OK.
                .orElse(ResponseEntity.notFound().build()); // Se não, retorna status 404 Not Found.
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivro(@PathVariable Long id) {
        livroService.deletar(id);
        return ResponseEntity.noContent().build(); // Retorna status 204 No Content.
    }

}