package br.com.biblioteca.biblioteca_api.config;

import br.com.biblioteca.biblioteca_api.autor.Autor;
import br.com.biblioteca.biblioteca_api.autor.AutorRepository;
import br.com.biblioteca.biblioteca_api.livro.Livro;
import br.com.biblioteca.biblioteca_api.livro.LivroRepository;
import br.com.biblioteca.biblioteca_api.usuario.Roles;
import br.com.biblioteca.biblioteca_api.usuario.Usuario;
import br.com.biblioteca.biblioteca_api.usuario.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import br.com.biblioteca.biblioteca_api.categoria.Categoria;
import br.com.biblioteca.biblioteca_api.categoria.CategoriaRepository;
import br.com.biblioteca.biblioteca_api.livro.TipoLivro;
import java.time.LocalDate;

import java.util.List;

@Component
public class DataSeedingLoader implements CommandLineRunner {

    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;
    private final AutorRepository autorRepository; // Adicione o repositório do autor
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final CategoriaRepository categoriaRepository;

    public DataSeedingLoader(LivroRepository livroRepository, UsuarioRepository usuarioRepository, AutorRepository autorRepository, CategoriaRepository categoriaRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (livroRepository.count() == 0 && usuarioRepository.count() == 0 && autorRepository.count() == 0) {
            System.out.println("Populando banco de dados com dados iniciais...");
            popularAutores();
            popularCategorias(); // Adicione esta chamada
            popularLivros();
            popularUsuarios();
            System.out.println("Banco de dados populado com sucesso.");
        }
    }

    private void popularAutores() {
        autorRepository.saveAll(List.of(
                new Autor(null, "Frank Herbert", LocalDate.of(1920, 10, 8), "Autor americano de ficção científica, mais conhecido pela série Duna."),
                new Autor(null, "Isaac Asimov", LocalDate.of(1920, 1, 2), "Escritor e professor de bioquímica, famoso por suas obras de ficção científica e divulgação científica."),
                new Autor(null, "William Gibson", LocalDate.of(1948, 3, 17), "Considerado o pai do cyberpunk, Gibson cunhou o termo 'ciberespaço'."),
                new Autor(null, "Douglas Adams", LocalDate.of(1952, 3, 11), "Escritor e comediante britânico, autor de 'O Guia do Mochileiro das Galáxias'.")
        ));
    }

    // Crie este novo método
    private void popularCategorias() {
        categoriaRepository.saveAll(List.of(
                new Categoria(null, "Ficção Científica", "Obras que exploram conceitos imaginativos baseados na ciência."),
                new Categoria(null, "Fantasia", "Gênero com elementos mágicos e sobrenaturais."),
                new Categoria(null, "Comédia", "Obras com o objetivo de provocar humor e riso.")
        ));
    }

    private void popularLivros() {
        Autor herbert = autorRepository.findById(1L).get();
        Autor asimov = autorRepository.findById(2L).get();
        Autor gibson = autorRepository.findById(3L).get();
        Autor adams = autorRepository.findById(4L).get();

        Categoria ficcao = categoriaRepository.findById(1L).get();
        Categoria comedia = categoriaRepository.findById(3L).get();

        livroRepository.saveAll(List.of(
                new Livro(null, "Duna", herbert, ficcao, "Aleph", "978-8576570988", 1965, 688, TipoLivro.FISICO, true),
                new Livro(null, "Fundação", asimov, ficcao, "Aleph", "978-8576570346", 1951, 240, TipoLivro.DIGITAL, true),
                new Livro(null, "Neuromancer", gibson, ficcao, "Aleph", "978-8576572937", 1984, 320, TipoLivro.FISICO, true),
                new Livro(null, "O Guia do Mochileiro das Galáxias", adams, comedia, "Arqueiro", "978-8599296387", 1979, 208, TipoLivro.FISICO, true)
        ));
    }

    private void popularUsuarios() {
        Usuario admin = new Usuario(null, "Admin", "admin@email.com", bCryptPasswordEncoder.encode("123"));
        admin.addRole(Roles.ADMIN);

        Usuario user1 = new Usuario(null, "Ana Clara", "ana@email.com", bCryptPasswordEncoder.encode("123"));
        user1.addRole(Roles.CLIENTE);

        Usuario user2 = new Usuario(null, "Bruno Costa", "bruno@email.com", bCryptPasswordEncoder.encode("123"));
        user2.addRole(Roles.CLIENTE);

        usuarioRepository.saveAll(List.of(admin, user1, user2));
    }
}
