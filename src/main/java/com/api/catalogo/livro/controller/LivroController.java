package com.api.catalogo.livro.controller;

import com.api.catalogo.livro.dto.LivroDTO;
import com.api.catalogo.livro.dto.LivroNotificacaoDTO;
import com.api.catalogo.livro.entity.Livro;
import com.api.catalogo.livro.entity.Usuario;
import com.api.catalogo.livro.enums.StatusLivro;
import com.api.catalogo.livro.messaging.NotificationService;
import com.api.catalogo.livro.service.EmailService;
import com.api.catalogo.livro.service.LivroService;
import com.api.catalogo.livro.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/catalogo")
public class LivroController {

    @Autowired
    private LivroService service;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    @Operation(summary = "Busca todos os livros cadastrados",
            responses = {
                @ApiResponse(responseCode = "200", description = "Livros encontrados",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Livro.class))),
                @ApiResponse(responseCode = "404", description = "Nenhum livro encontrado")
            }
    )
    @GetMapping
    public ResponseEntity<Page<Livro>> findAll(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Livro> LivroPage = service.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(LivroPage);
    }

    @Operation(summary = "Buscar livro pelo nome")
    @GetMapping(path = "findByTitulo/{nome}")
    public ResponseEntity<?> findEstudanteByTitulo(@PathVariable String nome){
        return ResponseEntity.status(HttpStatus.OK).body(service.findByTitulo(nome));
    }

    @Operation(summary = "Cadastrar novos livros",
            responses = {
                @ApiResponse(responseCode = "201", description = "Livro cadastrado com sucesso",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Livro.class)))
            }
    )
    @PostMapping(path = "incluir/livros")
    public ResponseEntity<Livro> insert(@Valid @RequestBody LivroDTO dto) {
        Livro livro = dto.converterParaLivro();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvarOuAtualizar(livro));
    }

    @Operation(summary = "Alugar os livros",
            parameters = @Parameter(name = "livroId", description = "ID do livro a ser alugado", required = true),
            responses = {
                @ApiResponse(responseCode = "200", description = "Livro alugado com sucesso",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Livro.class))),
                @ApiResponse(responseCode = "403", description = "Livro já está alugado"),
                @ApiResponse(responseCode = "404", description = "Livro não encontrado")
            }
    )
    @PutMapping(path = "alugar/{livroId}")
    public ResponseEntity<?> alugarLivro(@PathVariable(name = "livroId") Long livroId) {

        Livro livro = service.findById(livroId);
        String username = getAuthenticatedUsername();

        if(livro.getStatus().equals(StatusLivro.DISPONIVEL)) {
            Usuario usuario = usuarioService.findByLogin(username);

            livro.setStatus(StatusLivro.ALUGADO);
            livro.setUsuario(usuario);
            service.salvarOuAtualizar(livro);
            notificationService.sendNotificationALugado("Livro alugado: " + livro.getTitulo());

            String emailBody = "Você alugou o livro: " + livro.getTitulo();
            emailService.enviarEmail(usuario.getEmail(), "Confirmação de Aluguel", emailBody);

            return ResponseEntity.ok(livro.converterParaLivroAlugado());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Livro escolhido já está alugado");
        }
    }

    @Operation(summary = "Devolver os livros",
            parameters = @Parameter(name = "livroId", description = "ID do livro a ser devolvido", required = true),
            responses = {
                @ApiResponse(responseCode = "200", description = "Livro devolvido com sucesso",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Livro.class))),
                @ApiResponse(responseCode = "403", description = "Não é possível devolver o livro escolhido"),
                @ApiResponse(responseCode = "404", description = "Livro não encontrado")
            }
    )
    @PutMapping(path = "devolver/{livroId}")
    public ResponseEntity<?> devolverLivro(@PathVariable(name = "livroId") Long livroId) {

        Livro livro = service.findById(livroId);
        String username = getAuthenticatedUsername();

        if(livro.getUsuario().getLogin().equals(username)
                && livro.getStatus().equals(StatusLivro.ALUGADO)) {
            livro.setStatus(StatusLivro.DEVOLVIDO);
            livro.setUsuario(null);
            service.salvarOuAtualizar(livro);
            LivroNotificacaoDTO notificacaoDTO = livro.converterParaLivroNotificacao("Livro devolvido: " + livro.getTitulo());
            notificationService.sendNotificationDevolvido(notificacaoDTO);

            return ResponseEntity.ok(livro.converterParaLivroAlugado());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Não é possivel devolver o livro escolhido");
        }
    }

    @Operation(summary = "Deletar os livros por ID",
            parameters = @Parameter(name = "id", description = "ID do livro a ser excluído", required = true),
            responses = {
                @ApiResponse(responseCode = "200", description = "Livro excluído com sucesso"),
                @ApiResponse(responseCode = "404", description = "Livro não encontrado")
            }
    )
    @DeleteMapping(path = "deletar/livros/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Livro Excluído com sucesso!");
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
