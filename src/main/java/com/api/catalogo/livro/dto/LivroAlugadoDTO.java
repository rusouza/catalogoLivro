package com.api.catalogo.livro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LivroAlugadoDTO {

    private String titulo;
    private String autor;
    private String status;
    private String userAluguel;

}
