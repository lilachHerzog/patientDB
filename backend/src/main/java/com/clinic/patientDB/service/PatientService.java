package com.clinic.patientDB.service;

import com.clinic.patientDB.model.Patient;
import com.clinic.patientDB.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {
    @Autowired
    PatientRepository repository;

    public Iterable<Patient> all() {
        return repository.findAll();
    }

    public Optional<Patient> findById(String id) {
        return repository.findById(Long.parseLong(id));
    }


    public Patient save(Patient patient) {

        return repository.save(patient);
    }

    public void delete(Patient patient) {
        repository.delete(patient);
    }

    @Transactional
    public Patient updatePatient(Long id, Patient updatedPatient) {
        return repository.findById(id).map(patient -> {
            patient.setName(updatedPatient.getName());
            patient.setFiles(updatedPatient.getFiles());
            return patient;
        }).orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    public List<String> generateDataBase() {
        String directoryPath = "/Users/lilach/Documents/עניינים משפחתיים/לילך/job hunting";
        File directory = new File(directoryPath); //create an object for specific directory
        File[] patientFiles = directory.listFiles(); //  get all the files of a directory
        // Print name of the all files present in that path

        if (patientFiles != null) {
            return Arrays.stream(patientFiles).map(File::getName).collect(Collectors.toList()); // Extract file names
            }
        return List.of(new String[]{"patientFiles == null"});
        }




}
