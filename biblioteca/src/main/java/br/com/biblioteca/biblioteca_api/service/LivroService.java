package br.com.biblioteca.biblioteca_api.service;

import br.com.biblioteca.biblioteca_api.model.Livro;
import br.com.biblioteca.biblioteca_api.repository.LivroRepository;
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

}