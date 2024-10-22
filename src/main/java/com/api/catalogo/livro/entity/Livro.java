package com.api.catalogo.livro.entity;

import com.api.catalogo.livro.dto.LivroAlugadoDTO;
import com.api.catalogo.livro.dto.LivroNotificacaoDTO;
import com.api.catalogo.livro.enums.StatusLivro;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Livro(String titulo, String autor, StatusLivro status) {
        this.titulo = titulo;
        this.autor = autor;
        this.status = status;
    }

    public LivroNotificacaoDTO converterParaLivroNotificacao(String mensagem) {
        return new LivroNotificacaoDTO(id, mensagem);
    }

    public LivroAlugadoDTO converterParaLivroAlugado() {
        return new LivroAlugadoDTO(titulo, autor, status.name(), usuario.getLogin());
    }
}
