package com.clinic.patientDB.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="patientFIle")
public class PatientFile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private Integer visitDate;
    private Integer updateDate=null;
    private Integer updateTime=null;

    @JsonIgnore
//    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    public Integer getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Integer visitDate) {
        this.visitDate = visitDate;
    }

    public Integer getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Integer updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public PatientFile(String updateTime, Patient patient) {
        String[] time = updateTime.split(" ");
        Integer date = Integer.parseInt(time[time.length-1]); // שנה מ-Integer.getInteger ל- Integer.parseInt
        System.out.println(patient.getName()+ patient.getId()+ date.toString());

        this.visitDate = date;
        this.patient = patient;
        if (time.length>1) {
            this.updateTime = Integer.parseInt(time[1].replace("}", ""));
            this.updateDate = Integer.parseInt(time[0].replace("{", ""));

        }
    }



    public PatientFile() {
    }

    private class Times {
        private Integer visitDate;
        private Integer updateDate;
        private Integer updateTime;

        public Integer getVisitDate() {
            return visitDate;
        }

        public void setVisitDate(Integer visitDate) {
            this.visitDate = visitDate;
        }

        public Integer getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(Integer updateDate) {
            this.updateDate = updateDate;
        }

        public Integer getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Integer updateTime) {
            this.updateTime = updateTime;
        }
    }
}
