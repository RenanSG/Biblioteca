// Arquivo: src/test/java/br/com/biblioteca/biblioteca_api/autor/AutorControllerTest.java

package br.com.biblioteca.biblioteca_api.autor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AutorRepository autorRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveCriarUmAutor_E_RetornarStatus201() throws Exception {
        Autor novoAutor = new Autor(null, "J.K. Rowling");

        mockMvc.perform(post("/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoAutor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("J.K. Rowling"));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void naoDeveCriarAutor_QuandoUsuarioNaoForAdmin() throws Exception {
        Autor novoAutor = new Autor(null, "J.R.R. Tolkien");

        mockMvc.perform(post("/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoAutor)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveListarAutores_E_RetornarStatus200() throws Exception {
        autorRepository.save(new Autor(null, "Clarice Lispector"));

        mockMvc.perform(get("/autores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(5))); // 4 do seeder + 1 criado aqui
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveAtualizarAutor_E_RetornarStatus200() throws Exception {
        Autor autorSalvo = autorRepository.save(new Autor(null, "Machado de Assis"));
        Autor autorAtualizado = new Autor(autorSalvo.getId(), "Machado de Assis (Revisado)");

        mockMvc.perform(put("/autores/" + autorSalvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(autorAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Machado de Assis (Revisado)"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveDeletarAutor_E_RetornarStatus204() throws Exception {
        Autor autorSalvo = autorRepository.save(new Autor(null, "Graciliano Ramos"));

        mockMvc.perform(delete("/autores/" + autorSalvo.getId()))
                .andExpect(status().isNoContent());
    }
}