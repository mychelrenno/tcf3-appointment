package com.devrenno.appointment.service;

import com.devrenno.appointment.dto.AppointmentDto;
import com.devrenno.appointment.kafka.AgendamentoMessage;
import com.devrenno.appointment.kafka.EmailNotificationProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailNotificationService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("ddMMyyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Autowired
    EmailNotificationProducer emailNotificationProducer;

    public void send(AppointmentDto dto) {
        AgendamentoMessage message = createAgendamentoMessage(dto);
        emailNotificationProducer.send(message);
    }

    private AgendamentoMessage createAgendamentoMessage(AppointmentDto dto) {
        // Parse da data/hora do formato "dd/MM/yyyy HH:mm"
        LocalDateTime dateTime = LocalDateTime.parse(dto.getDateTime(), DATE_TIME_FORMATTER);
        
        // Formata data para ddmmaaaa
        String dataAtendimento = dateTime.format(DATE_FORMATTER);
        
        // Formata hora para hh:mm
        String horaAtendimento = dateTime.format(TIME_FORMATTER);
        
        // Cria a mensagem no formato esperado pelo serviço de notificação
        AgendamentoMessage message = new AgendamentoMessage();
        message.setEmailPaciente(dto.getPatient().getEmail());
        message.setNomePaciente(dto.getPatient().getName());
        message.setDataAtendimento(dataAtendimento);
        message.setHoraAtendimento(horaAtendimento);
        message.setNomeResponsavel(dto.getDoctor().getName());
        message.setCanceled(dto.getCanceled());
        
        return message;
    }
}
