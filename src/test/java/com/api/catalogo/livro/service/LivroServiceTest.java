package com.api.catalogo.livro.service;

import com.api.catalogo.livro.entity.Livro;
import com.api.catalogo.livro.entity.Usuario;
import com.api.catalogo.livro.enums.StatusLivro;
import com.api.catalogo.livro.error.LivroNotFoundException;
import com.api.catalogo.livro.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LivroServiceTest {

    @Mock
    private LivroRepository repository;

    @InjectMocks
    private LivroService service;

    public static final Long ID = 1L;
    public static final String TITULO = "Neuromancer";
    public static final String AUTOR = "William Gibson";
    public static final String STATUS_LIVRO = "Disponivel";
    public static final Long ID_USER = 1L;
    public static final String LOGIN = "admin";
    public static final String SENHA = "senhaCodificada";
    public static final String EMAIL = "admin@teste.com";

    private Optional<Livro> optionalLivro;
    private Livro livro;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startLivro();
    }

    @Test
    void testFindAll() {
        Pageable pageable = mock(Pageable.class);
        Page<Livro> expectedPage = new PageImpl<>(Arrays.asList(new Livro()));

        when(repository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Livro> result = service.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void testFindByIdSuccess() {
        when(repository.findById(anyLong())).thenReturn(optionalLivro);

        Livro result = service.findById(ID);

        assertNotNull(result);
        assertEquals(ID, result.getId());
        assertEquals(TITULO, result.getTitulo());
        assertEquals(AUTOR, result.getAutor());
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void testFindByIdNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(LivroNotFoundException.class, () -> service.findById(ID));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void testFindByTituloSuccess() {
        when(repository.findByTituloIgnoreCaseContaining(TITULO)).thenReturn(Collections.singletonList(livro));

        List<Livro> result = service.findByTitulo(TITULO);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ID, result.get(0).getId());
        assertEquals(TITULO, result.get(0).getTitulo());
        assertEquals(AUTOR, result.get(0).getAutor());
        verify(repository, times(1)).findByTituloIgnoreCaseContaining(TITULO);
    }

    @Test
    void testFindByTituloNotFound() {
        when(repository.findByTituloIgnoreCaseContaining(anyString())).thenReturn(null);

        assertThrows(LivroNotFoundException.class, () -> service.findByTitulo(TITULO));
        verify(repository, times(1)).findByTituloIgnoreCaseContaining(TITULO);
    }

    @Test
    void testSalvarOuAtualizar() {
        when(repository.save(any())).thenReturn(livro);

        Livro result = service.salvarOuAtualizar(livro);

        assertNotNull(result);
        assertEquals(ID, result.getId());
        assertEquals(TITULO, result.getTitulo());
        assertEquals(AUTOR, result.getAutor());
        verify(repository, times(1)).save(livro);
    }

    @Test
    void testDelete() {
        when(repository.findById(anyLong())).thenReturn(optionalLivro);
        doNothing().when(repository).delete(livro);

        service.delete(ID);

        verify(repository, times(1)).findById(ID);
        verify(repository, times(1)).delete(livro);
    }

    private void startLivro(){
        usuario = new Usuario(ID_USER, LOGIN, SENHA, EMAIL);
        livro = new Livro(ID, TITULO, AUTOR, StatusLivro.getStatus(STATUS_LIVRO), usuario);
        optionalLivro = Optional.of(new Livro(ID, TITULO, AUTOR, StatusLivro.getStatus(STATUS_LIVRO), usuario));
    }
}
