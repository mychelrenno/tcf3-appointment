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

    @MutationMapping
    @PreAuthorize(
            "hasAnyAuthority(" +
                    "T(com.devrenno.appointment.security.UserRole).ROLE_DOCTOR.name(), " +
                    "T(com.devrenno.appointment.security.UserRole).ROLE_NURSE.name()" +
                    ")"
    )
    public AppointmentDto createAppointment(@Argument AppointmentDto dto) {
        dto = appointmentService.create(dto);
        return dto;
    }

    @MutationMapping
    @PreAuthorize(
            "hasAnyAuthority(" +
                    "T(com.devrenno.appointment.security.UserRole).ROLE_DOCTOR.name(), " +
                    "T(com.devrenno.appointment.security.UserRole).ROLE_NURSE.name()" +
                    ")"
    )
    public AppointmentDto updateAppointment(@Argument AppointmentDto dto) {
        dto = appointmentService.update(dto);
        return dto;
    }

    @MutationMapping
    @PreAuthorize(
            "hasAnyAuthority(" +
                    "T(com.devrenno.appointment.security.UserRole).ROLE_DOCTOR.name(), " +
                    "T(com.devrenno.appointment.security.UserRole).ROLE_NURSE.name()" +
                    ")"
    )
    public AppointmentDto cancelAppointment(@Argument String id) {
        var dto = appointmentService.cancel(id);
        return dto;
    }

    @QueryMapping
    @PreAuthorize(
            "hasAnyAuthority(" +
                    "T(com.devrenno.appointment.security.UserRole).ROLE_DOCTOR.name(), " +
                    "T(com.devrenno.appointment.security.UserRole).ROLE_NURSE.name()" +
                    ")"
    )
    public List<AppointmentDto> findAllAppointment() {
        var dtos = appointmentService.findAll();
        return dtos;
    }

    @QueryMapping
    @PreAuthorize(
            "hasAnyAuthority(" +
                    "T(com.devrenno.appointment.security.UserRole).ROLE_DOCTOR.name(), " +
                    "T(com.devrenno.appointment.security.UserRole).ROLE_NURSE.name()" +
                    ")"
    )
    public List<AppointmentDto> findAppointmentByFilters(@Argument FilterDto filter) {
        var dtos = appointmentService.findAppointmentByFilter(filter);
        return dtos;
    }

    @QueryMapping
    public List<AppointmentDto> findFutureAppointmentByPatientCpf(@Argument String cpf) {
        var dtos = appointmentService.findFutureAppointmentByPatientCpf(cpf);
        return dtos;
    }
}
