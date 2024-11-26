package com.clinic.patientDB.service;

import java.io.File;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collection;

import com.clinic.patientDB.model.Patient;

public class dataBaseService {
    private Collection<Patient> patients = new ArrayList<>();
    private Collection<String> pdf = new ArrayList<>();
    private Collection<String> word = new ArrayList<>();

//    public GenerateDataBase() {
//    }


    public Collection<Patient> getPatients() {
        return patients;
    }

    public void setPatients(Collection<Patient> patients) {
        this.patients = patients;
    }

    public Collection<String> getPdf() {
        return pdf;
    }

    public void setPdf(Collection<String> pdf) {
        this.pdf = pdf;
    }

    public void addPdf(String pdf) {
        this.pdf.add(pdf);
    }

    public Collection<String> getWord() {
        return word;
    }

    public void setWord(Collection<String> word) {
        this.word = word;
    }
    public void addWord(String word) {
        this.word.add(word);
    }

    //todo יצירת בסיס נתונים, חלוקה לפי וורד וpdf
    //todo שמירת בסיס נתונים postgres??

    String wordFile="080218_herzog_laura_011599834.doc";
    String docFile="{080218 1326} 080218_herzog_laura_joann_011599834.pdf";

    public void getPatientFromWord(String fileName) {
        addWord(fileName);

    }

    public void getPatientFromPDF(String fileName) {

        addPdf(fileName);
    }


}
