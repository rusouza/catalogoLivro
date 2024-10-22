package com.api.catalogo.livro.service;

import com.api.catalogo.livro.entity.Livro;
import com.api.catalogo.livro.error.LivroNotFoundException;
import com.api.catalogo.livro.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    private LivroRepository repository;

    @Autowired
    public LivroService(LivroRepository repository){
        this.repository = repository;
    }

    public Page<Livro> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Livro findById(Long id) {
        Optional<Livro> livro = repository.findById(id);
        return livro.orElseThrow(() -> new LivroNotFoundException(id));
    }

    public List<Livro> findByTitulo(String titulo) {
        return Optional.ofNullable(repository.findByTituloIgnoreCaseContaining(titulo))
                .filter(livros -> !livros.isEmpty())
                .orElseThrow(() -> new LivroNotFoundException(titulo));
    }

    public Livro salvarOuAtualizar(Livro livro){
        return repository.save(livro);
    }

    public void delete(Long id) {
        Livro livro = findById(id);
        repository.delete(livro);
    }
}
