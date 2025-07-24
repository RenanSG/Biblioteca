package br.com.biblioteca.biblioteca_api.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String senha;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ROLES")
    private Set<Integer> roles = new HashSet<>();

    public Set<Roles> getRoles() {
        return roles.stream().map(Roles::toEnum).collect(Collectors.toSet());
    }

    public void addRole(Roles role) {
        roles.add(role.getCod());
    }

    public Usuario(Long id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
}