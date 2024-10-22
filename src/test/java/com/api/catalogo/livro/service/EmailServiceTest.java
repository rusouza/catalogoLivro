package com.api.catalogo.livro.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.verify;

@SpringBootTest
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService service;

    public static final String TO = "teste@mail.com";
    public static final String SUBJECT = "assunto teste";
    public static final String MENSAGEM = "Texto do email";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEnviarEmail() {
        service.enviarEmail(TO, SUBJECT, MENSAGEM);

        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setTo(TO);
        expectedMessage.setSubject(SUBJECT);
        expectedMessage.setText(MENSAGEM);

        verify(mailSender).send(expectedMessage);
    }
}
