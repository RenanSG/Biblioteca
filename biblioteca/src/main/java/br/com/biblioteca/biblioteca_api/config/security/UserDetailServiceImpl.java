package br.com.biblioteca.biblioteca_api.config.security;

import br.com.biblioteca.biblioteca_api.usuario.Usuario;
import br.com.biblioteca.biblioteca_api.usuario.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UsuarioRepository repo;

    public UserDetailServiceImpl(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        return new UserSS(usuario.getId(), usuario.getEmail(), usuario.getSenha(), usuario.getRoles());
    }
}