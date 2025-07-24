package br.com.biblioteca.biblioteca_api;

import br.com.biblioteca.biblioteca_api.livro.Livro;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveCriarUmLivro_E_RetornarStatus201() throws Exception {
        Livro novoLivro = new Livro(null, "A Guerra dos Tronos", "George R. R. Martin", "Leya", "978-8580411239", "FISICO", true);

        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoLivro)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("A Guerra dos Tronos"));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void naoDeveCriarUmLivro_QuandoUsuarioNaoForAdmin() throws Exception {
        Livro novoLivro = new Livro(null, "A Guerra dos Tronos", "George R. R. Martin", "Leya", "978-8580411239", "FISICO", true);

        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoLivro)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveListarTodosOsLivros_E_RetornarStatus200() throws Exception {
        mockMvc.perform(get("/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].titulo", hasItem("Duna")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveDeletarUmLivro_E_RetornarStatus204() throws Exception {
        Livro livro = new Livro(null, "A Dança dos Dragões", "George R. R. Martin", "Leya", "978-8580411260", "FISICO", true);
        String response = mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livro)))
                .andReturn().getResponse().getContentAsString();
        Livro livroSalvo = objectMapper.readValue(response, Livro.class);

        mockMvc.perform(delete("/livros/" + livroSalvo.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/livros/" + livroSalvo.getId()))
                .andExpect(status().isNotFound());
    }

    // Adicione este método de teste na classe LivroControllerTest...
    @Test
    @WithMockUser(roles = "ADMIN")
    void deveAtualizarUmLivro_E_RetornarStatus200() throws Exception {
        Livro livro = new Livro(null, "O Festim dos Corvos", "George R. R. Martin", "Leya", "978-8580411253", "FISICO", true);
        String response = mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livro)))
                .andReturn().getResponse().getContentAsString();
        Livro livroSalvo = objectMapper.readValue(response, Livro.class);

        Livro livroAtualizado = new Livro(livroSalvo.getId(), "O Festim dos Corvos (Edição Revisada)", "George R. R. Martin", "Suma", "978-8556510278", "FISICO", true);

        mockMvc.perform(put("/livros/" + livroSalvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("O Festim dos Corvos (Edição Revisada)"))
                .andExpect(jsonPath("$.editora").value("Suma"));
    }
}