package com.clinic.patientDB.controller;

import com.clinic.patientDB.model.*;
//import com.clinic.patientDB.repository.VisitRepository;
import com.clinic.patientDB.repository.PatientRepository;
import com.clinic.patientDB.repository.VisitRepository;
import com.clinic.patientDB.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

//todo authorize each method

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    @Autowired
    PatientService patientService;
    String UPLOAD_DIR = "c:\\users\\sharon\\documents\\lilach";


    @Autowired
    private final PatientRepository patientRepository;
    @Autowired
    private VisitRepository visitRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('SECRETARY')")
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }


    /**
     * One patient
     **/

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SECRETARY')")
    public Patient getPatientById(@PathVariable Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    @GetMapping("/{id}/visitDatess")
    @PreAuthorize("@roleChecker.hasPermission(authentication.principal.role, T(com.example.Role).SECRETARY)")
    public List<LocalDate> getPatientsVisitDatesById(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient == null) return null;
        return patient.getVisitsDates();
    }

    @GetMapping("/{id}/visits")
    @PreAuthorize("@roleChecker.hasPermission(authentication.principal.role, T(com.example.Role).SECRETARY)")
//    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE')") // רופאים ואחיות יכולים לגשת
    public List<Visit> getPatientsVisitsById(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient == null) return null;
        return patient.getVisits();
    }

    @GetMapping("/{id}/{visitDate}")
    @PreAuthorize("@roleChecker.hasPermission(authentication.principal.role, T(com.example.Role).SECRETARY)")
    public Optional<Visit> getVisitsByPatientId(@PathVariable Long id, @PathVariable String visitDate) {
        Patient patient = patientRepository.findById(id).orElse(null);
        return visitRepository.findByPatientAndVisitDate(patient, ExtractedFileInfo.extractVisitDate(visitDate));
    }

        // TODO: commented out by sharon
//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping("/upload")
//    @PreAuthorize("@roleChecker.hasPermission(authentication.principal.role, T(com.example.Role).DOCTOR)")
//    public ResponseEntity<Visit> insertFile(@RequestParam("file") String fileName) {
//        try {
//            Visit savedPatientFile = patientService.save(fileName);
//            return ResponseEntity.ok(savedPatientFile);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

//    @PreAuthorize("hasAnyRole('NURSE', 'SECRETARY', 'DOCTOR', 'ADMIN')")
    @GetMapping("/{id}/{visitDate}/pdfFiles")
    @PreAuthorize("hasRole('SECRETARY')")
    public List<PatientPdfFile> getPdfFilesByVisitId(@PathVariable Long id, @PathVariable String visitDate) {
        Patient patient = patientRepository.findById(id).orElse(null);
        Visit visit = visitRepository.findByPatientAndVisitDate(patient, ExtractedFileInfo.extractVisitDate(visitDate)).orElse(null);
        return visit.getPatientPdfFile();
    }

//    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    @GetMapping("/{id}/{visitDate}/docFiles")
    @PreAuthorize("hasRole('DOCTOR')")
    public List<PatientDocFile> getDocFilesByVisitId(@PathVariable Long id, @PathVariable String visitDate) {
        Patient patient = patientRepository.findById(id).orElse(null);
        Visit visit = visitRepository.findByPatientAndVisitDate(patient, ExtractedFileInfo.extractVisitDate(visitDate)).orElse(null);
        if (visit == null) return null;
        return visit.getPatientDocFile();
    }


    @RequiredRole(Role.ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Patient> updatePatientName(@PathVariable Long id, @RequestBody String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authorities = " + authentication.getAuthorities());
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        patient.setName(name);
        return ResponseEntity.ok(patientRepository.save(patient));
    }


    /**
     * handle patients
     **/

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<String>> addPatients() {
        List<String> patientNames = new ArrayList<>();
        for (Patient patient : patientRepository.findAll()) {
            patientNames.add(patient.getName());
            patientRepository.save(patient);
        }
        return ResponseEntity.ok(patientNames);
    }

    @PostMapping(value = "/generateDataBase")
    @PreAuthorize("@roleChecker.hasPermission(authentication.principal.role, T(com.example.Role).ADMIN)")
    public ResponseEntity<List<String>> generateDataBase() {
        return ResponseEntity.ok(patientService.generateDataBase());

    }


    //    @GetMapping("/open/{fileName}")
    @GetMapping("/open/{fileName}")
    @PreAuthorize("@roleChecker.hasPermission(authentication.principal.role, T(com.example.Role).SECRETARY)")
    public ResponseEntity<String> openFile(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get("Users/lilach/Documents/עניינים משפחתיים/", fileName);
            Desktop.getDesktop().open(filePath.toFile());
            return ResponseEntity.ok("File opened successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to open file.");
        }
    }



    @GetMapping("/numOfVisits/{id}")
    @PreAuthorize("@roleChecker.hasPermission(authentication.principal.role, T(com.example.Role).SECRETARY)")
    public ResponseEntity<Integer> numOfVisits(@PathVariable Long id) {
        return ResponseEntity.ok(patientRepository.getById(id).getNumberOfVisits());

    }


    /**
     * Handles file uploads via a POST request.
     * The file is expected to be sent as 'multipart/form-data'.
     *
     * @param files The uploaded file, represented by Spring's MultipartFile.
     * @return A ResponseEntity indicating success or failure of the upload.
     */
    @PostMapping("/upload") // Maps HTTP POST requests to /upload to this method
    public ResponseEntity<String> uploadFile(@RequestParam("files") MultipartFile[] files) {
        // Create the upload directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to create upload directory: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<String> uploadedFileNames = new ArrayList<>();
        List<String> failedFileNames = new ArrayList<>();

        // Process each file in the array
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                // Handle empty file (e.g., user selected an empty file or something went wrong)
                failedFileNames.add(file.getOriginalFilename() + " (empty)");
                continue;
            }

            // Get the original file name
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.trim().isEmpty()) {
                failedFileNames.add("Unnamed file");
                continue;
            }

            try {
                // Define the target path for the file
                Path filePath = Paths.get(UPLOAD_DIR + fileName);

                // Save the file to the specified path
                Files.copy(file.getInputStream(), filePath);

                uploadedFileNames.add(fileName);
            } catch (IOException e) {
                // Log the exception and add to failed list
                System.err.println("Failed to upload " + fileName + ": " + e.getMessage());
                failedFileNames.add(fileName + " (upload failed)");
            }
        }

        // Construct the response message
        StringBuilder responseMessage = new StringBuilder();
        if (!uploadedFileNames.isEmpty()) {
            responseMessage.append("Successfully uploaded: ").append(String.join(", ", uploadedFileNames)).append(". ");
        }
        if (!failedFileNames.isEmpty()) {
            responseMessage.append("Failed to upload: ").append(String.join(", ", failedFileNames)).append(". ");
            return new ResponseEntity<>(responseMessage.toString(), HttpStatus.MULTI_STATUS); // Use 207 Multi-Status for partial success
        }

        return new ResponseEntity<>(responseMessage.toString(), HttpStatus.OK);
    }


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiredRole {
        Role value(); // רמת ההרשאה הנדרשת
    }



}