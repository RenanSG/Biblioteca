package br.com.biblioteca.biblioteca_api;

import br.com.biblioteca.biblioteca_api.dto.CriarEmprestimoDTO;
import br.com.biblioteca.biblioteca_api.model.Emprestimo; // Import que faltava
import br.com.biblioteca.biblioteca_api.model.Livro;
import br.com.biblioteca.biblioteca_api.model.Usuario;
import br.com.biblioteca.biblioteca_api.repository.EmprestimoRepository; // Campo novo
import br.com.biblioteca.biblioteca_api.repository.LivroRepository;
import br.com.biblioteca.biblioteca_api.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate; // Import que faltava

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
    private EmprestimoRepository emprestimoRepository; // Campo que faltava

    @Test
    void deveCriarUmEmprestimo_E_RetornarStatus201() throws Exception {
        Livro livro = livroRepository.save(new Livro(null, "Neuromancer", "William Gibson", "Aleph", "978-8576572937", "FISICO", true));
        Usuario usuario = usuarioRepository.save(new Usuario(null, "Case"));
        CriarEmprestimoDTO dto = new CriarEmprestimoDTO(livro.getId(), usuario.getId());

        mockMvc.perform(post("/emprestimos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.livro.titulo").value("Neuromancer"))
                .andExpect(jsonPath("$.usuario.nome").value("Case"))
                .andExpect(jsonPath("$.dataDevolucao").isEmpty());
    }

    @Test
    void naoDeveCriarEmprestimoDeLivroIndisponivel_E_RetornarStatus400() throws Exception {
        Livro livro = livroRepository.save(new Livro(null, "Snow Crash", "Neal Stephenson", "Editora B", "978-0553380958", "FISICO", false));
        Usuario usuario = usuarioRepository.save(new Usuario(null, "Hiro Protagonist"));
        CriarEmprestimoDTO dto = new CriarEmprestimoDTO(livro.getId(), usuario.getId());

        mockMvc.perform(post("/emprestimos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveDevolverUmLivroEmprestado_E_RetornarStatus200() throws Exception {
        Livro livro = livroRepository.save(new Livro(null, "Fahrenheit 451", "Ray Bradbury", "Editora C", "978-0743247221", "FISICO", false));
        Usuario usuario = usuarioRepository.save(new Usuario(null, "Guy Montag"));
        Emprestimo emprestimo = emprestimoRepository.save(new Emprestimo(null, livro, usuario, LocalDate.now(), null));

        mockMvc.perform(patch("/emprestimos/" + emprestimo.getId() + "/devolver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.livro.disponivel").value(true))
                .andExpect(jsonPath("$.dataDevolucao").exists());
    }

    @Test
    void deveRetornarRelatorioDeEmprestimosPorUsuario() throws Exception {
        // Cenário
        Livro livro = livroRepository.save(new Livro(null, "O Nome do Vento", "Patrick Rothfuss", "Sextante", "978-8575424939", "FISICO", false));
        Usuario usuario = usuarioRepository.save(new Usuario(null, "Kvothe"));
        emprestimoRepository.save(new Emprestimo(null, livro, usuario, LocalDate.now(), null));

        // Ação e Verificação
        mockMvc.perform(get("/emprestimos?usuarioId=" + usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].usuario.nome").value("Kvothe"));
    }

}