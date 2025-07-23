package br.com.biblioteca.biblioteca_api.service;

import br.com.biblioteca.biblioteca_api.model.Livro;
import br.com.biblioteca.biblioteca_api.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public Livro salvar(Livro livro) {
        return livroRepository.save(livro);
    }

    public List<Livro> buscarTodos() {
        return livroRepository.findAll();
    }

    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.findById(id);
    }

    public void deletar(Long id) {
        livroRepository.deleteById(id);
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContaining(titulo);
    }

    public List<Livro> buscarPorAutor(String autor) {
        return livroRepository.findByAutorContaining(autor);
    }

    public Livro buscarPorIsbn(String isbn) {
        return livroRepository.findByIsbn(isbn);
    }
}