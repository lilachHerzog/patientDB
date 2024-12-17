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
    String filePath;
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

    public String getFilePath() {
        return filePath;
    }
    public String getFileName() {
        return filePath.split("/")[filePath.split("/").length - 1];
    }
    public void setFilePath(String filename) {
        this.filePath = filename;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public PatientPdfFile(LocalDateTime updateDate, String filePath, Visit visit) {
        this.updateDate = updateDate;
        this.filePath = filePath;
        this.visit = visit;
    }

    public PatientPdfFile() {
    }

    public PatientPdfFile(LocalDateTime updateDate, String visitType, String filePath, Visit visit) {
        this.updateDate = updateDate;
        this.visitType = visitType;
        this.visit = visit;
        this.filePath = filePath;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PatientPdfFile that = (PatientPdfFile) obj;
        return Objects.equals(filePath, that.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath);
    }
}
