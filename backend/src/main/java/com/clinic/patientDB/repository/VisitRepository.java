package com.clinic.patientDB.repository;

import com.clinic.patientDB.model.Patient;
import com.clinic.patientDB.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.Optional;


@Repository
public interface VisitRepository extends JpaRepository<Visit, LocalDate> {
    Optional<Visit> findByPatientAndVisitDate(Patient patient, LocalDate date);
//    Visit getTopByVisitOrderByVisitDateDesc(Patient patient);

//    List<Visit> getIdByVisitDate(LocalDate visitDate);
}
