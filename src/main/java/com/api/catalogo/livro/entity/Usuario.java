package com.api.catalogo.livro.entity;

import com.api.catalogo.livro.dto.UsuarioCadastradoDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;
    private String senha;

    @Column(unique = true)
    private String email;

    public Usuario(String login, String senha, String email) {
        this.login = login;
        this.senha = senha;
        this.email = email;
    }

    public UsuarioCadastradoDTO converterParaUsuarioCadastrado() {
        return new UsuarioCadastradoDTO(login, email);
    }
}
