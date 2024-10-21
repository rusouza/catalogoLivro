package com.api.catalogo.livro.controller;

import com.api.catalogo.livro.dto.UsuarioCadastradoDTO;
import com.api.catalogo.livro.dto.UsuarioDTO;
import com.api.catalogo.livro.entity.Livro;
import com.api.catalogo.livro.entity.Usuario;
import com.api.catalogo.livro.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/user")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Operation(summary = "Busca todos os usuários cadastrados")
    @GetMapping
    public ResponseEntity<List<UsuarioCadastradoDTO>> findAll() {
        List<Usuario> users = service.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(UsuarioCadastradoDTO.getAllUser(users));
    }

    @PostMapping(path = "cadastro")
    public ResponseEntity<UsuarioCadastradoDTO> insert(@RequestBody UsuarioDTO userDto) {
        Usuario user = userDto.converterParaUsuario();
        Usuario usuario = service.insert(user);
        UsuarioCadastradoDTO dto = usuario.converterParaUsuarioCadastrado();
        return ResponseEntity.ok(dto);
    }
}
