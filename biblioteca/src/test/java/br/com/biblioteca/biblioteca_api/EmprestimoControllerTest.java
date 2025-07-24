// Arquivo: src/test/java/br/com/biblioteca/biblioteca_api/EmprestimoControllerTest.java

package br.com.biblioteca.biblioteca_api;

import br.com.biblioteca.biblioteca_api.autor.Autor;
import br.com.biblioteca.biblioteca_api.autor.AutorRepository;
import br.com.biblioteca.biblioteca_api.categoria.Categoria;
import br.com.biblioteca.biblioteca_api.categoria.CategoriaRepository;
import br.com.biblioteca.biblioteca_api.emprestimo.CriarEmprestimoDTO;
import br.com.biblioteca.biblioteca_api.emprestimo.Emprestimo;
import br.com.biblioteca.biblioteca_api.emprestimo.EmprestimoRepository;
import br.com.biblioteca.biblioteca_api.livro.Livro;
import br.com.biblioteca.biblioteca_api.livro.LivroRepository;
import br.com.biblioteca.biblioteca_api.livro.TipoLivro;
import br.com.biblioteca.biblioteca_api.usuario.Usuario;
import br.com.biblioteca.biblioteca_api.usuario.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EmprestimoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EmprestimoRepository emprestimoRepository;
    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    @WithMockUser
    void deveCriarUmEmprestimo_E_RetornarStatus201() throws Exception {
        // CORREÇÃO: Criar Autor e Categoria antes de criar o Livro
        Autor autor = autorRepository.save(new Autor(null, "William Gibson", null, null));
        Categoria categoria = categoriaRepository.save(new Categoria(null, "Cyberpunk", null));
        Livro livro = livroRepository.save(new Livro(null, "Neuromancer", autor, categoria, "Aleph", "978-8576572937", 1984, 320, TipoLivro.FISICO, true));
        Usuario usuario = usuarioRepository.save(new Usuario(null, "Case", "case@email.com", "123"));
        CriarEmprestimoDTO dto = new CriarEmprestimoDTO(livro.getId(), usuario.getId());

        mockMvc.perform(post("/emprestimos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser
    void deveDevolverUmLivroEmprestado_E_RetornarStatus200() throws Exception {
        // CORREÇÃO: Criar Autor e Categoria antes de criar o Livro
        Autor autor = autorRepository.save(new Autor(null, "Ray Bradbury", null, null));
        Categoria categoria = categoriaRepository.save(new Categoria(null, "Distopia", null));
        Livro livro = livroRepository.save(new Livro(null, "Fahrenheit 451", autor, categoria, "Editora C", "978-0743247221", 1953, 256, TipoLivro.FISICO, false));
        Usuario usuario = usuarioRepository.save(new Usuario(null, "Guy Montag", "montag@email.com", "123"));
        Emprestimo emprestimo = emprestimoRepository.save(new Emprestimo(null, livro, usuario, LocalDate.now(), null));

        mockMvc.perform(patch("/emprestimos/" + emprestimo.getId() + "/devolver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.livro.disponivel").value(true))
                .andExpect(jsonPath("$.dataDevolucao").exists());
    }

    @Test
    @WithMockUser
    void deveRetornarRelatorioDeEmprestimosPorUsuario() throws Exception {
        // CORREÇÃO: Criar Autor e Categoria antes de criar o Livro
        Autor autor = autorRepository.save(new Autor(null, "Patrick Rothfuss", null, null));
        Categoria categoria = categoriaRepository.save(new Categoria(null, "Alta Fantasia", null));
        Livro livro = livroRepository.save(new Livro(null, "O Nome do Vento", autor, categoria, "Sextante", "978-8575424939", 2007, 656, TipoLivro.FISICO, false));
        Usuario usuario = usuarioRepository.save(new Usuario(null, "Kvothe", "kvothe@email.com", "123"));
        emprestimoRepository.save(new Emprestimo(null, livro, usuario, LocalDate.now(), null));

        mockMvc.perform(get("/emprestimos?usuarioId=" + usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].usuario.nome").value("Kvothe"));
    }

    @Test
    @WithMockUser
    void naoDeveCriarEmprestimoDeLivroIndisponivel_E_RetornarStatus400() throws Exception {
        // CORREÇÃO: Criar Autor e Categoria antes de criar o Livro
        Autor autor = autorRepository.save(new Autor(null, "Neal Stephenson", null, null));
        Categoria categoria = categoriaRepository.save(new Categoria(null, "Cyberpunk", null));
        Livro livro = livroRepository.save(new Livro(null, "Snow Crash", autor, categoria, "Editora B", "978-0553380958", 1992, 480, TipoLivro.FISICO, false));
        Usuario usuario = usuarioRepository.save(new Usuario(null, "Hiro Protagonist", "hiro@email.com", "123"));
        CriarEmprestimoDTO dto = new CriarEmprestimoDTO(livro.getId(), usuario.getId());

        mockMvc.perform(post("/emprestimos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}