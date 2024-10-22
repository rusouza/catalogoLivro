package com.api.catalogo.livro.messaging;

import com.api.catalogo.livro.dto.LivroNotificacaoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final String TOPIC_ALUGADO = "livro-alugado";
    private static final String TOPIC_DEVOLVIDO = "livro-devolvido";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendNotificationALugado(String message) {
        kafkaTemplate.send(TOPIC_ALUGADO, message);
    }

    public void sendNotificationDevolvido(LivroNotificacaoDTO dto) {
        try {
            String json = new ObjectMapper().writeValueAsString(dto);
            kafkaTemplate.send(TOPIC_DEVOLVIDO, json);
        } catch (JsonProcessingException e) {
            System.out.println("Não foi possivel enviar a notificação!");
        }
    }
}
