package br.com.biblioteca.biblioteca_api.config;

import br.com.biblioteca.biblioteca_api.livro.Livro;
import br.com.biblioteca.biblioteca_api.livro.LivroRepository;
import br.com.biblioteca.biblioteca_api.usuario.Usuario;
import br.com.biblioteca.biblioteca_api.usuario.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeedingLoader implements CommandLineRunner {

    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;

    public DataSeedingLoader(LivroRepository livroRepository, UsuarioRepository usuarioRepository) {
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (livroRepository.count() == 0 && usuarioRepository.count() == 0) {
            System.out.println("Populando banco de dados com dados iniciais...");
            popularLivros();
            popularUsuarios();
            System.out.println("Banco de dados populado com sucesso.");
        }
    }

    private void popularLivros() {
        livroRepository.saveAll(List.of(
                // Ficção Científica
                new Livro(null, "Duna", "Frank Herbert", "Aleph", "978-8576570988", "FISICO", true),
                new Livro(null, "Fundação", "Isaac Asimov", "Aleph", "978-8576570346", "DIGITAL", true),
                new Livro(null, "Neuromancer", "William Gibson", "Aleph", "978-8576572937", "FISICO", true),
                new Livro(null, "O Guia do Mochileiro das Galáxias", "Douglas Adams", "Arqueiro", "978-8599296387", "FISICO", true),

                // Fantasia
                new Livro(null, "O Senhor dos Anéis: A Sociedade do Anel", "J.R.R. Tolkien", "HarperCollins", "978-8595084759", "FISICO", true),
                new Livro(null, "O Hobbit", "J.R.R. Tolkien", "HarperCollins", "978-8595084742", "DIGITAL", true),
                new Livro(null, "A Guerra dos Tronos", "George R. R. Martin", "Suma", "978-8556510785", "FISICO", true),
                new Livro(null, "O Nome do Vento", "Patrick Rothfuss", "Arqueiro", "978-8599296493", "FISICO", true),

                // Clássicos
                new Livro(null, "1984", "George Orwell", "Companhia das Letras", "978-8535914849", "FISICO", true),
                new Livro(null, "Dom Casmurro", "Machado de Assis", "Principis", "978-6555522238", "DIGITAL", true),
                new Livro(null, "O Grande Gatsby", "F. Scott Fitzgerald", "Principis", "978-6555523969", "FISICO", true),
                new Livro(null, "O Apanhador no Campo de Centeio", "J.D. Salinger", "Todavia", "978-6580309386", "FISICO", true),

                // Não-ficção
                new Livro(null, "Sapiens: Uma Breve História da Humanidade", "Yuval Noah Harari", "L&PM", "978-8525434812", "DIGITAL", true),
                new Livro(null, "O Poder do Hábito", "Charles Duhigg", "Objetiva", "978-8539004119", "FISICO", true)
        ));
    }

    private void popularUsuarios() {
        usuarioRepository.saveAll(List.of(
                new Usuario(null, "Ana Clara"),
                new Usuario(null, "Bruno Costa"),
                new Usuario(null, "Carlos Eduardo"),
                new Usuario(null, "Daniela Ferraz"),
                new Usuario(null, "Eduardo Moreira")
        ));
    }
}