package com.api.catalogo.livro.repository;

import com.api.catalogo.livro.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByTituloIgnoreCaseContaining(String nome);
}