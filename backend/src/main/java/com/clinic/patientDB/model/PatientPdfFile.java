package com.clinic.patientDB.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="patient_pdf_files")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientPdfFile  implements Serializable {
    @Id
    String filename;
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "visit_id", nullable = false)
    private Visit visit;

    String visitType;
    LocalDateTime updateDate;


    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public PatientPdfFile(LocalDateTime updateDate, String filename, Visit visit) {
        this.updateDate = updateDate;
        this.filename = filename;
        this.visit = visit;
    }

    public PatientPdfFile() {
    }

    public PatientPdfFile(LocalDateTime updateDate, String visitType, String filename, Visit visit) {
        this.updateDate = updateDate;
        this.visitType = visitType;
        this.visit = visit;
        this.filename = filename;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PatientPdfFile that = (PatientPdfFile) obj;
        return Objects.equals(filename, that.filename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename);
    }
}
