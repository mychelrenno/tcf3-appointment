package com.devrenno.appointment.service;

import com.devrenno.appointment.dto.AppointmentDto;
import com.devrenno.appointment.dto.EmailNotificationDto;
import com.devrenno.appointment.kafka.EmailNotificationProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {

    @Autowired
    EmailNotificationProducer emailNotificationProducer;

    public void send(AppointmentDto dto) {
        emailNotificationProducer.send(createEmailNotification(dto));
    }

    private EmailNotificationDto createEmailNotification(AppointmentDto dto) {
        StringBuilder body = new StringBuilder();
        body.append("<html>");
        body.append("<body>");
        body.append("<p>Olá, sua consulta com o Doutor ");
        body.append(dto.getDoctor().getName());
        body.append(" foi agendada para ");
        body.append(dto.getDateTime());
        body.append(" no endereco ");
        body.append(dto.getAddress().getStreet());
        body.append(" ");
        body.append(dto.getAddress().getNumber());
        body.append("</p>");
        body.append("</body>");
        body.append("</html>");

        EmailNotificationDto _dto = new EmailNotificationDto(
                dto.getPatient().getEmail(),
                "Consulta Agendada",
                body.toString());

        return _dto;
    }
}
