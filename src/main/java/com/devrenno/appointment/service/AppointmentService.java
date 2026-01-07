package com.devrenno.appointment.service;

import com.devrenno.appointment.dto.AppointmentDto;
import com.devrenno.appointment.dto.AppointmentMapper;
import com.devrenno.appointment.dto.FilterDto;
import com.devrenno.appointment.entity.Appointment;
import com.devrenno.appointment.jwt.JwtService;
import com.devrenno.appointment.repository.AppointmentRepository;
import com.devrenno.appointment.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
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
    
    @Autowired
    private JwtService jwtService;

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
        // Validação de acesso: pacientes só podem ver suas próprias consultas
        if (SecurityUtils.isPatient()) {
            String token = SecurityUtils.getCurrentToken();
            if (token == null) {
                throw new AccessDeniedException("Token não encontrado. Acesso negado.");
            }
            
            String authenticatedUserCpf = jwtService.extractCpf(token);
            if (authenticatedUserCpf == null || authenticatedUserCpf.trim().isEmpty()) {
                throw new AccessDeniedException("CPF não encontrado no token. Acesso negado.");
            }
            
            // Normaliza CPFs para comparação (remove formatação)
            String normalizedRequestCpf = normalizeCpf(cpf);
            String normalizedAuthenticatedCpf = normalizeCpf(authenticatedUserCpf);
            
            if (!normalizedRequestCpf.equals(normalizedAuthenticatedCpf)) {
                throw new AccessDeniedException("Pacientes só podem visualizar suas próprias consultas.");
            }
        }
        // Médicos e enfermeiros podem ver qualquer consulta (sem validação adicional)
        
        var entities = appointmentRepository.findByDateTimeGreaterThanAndPatientCpf(LocalDateTime.now(), cpf);
        List<AppointmentDto> dtos = new ArrayList<>();
        for (Appointment entity : entities) {
            var dto = AppointmentMapper.toDto(entity);
            dtos.add(dto);
        }
        return dtos;
    }
    
    /**
     * Normaliza CPF removendo formatação (pontos, traços e espaços)
     */
    private String normalizeCpf(String cpf) {
        if (cpf == null) {
            return "";
        }
        return cpf.replaceAll("[.\\-\\s]", "");
    }
}
