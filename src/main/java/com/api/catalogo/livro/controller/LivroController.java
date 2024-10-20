package com.api.catalogo.livro.controller;

import com.api.catalogo.livro.entity.Livro;
import com.api.catalogo.livro.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/catalogo")
public class LivroController {

    @Autowired
    private LivroService service;

    @Operation(summary = "Busca todos os livros cadastrados")
    @GetMapping(path = "livros")
    public ResponseEntity<List<Livro>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
    }
}
