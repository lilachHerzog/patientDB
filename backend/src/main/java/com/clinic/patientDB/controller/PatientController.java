package com.clinic.patientDB.controller;

import com.clinic.patientDB.model.*;
//import com.clinic.patientDB.repository.VisitRepository;
import com.clinic.patientDB.repository.PatientRepository;
import com.clinic.patientDB.repository.VisitRepository;
import com.clinic.patientDB.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/patients")
public class PatientController {
    @Autowired
    PatientService patientService;


    @Autowired
    private final PatientRepository patientRepository;
    @Autowired
    private VisitRepository visitRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }


    /** One patient **/

    @GetMapping("/patients/{id}")
    public Patient getPatientById(@PathVariable Long id){
        return patientRepository.findById(id).orElse(null);
    }

    @GetMapping("/patients/{id}/{visitDate}")
    public Optional<Visit> getPatientById(@PathVariable Long id, @PathVariable String visitDate){
        Patient patient = patientRepository.findById(id).orElse(null);
        return visitRepository.findByPatientAndVisitDate(patient, ExtractedFileInfo.extractVisitDate(visitDate));
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

//    @DeleteMapping("/patients/{id}")
//    public ResponseEntity<String> deletePatient(@PathVariable Long id){
//        Patient patient = patientRepository.findById(id).orElse(null);
//        if (patient == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//        patientRepository.delete(patient);
//        return ResponseEntity.ok(patient.getName() + " deleted");
//    }

    @PutMapping("/patients/{id}")
    public ResponseEntity<Patient> updatePatientName(@PathVariable Long id, @RequestBody String name){
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        patient.setName(name);
        return ResponseEntity.ok(patientRepository.save(patient));
    }


    /** One file **/



//     @DeleteMapping("/patients/{id}")
//    public ResponseEntity<Boolean> deleteFile(@PathVariable String filename ){
//         ExtractedFileInfo extractedFileInfo = new ExtractedFileInfo(filename);
//        Patient patient = patientRepository.findById(extractedFileInfo.getId()).orElse(null);
//        if (patient == null) {
//            return ResponseEntity.ok(patientRepository.findAll().contains(patient));//status(HttpStatus.NOT_FOUND).body("patient with ID " + extractedFileInfo.getId() + " not found");
//        }
//        Visit visit = visitRepository.findByPatientAndVisitDate(patient, extractedFileInfo.getVisitDate()).orElse(null);
//         if (visit == null) {
//             return ResponseEntity.ok(patient.getVisits().contains(visit));//HttpStatus.NOT_FOUND).body("No visit found for " + extractedFileInfo.getName() + " on " + extractedFileInfo.getVisitDate());
//         }
//         if (extractedFileInfo.getUpdateDateAndTime() == null){
//             PatientDocFile docFile = visit.getPatientDocFile(filename);//.orElse(null);
////             if (docFile == null) {
////                 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(extractedFileInfo.getFileType() + "not found for patient " + extractedFileInfo.getName() + " on " + extractedFileInfo.getVisitDate());
////             }
////             visit.getPatientDocFile().remove(docFile);
//             return ResponseEntity.ok(visit.getPatientDocFile().remove(docFile));
//         } else  {
//             PatientPdfFile pdfFile = visit.getPatientPdfFile(filename);//.orElse(null);
//             return ResponseEntity.ok(visit.getPatientDocFile().remove(pdfFile));
//         }
//    }

//    @PutMapping("/patients/{id}")
//    public ResponseEntity<Object> GetFile( @PathVariable String filename ){
//            ExtractedFileInfo extractedFileInfo = new ExtractedFileInfo(filename);
//            Patient patient = patientRepository.findById(extractedFileInfo.getId()).orElse(null);
//            if (patient == null) {
//                return ResponseEntity.ok(patientRepository.findAll().contains(patient));//status(HttpStatus.NOT_FOUND).body("patient with ID " + extractedFileInfo.getId() + " not found");
//            }
//            Visit visit = visitRepository.findByPatientAndVisitDate(patient, extractedFileInfo.getVisitDate()).orElse(null);
//            if (visit == null) {
//                return ResponseEntity.ok(patient.getVisits().contains(visit));//HttpStatus.NOT_FOUND).body("No visit found for " + extractedFileInfo.getName() + " on " + extractedFileInfo.getVisitDate());
//            }
//            if (extractedFileInfo.getUpdateDateAndTime() == null){
//                PatientDocFile docFile = visit.getPatientDocFile(filename).orElse(null);
////             if (docFile == null) {
////                 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(extractedFileInfo.getFileType() + "not found for patient " + extractedFileInfo.getName() + " on " + extractedFileInfo.getVisitDate());
////             }
////             visit.getPatientDocFile().remove(docFile);
//                return ResponseEntity.ok(visit.getPatientDocFile().remove(docFile));
//            } else  {
//                PatientPdfFile pdfFile = visit.getPatientPdfFile(filename).orElse(null);
//                return ResponseEntity.ok(visit.getPatientDocFile().remove(pdfFile));
//            }
//    }



    /** handle patients **/

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

    @RequestMapping(value = "/numOfVisits/{id}", method = RequestMethod.GET)
    public ResponseEntity<Integer> numOfVisits(@PathVariable Long id) {
        return ResponseEntity.ok(patientRepository.getById(id).getNumberOfVisits());

    }


//    @PostConstruct
//    public void init() {
//        Long id = 46854L;
//        Patient patient = new Patient(id, "Test Patient", "");
//        patientRepository.save(patient);
//        Visit visit = new Visit(patient);
////        visit.setPatient(patient);
//
//        patient.getVisits().add(visit);
//        visitRepository.save(visit);
//        patientRepository.save(patient);
//
//        Patient savedPatient = patientRepository.findById(patient.getId()).get();
//        System.out.println("names are equal " + patient.getName().equals(savedPatient.getName()));
//        System.out.println("savedPatient.getVisits().size(): " + savedPatient.getVisits().size());
//        List<Patient> allPatients = patientRepository.findAll();
//        System.out.println("Number of patients in DB: " + allPatients.size());
//
//        // מחיקת מטופל לדוגמה
//        patientRepository.deleteById(id);
//    }







//    @GetMapping
//    public String openWordFile(String patientID) {
//        return fileService.getLatestFile(patientID);
//    }
}

