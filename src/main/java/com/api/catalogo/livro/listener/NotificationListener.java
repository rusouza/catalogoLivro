package com.api.catalogo.livro.listener;

import com.api.catalogo.livro.dto.LivroNotificacaoDTO;
import com.api.catalogo.livro.entity.Livro;
import com.api.catalogo.livro.enums.StatusLivro;
import com.api.catalogo.livro.service.LivroService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    @Autowired
    private LivroService service;

    @KafkaListener(topics = "livro-alugado", groupId = "livro-group")
    public void listenAlugado(String message) {
        System.out.println("Recebido: " + message);
    }

    @KafkaListener(topics = "livro-devolvido", groupId = "livro-group")
    public void listenDevolvido(ConsumerRecord<String, String> record) throws JsonProcessingException {
        String message = record.value();
        LivroNotificacaoDTO evento = new ObjectMapper().readValue(message, LivroNotificacaoDTO.class);

        Livro livro = service.findById(evento.getId());
        livro.setStatus(StatusLivro.DISPONIVEL);
        service.salvarOuAtualizar(livro);
        System.out.println("Devolvido: " + evento.getMensagem());
    }

}
