package br.com.biblioteca.biblioteca_api.config;

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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public DataSeedingLoader(LivroRepository livroRepository, UsuarioRepository usuarioRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
        // Seu método popularLivros() permanece o mesmo
        livroRepository.saveAll(List.of(
                // Ficção Científica
                new Livro(null, "Duna", "Frank Herbert", "Aleph", "978-8576570988", "FISICO", true),
                new Livro(null, "Fundação", "Isaac Asimov", "Aleph", "978-8576570346", "DIGITAL", true),
                new Livro(null, "Neuromancer", "William Gibson", "Aleph", "978-8576572937", "FISICO", true),
                new Livro(null, "O Guia do Mochileiro das Galáxias", "Douglas Adams", "Arqueiro", "978-8599296387", "FISICO", true)
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