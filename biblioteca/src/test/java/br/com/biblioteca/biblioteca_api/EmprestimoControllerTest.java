package br.com.biblioteca.biblioteca_api;

import br.com.biblioteca.biblioteca_api.dto.CriarEmprestimoDTO;
import br.com.biblioteca.biblioteca_api.model.Livro;
import br.com.biblioteca.biblioteca_api.model.Usuario;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    void deveCriarUmEmprestimo_E_RetornarStatus201() throws Exception {
        // Cenário
        Livro livro = livroRepository.save(new Livro(null, "Neuromancer", "William Gibson", "Aleph", "978-8576572937", "FISICO", true));
        Usuario usuario = usuarioRepository.save(new Usuario(null, "Case"));
        CriarEmprestimoDTO dto = new CriarEmprestimoDTO(livro.getId(), usuario.getId());

        // Ação e Verificação
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
        // Cenário
        Livro livro = livroRepository.save(new Livro(null, "Snow Crash", "Neal Stephenson", "Editora B", "978-0553380958", "FISICO", false));
        Usuario usuario = usuarioRepository.save(new Usuario(null, "Hiro Protagonist"));
        CriarEmprestimoDTO dto = new CriarEmprestimoDTO(livro.getId(), usuario.getId());

        // Ação e Verificação
        mockMvc.perform(post("/emprestimos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}