package br.com.biblioteca.biblioteca_api.config;

import br.com.biblioteca.biblioteca_api.autor.Autor;
import br.com.biblioteca.biblioteca_api.autor.AutorRepository;
import br.com.biblioteca.biblioteca_api.categoria.Categoria;
import br.com.biblioteca.biblioteca_api.categoria.CategoriaRepository;
import br.com.biblioteca.biblioteca_api.emprestimo.Emprestimo;
import br.com.biblioteca.biblioteca_api.emprestimo.EmprestimoRepository;
import br.com.biblioteca.biblioteca_api.livro.Livro;
import br.com.biblioteca.biblioteca_api.livro.LivroRepository;
import br.com.biblioteca.biblioteca_api.livro.TipoLivro;
import br.com.biblioteca.biblioteca_api.usuario.Roles;
import br.com.biblioteca.biblioteca_api.usuario.Usuario;
import br.com.biblioteca.biblioteca_api.usuario.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DataSeedingLoader implements CommandLineRunner {

    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public DataSeedingLoader(LivroRepository livroRepository, UsuarioRepository usuarioRepository, AutorRepository autorRepository, CategoriaRepository categoriaRepository, EmprestimoRepository emprestimoRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
        this.emprestimoRepository = emprestimoRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (autorRepository.count() == 0 && categoriaRepository.count() == 0 && usuarioRepository.count() == 0) {
            System.out.println("Populando banco de dados com dados iniciais...");
            Map<String, Autor> autores = popularAutores();
            Map<String, Categoria> categorias = popularCategorias();
            Map<String, Usuario> usuarios = popularUsuarios();
            List<Livro> livros = popularLivros(autores, categorias);
            popularEmprestimos(livros, usuarios);
            System.out.println("Banco de dados populado com sucesso.");
        }
    }

    private Map<String, Autor> popularAutores() {
        List<Autor> autoresList = List.of(
                new Autor(null, "Frank Herbert", LocalDate.of(1920, 10, 8), "Autor americano de ficção científica, mais conhecido pela série Duna."),
                new Autor(null, "Isaac Asimov", LocalDate.of(1920, 1, 2), "Escritor e professor de bioquímica, famoso por suas obras de ficção científica e divulgação científica."),
                new Autor(null, "William Gibson", LocalDate.of(1948, 3, 17), "Considerado o pai do cyberpunk, Gibson cunhou o termo 'ciberespaço'."),
                new Autor(null, "Douglas Adams", LocalDate.of(1952, 3, 11), "Escritor e comediante britânico, autor de 'O Guia do Mochileiro das Galáxias'."),
                new Autor(null, "J.R.R. Tolkien", LocalDate.of(1892, 1, 3), "Filólogo e escritor britânico, autor de 'O Senhor dos Anéis'."),
                new Autor(null, "George Orwell", LocalDate.of(1903, 6, 25), "Romancista, ensaísta e jornalista britânico, autor de '1984'."),
                new Autor(null, "Machado de Assis", LocalDate.of(1839, 6, 21), "Escritor brasileiro, amplamente considerado o maior nome da literatura nacional."),
                new Autor(null, "Clarice Lispector", LocalDate.of(1920, 12, 10), "Escritora e jornalista brasileira nascida na Ucrânia, uma das figuras mais importantes da literatura do século XX."),
                new Autor(null, "Agatha Christie", LocalDate.of(1890, 9, 15), "Escritora britânica de romances policiais, conhecida como a 'Rainha do Crime'.")
        );
        return autorRepository.saveAll(autoresList).stream()
                .collect(Collectors.toMap(Autor::getNome, autor -> autor));
    }

    private Map<String, Categoria> popularCategorias() {
        List<Categoria> categoriasList = List.of(
                new Categoria(null, "Ficção Científica", "Obras que exploram conceitos imaginativos baseados na ciência."),
                new Categoria(null, "Fantasia", "Gênero com elementos mágicos e sobrenaturais."),
                new Categoria(null, "Comédia", "Obras com o objetivo de provocar humor e riso."),
                new Categoria(null, "Distopia", "Obras que retratam sociedades opressivas e controladoras."),
                new Categoria(null, "Romance Policial", "Gênero focado na resolução de crimes."),
                new Categoria(null, "Literatura Brasileira", "Obras clássicas e contemporâneas do Brasil.")
        );
        return categoriaRepository.saveAll(categoriasList).stream()
                .collect(Collectors.toMap(Categoria::getNome, categoria -> categoria));
    }

    private Map<String, Usuario> popularUsuarios() {
        Usuario admin = new Usuario(null, "Admin", "admin@email.com", bCryptPasswordEncoder.encode("123"));
        admin.addRole(Roles.ADMIN);

        Usuario user1 = new Usuario(null, "Ana Clara", "ana@email.com", bCryptPasswordEncoder.encode("123"));
        user1.addRole(Roles.CLIENTE);

        Usuario user2 = new Usuario(null, "Bruno Costa", "bruno@email.com", bCryptPasswordEncoder.encode("123"));
        user2.addRole(Roles.CLIENTE);

        Usuario user3 = new Usuario(null, "Carla Dias", "carla@email.com", bCryptPasswordEncoder.encode("123"));
        user3.addRole(Roles.CLIENTE);

        List<Usuario> usuariosList = List.of(admin, user1, user2, user3);
        return usuarioRepository.saveAll(usuariosList).stream()
                .collect(Collectors.toMap(Usuario::getEmail, usuario -> usuario));
    }

    private List<Livro> popularLivros(Map<String, Autor> autores, Map<String, Categoria> categorias) {
        List<Livro> livrosList = List.of(
                new Livro(null, "Duna", autores.get("Frank Herbert"), categorias.get("Ficção Científica"), "Aleph", "978-8576570988", 1965, 688, TipoLivro.FISICO, true),
                new Livro(null, "Fundação", autores.get("Isaac Asimov"), categorias.get("Ficção Científica"), "Aleph", "978-8576570346", 1951, 240, TipoLivro.DIGITAL, true),
                new Livro(null, "Neuromancer", autores.get("William Gibson"), categorias.get("Ficção Científica"), "Aleph", "978-8576572937", 1984, 320, TipoLivro.FISICO, true),
                new Livro(null, "O Senhor dos Anéis: A Sociedade do Anel", autores.get("J.R.R. Tolkien"), categorias.get("Fantasia"), "HarperCollins", "978-8595084759", 1954, 576, TipoLivro.FISICO, true),
                new Livro(null, "1984", autores.get("George Orwell"), categorias.get("Distopia"), "Companhia das Letras", "978-8535914849", 1949, 416, TipoLivro.FISICO, false),
                new Livro(null, "O Guia do Mochileiro das Galáxias", autores.get("Douglas Adams"), categorias.get("Comédia"), "Arqueiro", "978-8599296387", 1979, 208, TipoLivro.FISICO, true),
                new Livro(null, "Dom Casmurro", autores.get("Machado de Assis"), categorias.get("Literatura Brasileira"), "Penguin Classics", "978-0143107519", 1899, 256, TipoLivro.DIGITAL, true),
                new Livro(null, "A Hora da Estrela", autores.get("Clarice Lispector"), categorias.get("Literatura Brasileira"), "Rocco", "978-8532505994", 1977, 88, TipoLivro.FISICO, true),
                new Livro(null, "O Assassinato no Expresso do Oriente", autores.get("Agatha Christie"), categorias.get("Romance Policial"), "HarperCollins", "978-0062693662", 1934, 272, TipoLivro.FISICO, false)
        );
        return livroRepository.saveAll(livrosList);
    }

    private void popularEmprestimos(List<Livro> livros, Map<String, Usuario> usuarios) {
        Emprestimo emprestimo1 = new Emprestimo();
        emprestimo1.setLivro(livros.get(0));
        emprestimo1.setUsuario(usuarios.get("ana@email.com"));
        emprestimo1.setDataEmprestimo(LocalDate.now().minusDays(30));
        emprestimo1.setDataDevolucao(LocalDate.now().minusDays(15));
        livros.get(0).setDisponivel(true);

        Emprestimo emprestimo2 = new Emprestimo();
        emprestimo2.setLivro(livros.get(4));
        emprestimo2.setUsuario(usuarios.get("bruno@email.com"));
        emprestimo2.setDataEmprestimo(LocalDate.now().minusDays(20));

        Emprestimo emprestimo3 = new Emprestimo();
        emprestimo3.setLivro(livros.get(8));
        emprestimo3.setUsuario(usuarios.get("carla@email.com"));
        emprestimo3.setDataEmprestimo(LocalDate.now().minusDays(5));

        emprestimoRepository.saveAll(List.of(emprestimo1, emprestimo2, emprestimo3));
        livroRepository.saveAll(livros);
    }
}
