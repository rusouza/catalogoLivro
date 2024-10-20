package com.api.catalogo.livro.entity;

import com.api.catalogo.livro.enums.StatusLivro;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Livro {

    private Long id;
    private String titulo;
    private String autor;
    private StatusLivro status;

}
