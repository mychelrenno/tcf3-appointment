package com.devrenno.appointment.repository;

import com.devrenno.appointment.dto.FilterDto;
import com.devrenno.appointment.entity.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepositoryImpl implements AppointmentRepositoryCustom {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Appointment> findByFilters(FilterDto filter) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();
        if (filter != null) {
            if (filter.doctorCrm() != null) {
                criteriaList.add(Criteria.where("doctor.crm").is(filter.doctorCrm()));
            }

            if (filter.patientCpf() != null) {
                criteriaList.add(Criteria.where("patient.cpf").is(filter.patientCpf()));
            }

            if (filter.startDateTime() != null) {
                criteriaList.add(Criteria.where("dateTime").gte(LocalDateTime.parse(filter.startDateTime(), formatter)));
            }

            if (filter.endDateTime() != null) {
                criteriaList.add(Criteria.where("dateTime").lte(LocalDateTime.parse(filter.endDateTime(), formatter)));
            }

            if (filter.canceled() != null) {
                criteriaList.add(Criteria.where("canceled").is(filter.canceled()));
            }

            if (!criteriaList.isEmpty()) {
                query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
            }
        }
        return mongoTemplate.find(query, Appointment.class);
    }
}
