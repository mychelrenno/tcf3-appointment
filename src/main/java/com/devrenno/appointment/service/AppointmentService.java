package com.devrenno.appointment.service;

import com.devrenno.appointment.dto.AppointmentDto;
import com.devrenno.appointment.dto.AppointmentMapper;
import com.devrenno.appointment.dto.FilterDto;
import com.devrenno.appointment.entity.Appointment;
import com.devrenno.appointment.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private EmailNotificationService emailNotificationService;

    public AppointmentDto create(AppointmentDto dto) {
        var entity = AppointmentMapper.toEntity(dto);
        entity.setCanceled(false);
        entity = appointmentRepository.save(entity);
        dto = AppointmentMapper.toDto(entity);
        emailNotificationService.send(dto);
        return dto;
    }

    public AppointmentDto update(AppointmentDto dto) {
        var optionalEntity = appointmentRepository.findById(dto.getId());
        if (optionalEntity.isEmpty()) {
            throw new IllegalArgumentException("Appointment not found.");
        }
        var entity = AppointmentMapper.toEntity(dto);
        entity = appointmentRepository.save(entity);
        dto = AppointmentMapper.toDto(entity);
        emailNotificationService.send(dto);
        return dto;
    }

    public AppointmentDto cancel(String id) {
        var optionalEntity = appointmentRepository.findById(id);
        if (optionalEntity.isEmpty()) {
            throw new IllegalArgumentException("Appointment not found.");
        }
        var entity = optionalEntity.get();
        entity.setCanceled(true);
        entity = appointmentRepository.save(entity);
        var dto = AppointmentMapper.toDto(entity);
        emailNotificationService.send(dto);
        return dto;
    }

    public List<AppointmentDto> findAll() {
        var entities = appointmentRepository.findAll();
        List<AppointmentDto> dtos = new ArrayList<>();
        for (Appointment entity : entities) {
            var dto = AppointmentMapper.toDto(entity);
            dtos.add(dto);
        }
        return dtos;
    }

    public List<AppointmentDto> findAppointmentByFilter(FilterDto filter) {
        var entities = appointmentRepository.findByFilters(filter);
        List<AppointmentDto> dtos = new ArrayList<>();
        for (Appointment entity : entities) {
            var dto = AppointmentMapper.toDto(entity);
            dtos.add(dto);
        }
        return dtos;
    }

    public List<AppointmentDto> findFutureAppointmentByPatientCpf(String cpf) {
        // Verifica se o usuário autenticado é paciente
        // NOTA: Para uma validação completa de que pacientes só veem suas próprias consultas,
        // seria necessário incluir o CPF no token JWT e comparar aqui.
        // Por enquanto, o controle de acesso é feito via @PreAuthorize no controller,
        // permitindo acesso apenas a usuários autenticados (médicos, enfermeiros e pacientes).
        
        var entities = appointmentRepository.findByDateTimeGreaterThanAndPatientCpf(LocalDateTime.now(), cpf);
        List<AppointmentDto> dtos = new ArrayList<>();
        for (Appointment entity : entities) {
            var dto = AppointmentMapper.toDto(entity);
            dtos.add(dto);
        }
        return dtos;
    }
}
