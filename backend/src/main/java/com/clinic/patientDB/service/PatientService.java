package com.clinic.patientDB.service;

import com.clinic.patientDB.model.ExtractedFileInfo;
import com.clinic.patientDB.model.Patient;
import com.clinic.patientDB.model.Visit;
import com.clinic.patientDB.repository.PatientRepository;
import com.clinic.patientDB.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.io.File;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;

/**
 * The type Patient service.
 */
@Service
public class PatientService {
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    VisitRepository visitRepository;


    public Iterable<Patient> all() {
        return patientRepository.findAll();
    }
    public Optional<Patient> findById(Long id) {return patientRepository.findById(id);}
    public Optional<Patient> findById(String id) {
        return findById(Long.parseLong(id));
    }



    /**
     * extract all info {ExtractedFileInfo(fileName)}
     * get patient
     * get visit
     * add file
     */
    public Visit save(String filePath) {
        String fileName = filePath.split("/")[filePath.split("/").length - 1];
        ExtractedFileInfo extractFileInfo = new ExtractedFileInfo(fileName);
        Long id = extractFileInfo.getId();
        Patient patient = findById(id).orElse(new Patient(id, extractFileInfo.getName(), extractFileInfo.getIdType()));
        LocalDate visitDate = extractFileInfo.getVisitDate();
        Visit visit = patient.addVisit(visitDate);
//                visitRepository.findByPatientAndVisitDate(patient, visitDate).orElseGet(()->{
//            Visit newVisit = new Visit(patient, visitDate);
//            patient.getVisits().add(newVisit);
//            return newVisit;
//        });

        String filetype = extractFileInfo.getFileType();
        if (filetype.contains("pdf")) {
            visit.addPatientPdfFile(extractFileInfo.getUpdateDateAndTime(), extractFileInfo.getActionType(), filePath);
        } else if (filetype.contains("doc")) {
            visit.addPatientDocFile(extractFileInfo.getActionType(), filePath);
        } else {
            System.out.println("file type" + filetype + "is not supported, file " + fileName + "not saved");
        }
        System.out.println("filename: {}" + fileName + "\n\tpatient: " + patient.getName() + "\n\tvisit patient: " + visit.getPatient().getName());

//        visitRepository.save(visit);
        patientRepository.save(patient);
        return visit;

    }

    public void delete(Patient patient) {
        patientRepository.delete(patient);
    }

    @Transactional
    public Patient updatePatient(Long id, Patient updatedPatient) {
        return patientRepository.findById(id).map(patient -> {
            patient.setName(updatedPatient.getName());
//            patient.setFiles(updatedPatient.getFiles());
            return patient;
        }).orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    //    /**
//     * Gets path.
//     *
//     * extract the Patient and the visit date from @param patientFile
//     * and @return the path (format {id}_{name)_{visitDate}.doc)
//     */
//    public String getPath(PatientFile patientFile) {
//        Patient patient = patientFile.getPatient();
//        String name = String.join("_", patient.getName().split(" "));
//        return "%s_%s_%s.doc".formatted(patientFile.getVisitDate().toString(), String.format("%09d", patient.getId()), name);
//    }

//    public List<String> generateDataBase() {
//        String directoryPath = "/Users/lilach/Documents/עניינים משפחתיים/לילך/job hunting";
//        File directory = new File(directoryPath); //create an object for specific directory
//        File[] patientFiles = directory.listFiles(); //  get all the files of a directory
//        // Print name of the all files present in that path
//
//        if (patientFiles != null) {
//            return Arrays.stream(patientFiles).map(File::getName).collect(Collectors.toList()); // Extract file names
//            }
//        return List.of(new String[]{"patientFiles == null"});
//        }


    /**
     * To generate the data base
     * Find folders containing pdf/doc files
     * Send the folders to insertFile() to extract files from folders
     *
     * @return the list of filenames extracted from the folders
     */
    public List<String> generateDataBase() {
        String basePath = "/Users/lilach/StudioProjects/patientDB/files";
        int currentYear = Year.now().getValue();
        int startYear = 2012;
        List<String> files = new ArrayList<>();
        files.addAll(insertFilesFromFolder(basePath + "/PDF/", ".pdf"));

        for (int i = startYear; i <= currentYear; i++) {
            String yearPath =  basePath + "/" + i + "/";
            List<String> temp = insertFilesFromFolder(yearPath, ".doc");
            if (temp != null) {
                files.addAll(temp);
            }
        }
        return files;
    }

    /**
     * Insert files from a folder to the database (with save())
     *
     * @param basePath the folder path
     * @param end      ".pdf" or ".doc"
     * @return the list of the file names extracted from the folder
     */
    public List<String> insertFilesFromFolder(String basePath, String end){
        List<String> patientFileList = new ArrayList<>();
        File fileDir = new File(basePath);
        if (!checkDir(fileDir)){
            return null;
        }
        File[] files = fileDir.listFiles((dir, name) -> name.endsWith(end));
        if (files != null) {
            for (File file : files) {
                Visit visit = save(file.getPath());
                String txt = "inserted [" + file.getName() + "] to patient    [" + visit.getPatient().getName() + "] id " + visit.getPatient().getId();
                patientFileList.add(txt);

//                patientFileList.add("inserted " + end + "file " + file.getName());
//                System.out.println(" file: " + file.getAbsolutePath());
            }
        }
        return patientFileList;
    }

    public boolean checkDir(File fileDir){
        if (!fileDir.exists()) {
            System.out.println("folder " + fileDir.getName() + " does not exist.");
            return false;
        }
        if (!fileDir.isDirectory()) {
            System.out.println(fileDir.getName() + " is not a directory.");
            return false;
        }
        System.out.println("going over files in folder: " + fileDir.getName());

        return true;
    }



}
