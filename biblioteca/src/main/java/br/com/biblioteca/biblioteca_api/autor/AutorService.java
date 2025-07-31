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

    public Autor salvar(AutorDTO dto) {
        Autor autor = new Autor();
        autor.setNome(dto.nome());
        autor.setDataNascimento(dto.dataNascimento());
        autor.setBiografia(dto.biografia());
        return autorRepository.save(autor);
    }

    public Autor atualizar(Long id, AutorDTO dto) {
        Autor autorExistente = buscarPorId(id);
        autorExistente.setNome(dto.nome());
        autorExistente.setDataNascimento(dto.dataNascimento());
        autorExistente.setBiografia(dto.biografia());
        return autorRepository.save(autorExistente);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        autorRepository.deleteById(id);
    }
}
