package com.devrenno.appointment.repository;

import com.devrenno.appointment.dto.FilterDto;
import com.devrenno.appointment.entity.Appointment;

import java.util.List;

public interface AppointmentRepositoryCustom {
    List<Appointment> findByFilters(FilterDto filter);
}
