package com.clinic.patientDB.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name="patient")
public class Patient implements Serializable {
    @Id
    private Long id;
    private String name;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private Collection<PatientFile> patientFiles = new ArrayList<>();

    public Patient() {

    }

    public Collection<PatientFile> getFiles() {
        return patientFiles;
    }
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }




    public void setFiles(Collection<PatientFile> patientFiles) {
        this.patientFiles = patientFiles;
    }

    public void addPatientFile(PatientFile patientFile) {
        patientFile.setPatient(this);
        this.patientFiles.add(patientFile)
        ;
    }

    public Patient(String name, long idNumber) {
        this.name = name;
        this.id = idNumber;
    }

}

