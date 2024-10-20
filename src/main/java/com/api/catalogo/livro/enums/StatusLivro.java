package com.api.catalogo.livro.enums;

public enum StatusLivro {
    ALUGADO(1, "Alugado"),
    DEVOLVIO(2, "Devolvido"),
    DISPONIVEL(3, "Disponivel");

    private int id;
    private String status;

    StatusLivro(int id, String status) {
        this.id = id;
        this.status = status;
    }
}
