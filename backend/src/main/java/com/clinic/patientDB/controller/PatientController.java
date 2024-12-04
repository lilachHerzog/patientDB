package com.clinic.patientDB.controller;

import com.clinic.patientDB.model.Patient;
import com.clinic.patientDB.model.Visit;
//import com.clinic.patientDB.repository.VisitRepository;
import com.clinic.patientDB.repository.PatientRepository;
import com.clinic.patientDB.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.testng.AssertJUnit.assertEquals;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    @Autowired
    PatientService patientService;


    @Autowired
    private final PatientRepository patientRepository;
//    @Autowired
//    private VisitRepository visitRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
    @PostMapping("/getPatient/{id}")
    public Optional<Patient> getPatient(Long id) {
        return patientRepository.findById(id);
    }

    @PostMapping("/upload")
    public ResponseEntity<Visit> insertFile(@RequestParam("file") String fileName) {
        try {
            Visit savedPatientFile = patientService.save(fileName);
            return ResponseEntity.ok(savedPatientFile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping
    public ResponseEntity<List<String>> addPatients() {
        List<String> patientNames = new ArrayList<>();
        for (Patient patient : patientRepository.findAll()) {
            patientNames.add(patient.getName());
            patientRepository.save(patient);
        }
        return ResponseEntity.ok(patientNames);
    }

    @PostMapping(value = "/generateDataBase")
    public ResponseEntity<List<String>> generateDataBase() {
        return ResponseEntity.ok(patientService.generateDataBase());//Stream.concat(db.getPdf().stream(), db.getWord().stream()));

//        System.out.println( ResponseEntity.ok(patientService.generateDataBase()));
//        return ResponseEntity.ok("File opened successfully!");

    }

//    @RequestMapping(value = "/getLatestWordFile/{patientId}", method = RequestMethod.GET)
//    public ResponseEntity<String> getLatestWordFile(@PathVariable String patientId) {
//        return ResponseEntity.ok(fileService.getLatestWordFile(patientId));
//    }
//    @RequestMapping(value = "/getLatestPdfFile/{patientId}", method = RequestMethod.GET)
//    public ResponseEntity<String> getLatestPdfFile(@PathVariable String patientId) {
//        return ResponseEntity.ok(fileService.getLatestPdfFile(patientId));
//    }

    //    @GetMapping("/open/{fileName}")
    @RequestMapping(value = "/open/{fileName}", method = RequestMethod.GET)
    public ResponseEntity<String> openFile(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get("Users/lilach/Documents/עניינים משפחתיים/", fileName);
            Desktop.getDesktop().open(filePath.toFile());
            return ResponseEntity.ok("File opened successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to open file.");
        }
    }

//    @PostConstruct
//    public void init() {
//        Patient patient = new Patient(46854L, "Test Patient", "");
//        patientRepository.save(patient);
//        Visit visit = new Visit(patient);
////        visit.setPatient(patient);
//
//        patient.getVisits().add(visit);
//        visitRepository.save(visit);
//        patientRepository.save(patient);
//
//        Patient savedPatient = patientRepository.findById(patient.getId()).get();
//        assertEquals(patient.getName(), savedPatient.getName());
//        assertEquals(1, savedPatient.getVisits().size());    }







//    @GetMapping
//    public String openWordFile(String patientID) {
//        return fileService.getLatestFile(patientID);
//    }
}

