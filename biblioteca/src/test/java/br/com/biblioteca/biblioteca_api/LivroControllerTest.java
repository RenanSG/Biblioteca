// Arquivo: src/test/java/br/com/biblioteca/biblioteca_api/livro/LivroControllerTest.java

package br.com.biblioteca.biblioteca_api.livro;

import br.com.biblioteca.biblioteca_api.autor.Autor;
import br.com.biblioteca.biblioteca_api.autor.AutorRepository;
import br.com.biblioteca.biblioteca_api.categoria.Categoria;
import br.com.biblioteca.biblioteca_api.categoria.CategoriaRepository;
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
    private AutorRepository autorRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Autor autorSalvo;
    private Categoria categoriaSalva;

    @BeforeEach
    void setUp() {
        autorSalvo = autorRepository.save(new Autor(null, "George R. R. Martin", null, null));
        categoriaSalva = categoriaRepository.save(new Categoria(null, "Fantasia Épica", null));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveCriarUmLivro_E_RetornarStatus201() throws Exception {
        // CORREÇÃO: Usando o construtor completo e correto do DTO.
        LivroDTO novoLivroDto = new LivroDTO("A Guerra dos Tronos", autorSalvo.getId(), categoriaSalva.getId(), "Leya", "978-8580411239", 1996, 600, TipoLivro.FISICO, true);

        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoLivroDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("A Guerra dos Tronos"))
                .andExpect(jsonPath("$.categoria.nome").value("Fantasia Épica"));
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void naoDeveCriarUmLivro_QuandoUsuarioNaoForAdmin() throws Exception {
        // CORREÇÃO: Usando o construtor completo e correto do DTO.
        LivroDTO novoLivroDto = new LivroDTO("A Guerra dos Tronos", autorSalvo.getId(), categoriaSalva.getId(), "Leya", "978-8580411239", 1996, 600, TipoLivro.FISICO, true);

        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoLivroDto)))
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
        // CORREÇÃO: Usando o construtor completo e correto do DTO para criar o livro a ser deletado.
        LivroDTO livroDto = new LivroDTO("A Dança dos Dragões", autorSalvo.getId(), categoriaSalva.getId(), "Leya", "978-8580411260", 2011, 864, TipoLivro.FISICO, true);
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
        // CORREÇÃO: Criando o livro inicial e o DTO de atualização com o construtor correto.
        LivroDTO livroDto = new LivroDTO("O Festim dos Corvos", autorSalvo.getId(), categoriaSalva.getId(), "Leya", "978-8580411253", 2005, 656, TipoLivro.FISICO, true);
        String response = mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroDto)))
                .andReturn().getResponse().getContentAsString();
        Livro livroSalvo = objectMapper.readValue(response, Livro.class);

        LivroDTO livroAtualizadoDto = new LivroDTO("O Festim dos Corvos (Edição Revisada)", autorSalvo.getId(), categoriaSalva.getId(), "Suma", "978-8556510278", 2019, 700, TipoLivro.FISICO, true);

        mockMvc.perform(put("/livros/" + livroSalvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroAtualizadoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("O Festim dos Corvos (Edição Revisada)"))
                .andExpect(jsonPath("$.editora").value("Suma"));
    }
}