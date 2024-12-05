package com.clinic.patientDB.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="patient_doc_files")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientDocFile  implements Serializable {
    @Id
    String filename;
    @ManyToOne(optional = false)
    @JoinColumn(name = "visit_id", nullable = false)
    private Visit visit;
    @JsonIgnore
    String visitType;

    public PatientDocFile() {
    }

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

    public PatientDocFile(String visitType, String filename, Visit visit) {
        this.visitType = visitType;
        this.filename = filename;
        this.visit = visit;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PatientDocFile that = (PatientDocFile) obj;
        return Objects.equals(filename, that.filename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename);
    }
}
