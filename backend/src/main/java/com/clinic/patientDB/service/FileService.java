package com.clinic.patientDB.service;

import com.clinic.patientDB.model.PatientFile;
import com.clinic.patientDB.model.Patient;
import com.clinic.patientDB.repository.PatientFileRepository;
import com.clinic.patientDB.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    @Autowired
    PatientFileRepository fileRepository;
    @Autowired
    PatientRepository patientRepository;

    public Iterable<PatientFile> all() {
        return fileRepository.findAll();
    }

    public Optional<PatientFile> findById(Integer id) {
        return fileRepository.findById(id);
    }

    public PatientFile save(File patientFileName) {
        String[] cut = patientFileName.getName().split("_");
        String patientID = cut[cut.length - 1].replace(".doc","").replace(".pdf","");
        String name = String.join(" ", Arrays.copyOfRange(cut, 1, cut.length - 1));
        Patient patient = patientRepository.findById(Long.parseLong(patientID)).orElse(new Patient(name, Long.parseLong(patientID)));
        String dateAndTime = cut[0];

        PatientFile patientFile = new PatientFile(dateAndTime, patient); // ייתכן שצריך לעדכן את הקונסטרוקטור
        patientFile.setFilename(patientFileName.getName());
        patient.addPatientFile(patientFile);
        patientRepository.save(patient);
        return patientFile;
    }

    public void delete(PatientFile patientFile) {
        fileRepository.delete(patientFile);
    }

    public String getPath(PatientFile patientFile) {
        Patient patient = patientFile.getPatient();
        String name = String.join("_", patient.getName().split(" "));
        return "{}_{}_{}".formatted(patient.getId(), name, patientFile.getVisitDate().toString());
    }


    public List<String> dataBaseService() {
        String basePath = "/Users/lilach/StudioProjects/patientDB/files";
        int currentYear = Year.now().getValue();
        int startYear = 2012;
        List<String> files = new ArrayList<>();



        files.addAll(insertFile(basePath + "/PDF/", ".pdf"));

        for (int i = startYear; i <= currentYear; i++) {
            String yearPath = basePath +"/" + i + "/";
            System.out.println(yearPath);
            List<String> temp = insertFile(yearPath, ".doc");
            if (temp != null) {
                files.addAll(temp);
            }
        }
        return files;
    }

    public List<String> insertFile(String basePath, String end){
        List<String> patientFileList = new ArrayList<>();
        File fileDir = new File(basePath);
        File[] files = fileDir.listFiles((dir, name) -> name.endsWith(end));

        if (!fileDir.exists()) {
            System.out.println("folder " + basePath + " does not exist.");
            return null;
        }
        if (!fileDir.isDirectory()) {
            System.out.println(basePath + " is not a directory.");
            return null;
        }

        if (files != null) {
            for (File file : files) {
                // Process each PDF file here
                patientFileList.add("inserted " + end + "file " + save(file).getFilename());
                System.out.println(" file: " + file.getAbsolutePath());
            }
        }
        return patientFileList;
    }

    public String getLatestFile(String patientID) {
        try {
            Patient patient = patientRepository.getById(Long.parseLong(patientID));

            if (patient.getFiles().isEmpty()) {
                return "patient with ID {} has no files.".formatted(patientID);
            }
            try {
                return getPath(fileRepository.getTopByPatientOrderByVisitDateDesc(patient));

//                return Collections.max(patient.getFiles());
            } catch (Exception e) {
                return "patient with ID {} has files, but not found.".formatted(patientID);
            }
        } catch (Exception e) {
            return "patient with ID {} not found.".formatted(patientID);
        }
    }
}
