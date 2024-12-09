package com.clinic.patientDB.service;

import com.clinic.patientDB.model.Patient;
import com.clinic.patientDB.model.Visit;
import com.clinic.patientDB.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class VisitService {
    @Autowired
    VisitRepository visitRepository;
    public Visit createVisit(Visit visit) {
        return visitRepository.save(visit);
    }

}
