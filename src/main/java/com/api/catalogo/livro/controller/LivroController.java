package com.api.catalogo.livro.controller;

import com.api.catalogo.livro.dto.LivroDTO;
import com.api.catalogo.livro.dto.LivroNotificacaoDTO;
import com.api.catalogo.livro.entity.Livro;
import com.api.catalogo.livro.enums.StatusLivro;
import com.api.catalogo.livro.service.LivroService;
import com.api.catalogo.livro.messaging.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/catalogo")
public class LivroController {

    @Autowired
    private LivroService service;

    @Autowired
    private NotificationService notificationService;

    @Operation(summary = "Busca todos os livros cadastrados")
    @GetMapping
    public ResponseEntity<Page<Livro>> findAll(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Livro> LivroPage = service.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(LivroPage);
    }

    @Operation(summary = "Busca o livro pelo nome")
    @GetMapping(path = "findByTitulo/{nome}")
    public ResponseEntity<?> findEstudanteByTitulo(@PathVariable String nome){
        return ResponseEntity.status(HttpStatus.OK).body(service.findByTitulo(nome));
    }

    @Operation(summary = "Cadastrar novos livros")
    @PostMapping(path = "incluir/livros")
    public ResponseEntity<Livro> insert(@RequestBody LivroDTO dto) {
        Livro livro = dto.converterParaLivro();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvarOuAtualizar(livro));
    }

    @Operation(summary = "Alugar os livros")
    @PutMapping(path = "alugar/{livroId}")
    public ResponseEntity<?> alugarLivro(@PathVariable(name = "livroId") Long livroId) {

        Livro livro = service.findById(livroId);
        livro.setStatus(StatusLivro.ALUGADO);
        notificationService.sendNotificationALugado("Livro alugado: " + livro.getTitulo());
        return ResponseEntity.ok(service.salvarOuAtualizar(livro));
    }

    @Operation(summary = "Devolver os livros")
    @PutMapping(path = "devolver/{livroId}")
    public ResponseEntity<?> devolverLivro(@PathVariable(name = "livroId") Long livroId) {

        Livro livro = service.findById(livroId);
        if(livro.getStatus().equals(StatusLivro.ALUGADO)) {
            livro.setStatus(StatusLivro.DEVOLVIDO);
            LivroNotificacaoDTO notificacaoDTO = livro.converterParaLivroNotificacao("Livro devolvido: " + livro.getTitulo());
            notificationService.sendNotificationDevolvido(notificacaoDTO);
            return ResponseEntity.ok(service.salvarOuAtualizar(livro));
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Livro escolhido não está alugado");
        }
    }

    @Operation(summary = "Deletar os livros por ID")
    @DeleteMapping(path = "deletar/livros/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Livro Excluído com sucesso!");
    }
}
