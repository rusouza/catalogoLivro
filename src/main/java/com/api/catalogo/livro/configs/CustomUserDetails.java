package com.api.catalogo.livro.configs;

import com.api.catalogo.livro.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private String login;
    private String senha;

    public CustomUserDetails(Usuario usuario) {
        this.login = usuario.getLogin();
        this.senha = usuario.getSenha();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Personalize conforme sua lógica
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Personalize conforme sua lógica
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Personalize conforme sua lógica
    }

    @Override
    public boolean isEnabled() {
        return true; // Personalize conforme sua lógica
    }
}
