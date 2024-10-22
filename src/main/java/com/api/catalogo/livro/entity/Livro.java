package com.api.catalogo.livro.entity;

import com.api.catalogo.livro.dto.LivroNotificacaoDTO;
import com.api.catalogo.livro.dto.UsuarioCadastradoDTO;
import com.api.catalogo.livro.enums.StatusLivro;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Entity
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    private String titulo;
    private String autor;

    @Enumerated(EnumType.STRING)
    private StatusLivro status;

    public Livro(String titulo, String autor, StatusLivro status) {
        this.titulo = titulo;
        this.autor = autor;
        this.status = status;
    }

    public LivroNotificacaoDTO converterParaLivroNotificacao(String mensagem) {
        return new LivroNotificacaoDTO(id, mensagem);
    }
}
