package com.api.catalogo.livro.service;

import com.api.catalogo.livro.entity.Usuario;
import com.api.catalogo.livro.error.UsuarioNotFoundException;
import com.api.catalogo.livro.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UsuarioServiceTest {

    public static final Long ID = 1L;
    public static final String LOGIN = "admin";
    public static final String SENHA = "senhaCodificada";
    public static final String EMAIL = "admin@teste.com";

    @InjectMocks
    private UsuarioService service;

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Usuario user;
    private Optional<Usuario> optionalUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startUser();
    }

    @Test
    void testInsertSucess() {
        when(repository.findByLoginIgnoreCaseContaining(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn(SENHA);
        when(repository.save(any())).thenReturn(user);

        Usuario result = service.insert(user);

        assertNotNull(result);
        assertEquals(ID, result.getId());
        assertEquals(LOGIN, result.getLogin());
        assertEquals(SENHA, result.getSenha());
        assertEquals(EMAIL, result.getEmail());
    }

    @Test
    void testInsertLoginExists() {
        when(repository.findByLoginIgnoreCaseContaining(anyString())).thenReturn(optionalUser);

        assertThrows(DataIntegrityViolationException.class, () -> {
            service.insert(user);
        });
    }

    @Test
    void testFindAllSuccess() {
        when(repository.findAll()).thenReturn(Arrays.asList(user));

        List<Usuario> result = service.findAll();

        assertNotNull(result);
        assertEquals(ID, result.get(0).getId());
        assertEquals(LOGIN, result.get(0).getLogin());
        assertEquals(SENHA, result.get(0).getSenha());
        assertEquals(EMAIL, result.get(0).getEmail());
    }

    @Test
    void testFindAllReturnListEmpty() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<Usuario> result = service.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByLoginSuccess() {
        when(repository.findByLogin(anyString())).thenReturn(optionalUser);

        Usuario result = service.findByLogin(LOGIN);

        assertNotNull(result);
        assertEquals(ID, result.getId());
        assertEquals(LOGIN, result.getLogin());
        assertEquals(SENHA, result.getSenha());
        assertEquals(EMAIL, result.getEmail());
    }

    @Test
    void testFindByLoginNotFound() {
        when(repository.findByLogin(anyString())).thenReturn(Optional.empty());

        assertThrows(UsuarioNotFoundException.class, () -> {
            service.findByLogin(LOGIN);
        });
    }

    private void startUser(){
        user = new Usuario(ID, LOGIN, SENHA, EMAIL);
        optionalUser = Optional.of(new Usuario(ID, LOGIN, SENHA, EMAIL));
    }

}
