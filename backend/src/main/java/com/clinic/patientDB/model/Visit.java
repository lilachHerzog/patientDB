package com.clinic.patientDB.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="visits")
public class Visit implements Serializable {
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL)
    private List<PatientPdfFile> patientPdfFile = new ArrayList<>();

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL)
    private List<PatientDocFile> patientDocFile = new ArrayList<>();

    public Visit() {
        this.visitDate = LocalDate.now();
//        this.patientPdfFile = new ArrayList<>();
//        this.patientDocFile = new ArrayList<>();
    }
    public Visit(Patient patient) {
        this.patient = patient;
        this.visitDate = LocalDate.now();
//        this.patientPdfFile = new ArrayList<>();
//        this.patientDocFile = new ArrayList<>();
    }

    public Visit(Patient patient, LocalDate visitDate) {
        this.patient = patient;
        this.visitDate = visitDate;
//        this.patientPdfFile = new ArrayList<>();
//        this.patientDocFile = new ArrayList<>();
    }


    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public List<PatientDocFile> getPatientDocFile() {
        return patientDocFile;
    }

    public void setPatientDocFile(List<PatientDocFile> patientDocFile) {
        this.patientDocFile = patientDocFile;
    }


    public PatientDocFile addPatientDocFile(PatientDocFile newDocFile) {
        if(!patientDocFile.contains(newDocFile)){
            patientDocFile.add(newDocFile);
        } else {
            System.out.println(newDocFile.filename + "file already exists");
        }
        return newDocFile;
    }
    public PatientDocFile addPatientDocFile(String visitType, String filename) {
        return addPatientDocFile(new PatientDocFile(visitType, filename, this));
    }


    public List<PatientPdfFile> getPatientPdfFile() {
        return patientPdfFile;
    }

    public void setPatientPdfFile(List<PatientPdfFile> patientPdfFile) {
        this.patientPdfFile = patientPdfFile;
    }

    public PatientPdfFile addPatientPdfFile(PatientPdfFile newPdfFile) {
        if(!patientPdfFile.contains(newPdfFile)){
            patientPdfFile.add(newPdfFile);
        }
        else {
            System.out.println(newPdfFile.filename + "file already exists");
        }
        return newPdfFile;
    }

    public PatientPdfFile addPatientPdfFile(String updateDateStr, String actionType, String filename) {
        LocalDateTime updateDate = LocalDateTime.parse(updateDateStr, ExtractedFileInfo.updateFormatter);
        return addPatientPdfFile(new PatientPdfFile(updateDate, actionType, filename, this));
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }


//    public String separateAdditiveDate(String date) {
//        Character identificationType = date.charAt(date.length() - 1);
//        if (Character.isLetter(identificationType)) {
//            setAdditives(identificationType);
//            date.substring(0, date.length() - 1);
//        }
//        return date;
//    }



    public LocalDate convertStringToDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        LocalDate time = LocalDate.parse(dateString, formatter);
        return time;

    }
    public LocalTime convertStringToTime(String timeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
        LocalTime time = LocalTime.parse(timeString, formatter);
        return time;
    }

    public LocalDateTime convertStringToDateAndTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("{HHmm ddMMyy}");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        return dateTime;
    }







}
