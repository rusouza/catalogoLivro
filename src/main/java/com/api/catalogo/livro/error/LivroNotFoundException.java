package com.api.catalogo.livro.error;

public class LivroNotFoundException extends RuntimeException{

    public LivroNotFoundException(Long id) {
        super("Livro com ID " + id + " não encontrado.");
    }

    public LivroNotFoundException(String nome) {
        super("Livro " + nome + " não encontrado.");
    }
}
