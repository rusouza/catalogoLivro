package com.api.catalogo.livro.dto;

import com.api.catalogo.livro.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioCadastradoDTO {

    private String login;
    private String email;

    public static List<UsuarioCadastradoDTO> getAllUser(List<Usuario> list) {
        List<UsuarioCadastradoDTO> listDto = new ArrayList<>();
         for(Usuario u : list) {
             UsuarioCadastradoDTO dto = new UsuarioCadastradoDTO();
             dto.setLogin(u.getLogin());
             dto.setEmail(u.getEmail());
             listDto.add(dto);
         }
         return listDto;
    }

}
