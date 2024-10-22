package com.api.catalogo.livro.service;

import com.api.catalogo.livro.entity.Usuario;
import com.api.catalogo.livro.error.UsuarioNotFoundException;
import com.api.catalogo.livro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private UsuarioRepository repository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.repository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> findAll() {
        return repository.findAll();
    }

    private void isLoginExist(Usuario usuario) {
        Optional<Usuario> user = repository.findByLoginIgnoreCaseContaining(usuario.getLogin());
        if(user.isPresent() && user.get().getLogin().equals(usuario.getLogin()))
            throw new DataIntegrityViolationException("Nome de login já utilizado!");
    }

    public Usuario insert(Usuario usuario) {
        isLoginExist(usuario);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return repository.save(usuario);
    }

    public Usuario findByLogin(String login) {
        Optional<Usuario> opUsuario = repository.findByLogin(login);
        return opUsuario.orElseThrow(() -> new UsuarioNotFoundException(login));
    }

}
