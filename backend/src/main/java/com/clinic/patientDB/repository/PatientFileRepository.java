package com.clinic.patientDB.repository;

import com.clinic.patientDB.model.PatientFile;
import com.clinic.patientDB.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientFileRepository extends JpaRepository<PatientFile, Integer> {
    PatientFile getTopByPatientOrderByVisitDateDesc(Patient patient);
}
//public interface PatientFileRepository extends CrudRepository<File,Long> {}

