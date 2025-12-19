package com.devrenno.appointment.repository;

import com.devrenno.appointment.entity.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends MongoRepository<Appointment, String>, AppointmentRepositoryCustom {
    List<Appointment> findByDateTimeGreaterThanAndPatientCpf(LocalDateTime dateTime, String cpf);
}
