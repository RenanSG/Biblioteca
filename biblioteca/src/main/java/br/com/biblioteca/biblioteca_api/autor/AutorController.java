package br.com.biblioteca.biblioteca_api.autor;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping
    public ResponseEntity<List<Autor>> listarAutores() {
        return ResponseEntity.ok(autorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Autor> buscarAutorPorId(@PathVariable Long id) {
        Autor autor = autorService.buscarPorId(id);
        return ResponseEntity.ok(autor);
    }

    @PostMapping
    public ResponseEntity<Autor> criarAutor(@Valid @RequestBody AutorDTO dto) {
        Autor novoAutor = autorService.salvar(dto);
        return new ResponseEntity<>(novoAutor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Autor> atualizarAutor(@PathVariable Long id, @Valid @RequestBody AutorDTO dto) {
        Autor autorAtualizado = autorService.atualizar(id, dto);
        return ResponseEntity.ok(autorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAutor(@PathVariable Long id) {
        autorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
