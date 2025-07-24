package br.com.biblioteca.biblioteca_api.livro;

import br.com.biblioteca.biblioteca_api.autor.Autor;
import br.com.biblioteca.biblioteca_api.autor.AutorRepository;
import br.com.biblioteca.biblioteca_api.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository; // Injeção do novo repositório

    // Atualize o construtor
    public LivroService(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    // O método buscar por autor precisa ser ajustado
    public List<Livro> buscar(String titulo, String autor, String isbn) {
        if (autor != null) {
            return livroRepository.findByAutorNomeContaining(autor);
        }
        // ... o restante do método permanece igual
        if (titulo != null) {
            return livroRepository.findByTituloContaining(titulo);
        }
        if (isbn != null) {
            Livro livro = livroRepository.findByIsbn(isbn);
            return livro != null ? List.of(livro) : Collections.emptyList();
        }
        return livroRepository.findAll();
    }

    // Altere a assinatura do método 'salvar' para receber o DTO
    public Livro salvar(LivroDTO dto) {
        Autor autor = autorRepository.findById(dto.autorId())
                .orElseThrow(() -> new ObjectNotFoundException("Autor não encontrado!"));

        Livro livro = new Livro(null, dto.titulo(), autor, dto.editora(), dto.isbn(), dto.tipo(), dto.disponivel());
        return livroRepository.save(livro);
    }

    // Altere a assinatura do método 'atualizar' para receber o DTO
    public Livro atualizar(Long id, LivroDTO dto) {
        Livro livroExistente = buscarPorId(id)
                .orElseThrow(() -> new ObjectNotFoundException("Livro não encontrado com o ID: " + id));

        Autor autor = autorRepository.findById(dto.autorId())
                .orElseThrow(() -> new ObjectNotFoundException("Autor não encontrado!"));

        livroExistente.setTitulo(dto.titulo());
        livroExistente.setAutor(autor);
        livroExistente.setEditora(dto.editora());
        livroExistente.setIsbn(dto.isbn());
        livroExistente.setTipo(dto.tipo());
        livroExistente.setDisponivel(dto.disponivel());

        return livroRepository.save(livroExistente);
    }

    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.findById(id);
    }

    public void deletar(Long id) {
        livroRepository.deleteById(id);
    }
}