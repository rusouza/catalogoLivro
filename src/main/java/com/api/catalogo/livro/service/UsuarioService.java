package com.api.catalogo.livro.service;

import com.api.catalogo.livro.entity.Usuario;
import com.api.catalogo.livro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private UsuarioRepository repository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.repository = usuarioRepository;
    }

    private void isLoginExist(Usuario usuario) {
        Optional<Usuario> user = repository.findByLoginIgnoreCaseContaining(usuario.getUser());
        if(user.isPresent() && user.get().getUser().equals(usuario.getUser()))
            throw new DataIntegrityViolationException("Nome de login já utilizado!");
    }

    public Usuario insert(Usuario usuario){
        isLoginExist(usuario);
        return repository.save(usuario);
    }
}
