package com.api.catalogo.livro.dto;

import com.api.catalogo.livro.entity.Livro;
import com.api.catalogo.livro.enums.StatusLivro;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LivroDTO {

    private String titulo;
    private String autor;
    private String status;

    public Livro converterParaLivro(){
        return new Livro(titulo, autor, StatusLivro.getStatus(status));
    }
}
