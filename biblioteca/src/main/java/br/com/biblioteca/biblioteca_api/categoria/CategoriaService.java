package br.com.biblioteca.biblioteca_api.categoria;

import br.com.biblioteca.biblioteca_api.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Categoria não encontrada com o ID: " + id));
    }

    public Categoria salvar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria atualizar(Long id, Categoria categoriaAtualizada) {
        Categoria categoriaExistente = buscarPorId(id);
        categoriaExistente.setNome(categoriaAtualizada.getNome());
        categoriaExistente.setDescricao(categoriaAtualizada.getDescricao());
        return categoriaRepository.save(categoriaExistente);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        categoriaRepository.deleteById(id);
    }
}