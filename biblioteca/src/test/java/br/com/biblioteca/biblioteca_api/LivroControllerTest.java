package br.com.biblioteca.biblioteca_api;

import br.com.biblioteca.biblioteca_api.livro.Livro;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
    void deveListarTodosOsLivros_E_RetornarStatus200() throws Exception {
        // O DataSeedingLoader já popula o banco, então esperamos encontrar "O Hobbit"
        mockMvc.perform(get("/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].titulo", hasItem("O Hobbit")));
    }

    @Test
    void deveBuscarLivroPorTitulo_E_RetornarStatus200() throws Exception {
        mockMvc.perform(get("/livros?titulo=Duna"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].titulo").value("Duna"));
    }

    // Adicione estes métodos de teste à classe LivroControllerTest
    @Test
    void deveBuscarLivroPorAutor_E_RetornarStatus200() throws Exception {
        mockMvc.perform(get("/livros?autor=Isaac Asimov"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].autor").value("Isaac Asimov"));
    }

    @Test
    void deveBuscarLivroPorIsbn_E_RetornarStatus200() throws Exception {
        mockMvc.perform(get("/livros?isbn=978-8576570988"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].isbn").value("978-8576570988"));
    }

    @Test
    void deveRetornar404_AoBuscarLivroComIdInexistente() throws Exception {
        mockMvc.perform(get("/livros/999"))
                .andExpect(status().isNotFound());
    }

    @Test
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
}