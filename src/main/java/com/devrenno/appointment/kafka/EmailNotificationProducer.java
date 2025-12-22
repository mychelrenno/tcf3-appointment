package com.devrenno.appointment.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationProducer {

    private final Logger log = LoggerFactory.getLogger(EmailNotificationProducer.class);
    private static final String TOPIC_NAME = "agendamentos";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void send(AgendamentoMessage message) {
        kafkaTemplate.send(TOPIC_NAME, message.getEmailPaciente(), message);
        log.info("Mensagem de agendamento enviada para o tópico {}: {}", TOPIC_NAME, message);
    }
}
