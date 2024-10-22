package com.api.catalogo.livro.controller;

import com.api.catalogo.livro.security.JwtTokenProvider;
import com.api.catalogo.livro.dto.JwtResponseDTO;
import com.api.catalogo.livro.dto.UsuarioCadastradoDTO;
import com.api.catalogo.livro.dto.UsuarioDTO;
import com.api.catalogo.livro.entity.Usuario;
import com.api.catalogo.livro.error.UsuarioNotFoundException;
import com.api.catalogo.livro.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/user")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "Busca todos os usuários cadastrados",
            responses = {
                @ApiResponse(responseCode = "200", description = "Usuários encontrados",
                        content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = UsuarioCadastradoDTO.class))),
                @ApiResponse(responseCode = "404", description = "Nenhum usuários encontrados")
            }
    )
    @GetMapping
    public ResponseEntity<List<UsuarioCadastradoDTO>> findAll() {
        List<Usuario> users = service.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(UsuarioCadastradoDTO.getAllUser(users));
    }

    @Operation(summary = "Cadastar os usuários",
            requestBody = @RequestBody(description = "Dados do usuário a ser cadastrado",
                required = true,
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UsuarioDTO.class))),
            responses = {
                @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioCadastradoDTO.class)))
            }
    )
    @PostMapping(path = "cadastro")
    public ResponseEntity<UsuarioCadastradoDTO> insert(@Valid @RequestBody UsuarioDTO userDto) {
        Usuario user = userDto.converterParaUsuario();
        Usuario usuario = service.insert(user);
        UsuarioCadastradoDTO dto = usuario.converterParaUsuarioCadastrado();
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Conectar na API",
            requestBody = @RequestBody(description = "Credenciais do usuário para autenticação",
                required = true,
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UsuarioDTO.class))),
            responses = {
                @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JwtResponseDTO.class))),
                @ApiResponse(responseCode = "404", description = "Usuário inválido ou senha incorreta")
            }
    )
    @PostMapping("login")
    public JwtResponseDTO AuthenticateAndGetToken(@Valid @RequestBody UsuarioDTO dto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getSenha()));
        if(authentication.isAuthenticated()){
            return JwtResponseDTO.builder()
                    .accessToken(jwtTokenProvider.generateToken(dto.getLogin())).build();
        } else {
            throw new UsuarioNotFoundException("Usuário inválido ou senha incorreta");
        }
    }
}
