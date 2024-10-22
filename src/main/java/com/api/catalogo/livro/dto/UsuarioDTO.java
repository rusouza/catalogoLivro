package com.api.catalogo.livro.dto;

import com.api.catalogo.livro.entity.Usuario;
import lombok.Data;

@Data
public class UsuarioDTO {

    private String login;
    private String senha;
    private String email;

    public Usuario converterParaUsuario() {
        return new Usuario(login, senha, email);
    }
}
