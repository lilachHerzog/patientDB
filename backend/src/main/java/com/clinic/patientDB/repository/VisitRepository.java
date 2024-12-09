package com.clinic.patientDB.repository;

import com.clinic.patientDB.model.Patient;
import com.clinic.patientDB.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.Optional;


@Repository
public interface VisitRepository extends JpaRepository<Visit, LocalDate> {
    Optional<Visit> findByPatientAndVisitDate(Patient patient, LocalDate date);
    @Query("SELECT v FROM Visit v WHERE v.patient.id = :patientId ORDER BY v.visitDate DESC")
    Visit findFirstByPatientIdOrderByVisitDateDesc(@Param("patientId") Long patientId);

//    List<Visit> getIdByVisitDate(LocalDate visitDate);
}
