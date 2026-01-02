package com.devrenno.appointment.controller;

import com.devrenno.appointment.dto.AppointmentDto;
import com.devrenno.appointment.dto.FilterDto;
import com.devrenno.appointment.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    /**
     * Criar consulta - Apenas médicos e enfermeiros
     */
    @MutationMapping
    @PreAuthorize(
            "hasAnyAuthority(" +
                    "'ROLE_DOCTOR', " +
                    "'ROLE_NURSE'" +
                    ")"
    )
    public AppointmentDto createAppointment(@Argument AppointmentDto dto) {
        dto = appointmentService.create(dto);
        return dto;
    }

    /**
     * Editar consulta - Apenas médicos
     */
    @MutationMapping
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public AppointmentDto updateAppointment(@Argument AppointmentDto dto) {
        dto = appointmentService.update(dto);
        return dto;
    }

    /**
     * Cancelar consulta - Apenas médicos
     */
    @MutationMapping
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public AppointmentDto cancelAppointment(@Argument String id) {
        var dto = appointmentService.cancel(id);
        return dto;
    }

    /**
     * Visualizar todas as consultas - Apenas médicos (histórico completo)
     */
    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public List<AppointmentDto> findAllAppointment() {
        var dtos = appointmentService.findAll();
        return dtos;
    }

    /**
     * Buscar consultas por filtros (histórico) - Médicos e enfermeiros
     */
    @QueryMapping
    @PreAuthorize(
            "hasAnyAuthority(" +
                    "'ROLE_DOCTOR', " +
                    "'ROLE_NURSE'" +
                    ")"
    )
    public List<AppointmentDto> findAppointmentByFilters(@Argument FilterDto filter) {
        var dtos = appointmentService.findAppointmentByFilter(filter);
        return dtos;
    }

    /**
     * Visualizar consultas futuras por CPF - Público (mas validado internamente para pacientes)
     * Pacientes só podem ver suas próprias consultas
     * Médicos e enfermeiros podem ver qualquer consulta
     */
    @QueryMapping
    @PreAuthorize(
            "hasAnyAuthority(" +
                    "'ROLE_DOCTOR', " +
                    "'ROLE_NURSE', " +
                    "'ROLE_PATIENT'" +
                    ")"
    )
    public List<AppointmentDto> findFutureAppointmentByPatientCpf(@Argument String cpf) {
        var dtos = appointmentService.findFutureAppointmentByPatientCpf(cpf);
        return dtos;
    }
}
