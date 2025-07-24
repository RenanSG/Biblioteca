package br.com.biblioteca.biblioteca_api.categoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveCriarCategoria_E_RetornarStatus201() throws Exception {
        Categoria novaCategoria = new Categoria(null, "Terror", "Obras destinadas a causar medo e suspense.");

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaCategoria)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Terror"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveDeletarCategoria_E_RetornarStatus204() throws Exception {
        Categoria categoriaSalva = categoriaRepository.save(new Categoria(null, "Aventura", "Obras com jornadas e perigos."));

        mockMvc.perform(delete("/categorias/" + categoriaSalva.getId()))
                .andExpect(status().isNoContent());
    }
}