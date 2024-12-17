package com.clinic.patientDB.repository;

import com.clinic.patientDB.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
//    Optional<Patient> findByIdNumber(Long idNumber);
}

//public interface PatientRepository extends CrudRepository<Patient,Long> {
//    List<Patient> findAllByDateGreaterThan(String date);
//
//}

