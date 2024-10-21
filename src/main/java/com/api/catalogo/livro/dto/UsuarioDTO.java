package com.api.catalogo.livro.dto;

import com.api.catalogo.livro.entity.Usuario;
import lombok.Data;

@Data
public class UsuarioDTO {

    private String login;
    private String senha;

    public Usuario converterParaUsuario() {
        return new Usuario(login, senha);
    }
}
