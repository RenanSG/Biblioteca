package br.com.biblioteca.biblioteca_api.livro;

import br.com.biblioteca.biblioteca_api.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public List<Livro> buscar(String titulo, String autor, String isbn) {
        if (titulo != null) {
            return livroRepository.findByTituloContaining(titulo);
        }
        if (autor != null) {
            return livroRepository.findByAutorContaining(autor);
        }
        if (isbn != null) {
            Livro livro = livroRepository.findByIsbn(isbn);
            // Se o livro for encontrado, retorna uma lista com ele. Se não, uma lista vazia.
            return livro != null ? List.of(livro) : Collections.emptyList();
        }
        // Se nenhum parâmetro for fornecido, retorna todos os livros.
        return livroRepository.findAll();
    }

    public Livro salvar(Livro livro) {
        return livroRepository.save(livro);
    }

    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.findById(id);
    }

    public void deletar(Long id) {
        livroRepository.deleteById(id);
    }

    // Adicione este método dentro da classe LivroService...
    public Livro atualizar(Long id, Livro livroAtualizado) {
        return buscarPorId(id)
                .map(livroExistente -> {
                    livroExistente.setTitulo(livroAtualizado.getTitulo());
                    livroExistente.setAutor(livroAtualizado.getAutor());
                    livroExistente.setEditora(livroAtualizado.getEditora());
                    livroExistente.setIsbn(livroAtualizado.getIsbn());
                    livroExistente.setTipo(livroAtualizado.getTipo());
                    return salvar(livroExistente);
                })
                .orElseThrow(() -> new ObjectNotFoundException("Livro não encontrado com o ID: " + id));
    }
}