package com.devrenno.appointment.dto;

import com.devrenno.appointment.entity.Address;
import com.devrenno.appointment.entity.Doctor;
import com.devrenno.appointment.entity.Patient;
import com.devrenno.appointment.entity.Appointment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppointmentMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static Appointment toEntity(AppointmentDto dto) {
        var appointment = new Appointment();
        appointment.setId(dto.getId());
        LocalDateTime dateTime = LocalDateTime.parse(dto.getDateTime(), formatter);
        appointment.setDateTime(dateTime);
        appointment.setCanceled(dto.getCanceled());

        var address = new Address();
        address.setStreet(dto.getAddress().getStreet());
        address.setNumber(dto.getAddress().getNumber());
        appointment.setAddress(address);

        var doctor = new Doctor();
        doctor.setName(dto.getDoctor().getName());
        doctor.setCrm(dto.getDoctor().getCrm());
        appointment.setDoctor(doctor);

        var patient = new Patient();
        patient.setName(dto.getPatient().getName());
        patient.setCpf(dto.getPatient().getCpf());
        patient.setEmail(dto.getPatient().getEmail());
        appointment.setPatient(patient);

        return appointment;
    }

    public static AppointmentDto toDto(Appointment entity) {
        var appointmentDto = new AppointmentDto();
        appointmentDto.setId(entity.getId());
        appointmentDto.setDateTime(entity.getDateTime().format(formatter));
        appointmentDto.setCanceled(entity.getCanceled());

        var addressDto = new AddressDto();
        addressDto.setStreet(entity.getAddress().getStreet());
        addressDto.setNumber(entity.getAddress().getNumber());
        appointmentDto.setAddress(addressDto);

        var doctorDto = new DoctorDto();
        doctorDto.setName(entity.getDoctor().getName());
        doctorDto.setCrm(entity.getDoctor().getCrm());
        appointmentDto.setDoctor(doctorDto);

        var patientDto = new PatientDto();
        patientDto.setName(entity.getPatient().getName());
        patientDto.setCpf(entity.getPatient().getCpf());
        patientDto.setEmail(entity.getPatient().getEmail());
        appointmentDto.setPatient(patientDto);

        return appointmentDto;
    }
}
