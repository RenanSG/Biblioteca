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

import java.util.List;

@Component
public class DataSeedingLoader implements CommandLineRunner {

    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;
    private final AutorRepository autorRepository; // Adicione o repositório do autor
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // Atualize o construtor
    public DataSeedingLoader(LivroRepository livroRepository, UsuarioRepository usuarioRepository, AutorRepository autorRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
        this.autorRepository = autorRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (livroRepository.count() == 0 && usuarioRepository.count() == 0 && autorRepository.count() == 0) {
            System.out.println("Populando banco de dados com dados iniciais...");
            popularAutores(); // Adicione esta chamada
            popularLivros();
            popularUsuarios();
            System.out.println("Banco de dados populado com sucesso.");
        }
    }

    // Crie este novo método
    private void popularAutores() {
        autorRepository.saveAll(List.of(
                new Autor(null, "Frank Herbert"),
                new Autor(null, "Isaac Asimov"),
                new Autor(null, "William Gibson"),
                new Autor(null, "Douglas Adams")
        ));
    }

    private void popularLivros() {
        // Busque os autores recém-criados para associá-los aos livros
        Autor herbert = autorRepository.findById(1L).get();
        Autor asimov = autorRepository.findById(2L).get();
        Autor gibson = autorRepository.findById(3L).get();
        Autor adams = autorRepository.findById(4L).get();

        livroRepository.saveAll(List.of(
                new Livro(null, "Duna", herbert, "Aleph", "978-8576570988", "FISICO", true),
                new Livro(null, "Fundação", asimov, "Aleph", "978-8576570346", "DIGITAL", true),
                new Livro(null, "Neuromancer", gibson, "Aleph", "978-8576572937", "FISICO", true),
                new Livro(null, "O Guia do Mochileiro das Galáxias", adams, "Arqueiro", "978-8599296387", "FISICO", true)
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
