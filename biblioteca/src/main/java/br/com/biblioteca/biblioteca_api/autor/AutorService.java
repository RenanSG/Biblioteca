// Arquivo: src/main/java/br/com/biblioteca/biblioteca_api/autor/AutorService.java

package br.com.biblioteca.biblioteca_api.autor;

import br.com.biblioteca.biblioteca_api.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public List<Autor> listarTodos() {
        return autorRepository.findAll();
    }

    public Autor buscarPorId(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Autor não encontrado com o ID: " + id));
    }

    public Autor salvar(Autor autor) {
        return autorRepository.save(autor);
    }

    public Autor atualizar(Long id, Autor autorAtualizado) {
        Autor autorExistente = buscarPorId(id);
        autorExistente.setNome(autorAtualizado.getNome());
        return autorRepository.save(autorExistente);
    }

    public void deletar(Long id) {
        buscarPorId(id); // Garante que o autor existe antes de tentar deletar
        autorRepository.deleteById(id);
    }
}