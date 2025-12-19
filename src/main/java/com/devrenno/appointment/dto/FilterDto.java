package com.devrenno.appointment.dto;

public record FilterDto(
        String startDateTime,
        String endDateTime,
        Boolean canceled,
        String doctorCrm,
        String patientCpf
) {}
