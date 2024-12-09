package com.clinic.patientDB.service;

import com.clinic.patientDB.model.ExtractedFileInfo;
import com.clinic.patientDB.model.Patient;
import com.clinic.patientDB.model.Visit;
import com.clinic.patientDB.repository.PatientRepository;
import com.clinic.patientDB.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Visit save(String fileName) {
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
            visit.addPatientPdfFile(extractFileInfo.getUpdateDateAndTime(), extractFileInfo.getActionType(), fileName);
        } else if (filetype.contains("doc")) {
            visit.addPatientDocFile(extractFileInfo.getActionType(), fileName);
        } else {
            System.out.println("file type" + filetype + "is not supported, file " + fileName + "not saved");
        }
        System.out.println("filename: {}" + fileName + "\n\tpatient: " + patient.getName() + "\n\tvisit patient: " + visit.getPatient().getName());

//        visitRepository.save(visit);
        patientRepository.save(patient);
        return visit;

    }

//
//        Matcher matcher = pattern.matcher(fileName);
//
//        if (matcher.find()) {
//            List<String> extractedFileAttributes = new ArrayList<>();
//            int groupCount = matcher.groupCount();
//            String fileType = matcher.group(groupCount);
//            if (fileType.contains("doc")) {
//                extractedFileAttributes.add(null);
//            }
//            extractedFileAttributes.add(matcher.group());
    ////            int visitDatePlace = 1;
    ////            int namePlace = 2;
    ////            int idOrPassportPlace = 3;
    ////
    ////            if (fileType.contains("pdf")) {
    ////                visitDatePlace = 2;
    ////                namePlace = 3;
    ////                idOrPassportPlace = 4;
    ////            }
//
//            // Extracting groups
//            String updateDateAndTimeStr = extractedFileAttributes.get(0); //matcher.group(1); // Optional: {updateDate updateTime}
//            String visitDateStr = extractedFileAttributes.get(1);// matcher.group(visitDatePlace);        // Mandatory: visitDate
//            String name = extractedFileAttributes.get(2);            // Mandatory: name (may include underscores)
//            String idString = extractedFileAttributes.get(3); //matcher.group(idOrPassportPlace);    // Mandatory: ID or passport
//
//            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyy");
//            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyy HHmm");
//            String[] extractedId = extractId(idString);
//            String idType = extractedId[0];
//            Long id = Long.parseLong(extractedId[1]);
//            String actionType = extractedId[2];




//
//                if (updateDateAndTimeStr != null) {
//                    // Replace multiple spaces and parse updateDateAndTime
//                    updateDateAndTimeStr = updateDateAndTimeStr.replaceAll("\\s+", " ");
//                    visit.addPatientPdfFile(LocalDateTime.parse(updateDateAndTimeStr, dateTimeFormatter), actionType, fileName);
//                } else{
//                    System.out.println("filetype is pdf but no update dates");
//                }
//            }
//            patientRepository.save(patient);
//            return visit;
//        }
//        System.out.println("Patient not found");
//        return null;


//    public PatientFile save(String patientFileName, String fileType) {
//        // 1. פירוק המחרוזת
//        String[] cut = patientFileName.split("_");
//        if (fileType.isEmpty()) {
//            fileType = patientFileName.split("\\.")[1];
//        }
//        String patientID = cut[cut.length - 1].replace(fileType, "");
//        String name = String.join(" ", Arrays.copyOfRange(cut, 1, cut.length - 1));
//        String dateAndTime = cut[0];
//        Long id = Patient.getIdFromString(patientID);
//
//        // 2. מציאת המטופל
//        Patient patient = repository.findById(id).orElse(new Patient(name, patientID));
//
//        // 3. יצירת הקובץ
//        PatientFile newFile = new PatientFile(dateAndTime, patient, fileType);
//        newFile.setFilename(patientFileName);
//
//        // 4. הוספת הקובץ לרשימה במיקום המתאים
//        if (patient.getFiles() == null) {
//            patient.setFiles(new ArrayList<>());
//        }
//        insertFileInOrder(patient.getFiles(), newFile);
//
//        // 5. שמירת המטופל
//        repository.save(patient);
//        return newFile;
//    }

//    private void insertFileInOrder(List<PatientFile> files, PatientFile newFile) {
//        for (int i = 0; i < files.size(); i++) {
//            if (newFile.getFileType() == ".pdf"){
//                if (newFile.getUpdateDateAndTime().compareTo(files.get(i).getDate()) > 0) {
//                    files.add(i, newFile); // הכנסת הקובץ בעמדה המתאימה
//                    return;
//                }
//        }
//        files.add(newFile); // אם הקובץ הכי פחות מעודכן, הוסף לסוף
//    }

//    public Patient save(Patient patient) {
//
//        return repository.save(patient);
//    }

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
            String yearPath =  "%s/%s/".formatted(basePath, i);
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
                Visit visit = save(file.getName());
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
