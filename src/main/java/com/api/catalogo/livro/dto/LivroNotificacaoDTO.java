package com.api.catalogo.livro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivroNotificacaoDTO {

    private Long id;
    private String mensagem;
}
