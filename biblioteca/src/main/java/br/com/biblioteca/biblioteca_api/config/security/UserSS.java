package br.com.biblioteca.biblioteca_api.config.security;

import br.com.biblioteca.biblioteca_api.usuario.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class UserSS implements UserDetails {

    private Long id;
    private String email;
    private String senha;
    private Collection<? extends GrantedAuthority> authorities;

    public UserSS(Long id, String email, String senha, Set<Roles> roles) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getDescricao()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean hasRole(Roles role) {
        return getAuthorities().contains(new SimpleGrantedAuthority(role.getDescricao()));
    }
}
