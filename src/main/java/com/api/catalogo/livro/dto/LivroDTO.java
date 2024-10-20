package com.api.catalogo.livro.dto;

import com.api.catalogo.livro.enums.StatusLivro;
import lombok.Data;

@Data
public class LivroDTO {

    private String titulo;
    private String autor;
    private StatusLivro status;
}
