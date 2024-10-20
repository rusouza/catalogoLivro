package com.api.catalogo.livro.service;

import com.api.catalogo.livro.entity.Livro;
import com.api.catalogo.livro.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Livro> findAll() {
        return repository.findAll();
    }

    public List<Livro> findByTitulo(String titulo) throws Exception {
        List<Livro> livros = repository.findByTituloIgnoreCaseContaining(titulo);
        if(!livros.isEmpty())
            return livros;
        throw new Exception("Nenhum Livro com esse nome foi encontrado!");
    }

    public Livro insert(Livro livro){
        return repository.save(livro);
    }

    public void delete(Long id) throws Exception {
        Optional<Livro> livro = repository.findById(id);
        if(livro.isPresent())
            repository.delete(livro.get());
        else
            throw new Exception();
    }
}
