package br.com.biblioteca.biblioteca_api;

import br.com.biblioteca.biblioteca_api.emprestimo.CriarEmprestimoDTO;
import br.com.biblioteca.biblioteca_api.emprestimo.Emprestimo;
import br.com.biblioteca.biblioteca_api.emprestimo.EmprestimoRepository;
import br.com.biblioteca.biblioteca_api.livro.Livro;
import br.com.biblioteca.biblioteca_api.livro.LivroRepository;
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

    @Test
    @WithMockUser
    void deveCriarUmEmprestimo_E_RetornarStatus201() throws Exception {
        Livro livro = livroRepository.save(new Livro(null, "Neuromancer", "William Gibson", "Aleph", "978-8576572937", "FISICO", true));
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
        Livro livro = livroRepository.save(new Livro(null, "Fahrenheit 451", "Ray Bradbury", "Editora C", "978-0743247221", "FISICO", false));
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
        Livro livro = livroRepository.save(new Livro(null, "O Nome do Vento", "Patrick Rothfuss", "Sextante", "978-8575424939", "FISICO", false));
        Usuario usuario = usuarioRepository.save(new Usuario(null, "Kvothe", "kvothe@email.com", "123"));
        emprestimoRepository.save(new Emprestimo(null, livro, usuario, LocalDate.now(), null));

        mockMvc.perform(get("/emprestimos?usuarioId=" + usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].usuario.nome").value("Kvothe"));
    }
}