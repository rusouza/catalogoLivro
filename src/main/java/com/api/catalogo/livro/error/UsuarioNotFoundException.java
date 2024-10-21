package com.api.catalogo.livro.error;

public class UsuarioNotFoundException extends RuntimeException {

    public UsuarioNotFoundException(String nome) {
        super("Usuário " + nome + " não encontrado.");
    }
}
