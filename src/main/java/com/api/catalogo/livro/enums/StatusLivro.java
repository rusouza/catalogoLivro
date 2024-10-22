package com.api.catalogo.livro.enums;

public enum StatusLivro {
    ALUGADO(1, "Alugado"),
    DEVOLVIDO(2, "Devolvido"),
    DISPONIVEL(3, "Disponivel");

    private int id;
    private String status;

    StatusLivro(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public static StatusLivro getStatus(String status) {
        for (StatusLivro s : StatusLivro.values()) {
            if (s.status.equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Status inválido: " + status);
    }
}
