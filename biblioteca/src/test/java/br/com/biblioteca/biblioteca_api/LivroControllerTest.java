package br.com.biblioteca.biblioteca_api;

import br.com.biblioteca.biblioteca_api.autor.Autor;
import br.com.biblioteca.biblioteca_api.autor.AutorRepository;
import br.com.biblioteca.biblioteca_api.livro.Livro;
import br.com.biblioteca.biblioteca_api.livro.LivroDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private AutorRepository autorRepository; // Adicione o repositório

    private Autor autorSalvo;

    @BeforeEach
    void setUp() {
        // Cria um autor que será usado em todos os testes
        autorSalvo = autorRepository.save(new Autor(null, "George R. R. Martin"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveCriarUmLivro_E_RetornarStatus201() throws Exception {
        // Use o DTO para a requisição
        LivroDTO novoLivroDto = new LivroDTO("A Guerra dos Tronos", autorSalvo.getId(), "Leya", "978-8580411239", "FISICO", true);

        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoLivroDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("A Guerra dos Tronos"))
                .andExpect(jsonPath("$.autor.nome").value("George R. R. Martin"));
    }

    // ... os outros testes (naoDeveCriarUmLivro, deveListarTodosOsLivros) permanecem funcionais ...

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveDeletarUmLivro_E_RetornarStatus204() throws Exception {
        LivroDTO livroDto = new LivroDTO("A Dança dos Dragões", autorSalvo.getId(), "Leya", "978-8580411260", "FISICO", true);
        String response = mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroDto)))
                .andReturn().getResponse().getContentAsString();
        Livro livroSalvo = objectMapper.readValue(response, Livro.class);

        mockMvc.perform(delete("/livros/" + livroSalvo.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/livros/" + livroSalvo.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveAtualizarUmLivro_E_RetornarStatus200() throws Exception {
        LivroDTO livroDto = new LivroDTO("O Festim dos Corvos", autorSalvo.getId(), "Leya", "978-8580411253", "FISICO", true);
        String response = mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroDto)))
                .andReturn().getResponse().getContentAsString();
        Livro livroSalvo = objectMapper.readValue(response, Livro.class);

        Autor outraEditora = autorRepository.save(new Autor(null, "Suma"));
        LivroDTO livroAtualizadoDto = new LivroDTO("O Festim dos Corvos (Edição Revisada)", autorSalvo.getId(), "Suma", "978-8556510278", "FISICO", true);

        mockMvc.perform(put("/livros/" + livroSalvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroAtualizadoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("O Festim dos Corvos (Edição Revisada)"))
                .andExpect(jsonPath("$.editora").value("Suma"));
    }
}