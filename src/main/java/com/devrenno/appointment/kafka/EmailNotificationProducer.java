package com.devrenno.appointment.kafka;

import com.devrenno.appointment.dto.EmailNotificationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationProducer {

    private final Logger log = LoggerFactory.getLogger(EmailNotificationProducer.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void send(EmailNotificationDto dto) {
        kafkaTemplate.send("email-notification-topic", dto);
        log.info("Email notification sent: {}", dto);
    }
}
