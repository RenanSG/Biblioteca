// Arquivo: src/main/java/br/com/biblioteca/biblioteca_api/autor/Autor.java

package br.com.biblioteca.biblioteca_api.autor;

import jakarta.persistence.*; // Garanta esta importação
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate; // Importe LocalDate

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    // Adicione os novos campos
    private LocalDate dataNascimento;

    @Column(length = 1000) // Permite uma biografia um pouco maior
    private String biografia;
}