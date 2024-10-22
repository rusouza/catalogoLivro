package com.api.catalogo.livro.security;

import com.api.catalogo.livro.configs.CustomUserDetails;
import com.api.catalogo.livro.entity.Usuario;
import com.api.catalogo.livro.error.UsuarioNotFoundException;
import com.api.catalogo.livro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository repository;

    @Autowired
    public CustomUserDetailsService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Usuario> opUsuario = repository.findByLogin(login);
        if(opUsuario.isPresent()){
            return new CustomUserDetails(opUsuario.get());
        } else {
            throw new UsuarioNotFoundException(login);
        }
    }
}
