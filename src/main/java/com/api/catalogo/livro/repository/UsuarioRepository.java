package com.api.catalogo.livro.repository;

import com.api.catalogo.livro.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByLogin(String nome);
    Optional<Usuario> findByLoginIgnoreCaseContaining(String nome);
}
