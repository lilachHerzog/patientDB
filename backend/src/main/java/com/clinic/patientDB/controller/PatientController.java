package com.clinic.patientDB.controller;
import com.clinic.patientDB.service.dataBaseService;
import com.clinic.patientDB.model.GenerateDemoPatients;
import com.clinic.patientDB.model.PatientFile;
import com.clinic.patientDB.model.Patient;
//import com.clinic.patientDB.repository.PatientFileRepository;
import com.clinic.patientDB.repository.PatientRepository;
import com.clinic.patientDB.service.FileService;
import com.clinic.patientDB.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    @Autowired
    PatientService patientService;

    @Autowired
    FileService fileService;

    @Autowired
    GenerateDemoPatients generateDemoPatients;

    @Autowired
    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @PostMapping("/upload")
    public ResponseEntity<PatientFile> insertFile(@RequestParam("file") String fileName) {
        try {
            PatientFile savedPatientFile = fileService.save(new File(fileName));
            return ResponseEntity.ok(savedPatientFile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @RequestMapping(value = "genrate names", method = RequestMethod.GET)
    public String genrateNames() throws IOException {
        return  generateDemoPatients.generateDemoFileName();

    }

    @PostMapping(value = "/generateDataBase")
    public ResponseEntity<List<String>> generateDataBase() {
        return ResponseEntity.ok(fileService.dataBaseService());//Stream.concat(db.getPdf().stream(), db.getWord().stream()));

//        System.out.println( ResponseEntity.ok(patientService.generateDataBase()));
//        return ResponseEntity.ok("File opened successfully!");

    }

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




//    @GetMapping
//    public String openWordFile(String patientID) {
//        return fileService.getLatestFile(patientID);
//    }
}

