package com.clinic.patientDB.model;


import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="patients")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Patient implements Serializable {
    @Id
    private Long id;
    private String name;
    private String idType = null;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Visit> visits;





    public Patient() {
        this.id = null;
        this.visits = new ArrayList<>();
    }
    public Long getId() {
        return id;
    }

    public static Long getIdFromString(String id){
        Character identificationType = id.charAt(0);
        if (Character.isLetter(identificationType)){
            id.substring(1);
        }
        return Long.parseLong(id);
    }



    public void setId(String id) {

        Long LongId = getIdFromString(id);
//        if (LongId != Long.parseLong(id)){
//            this.idType = id.charAt(0);
//        }
        this.id = LongId ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




    public Patient(Long id, String name, String idType) {
        this.visits = new ArrayList<>();
        this.id = id;
        this.name = name;
        this.idType = idType;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }


    public Visit addVisit(Visit visit) {
        if(!visits.contains(visit)){
            visits.add(visit);
        }
        return visit;
    }

    public Visit addVisit(LocalDate updateDate) {
        return addVisit(new Visit(this, updateDate));
    }


    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }
}
