package com.clinic.patientDB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.clinic.patientDB.model")
public class PatientDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatientDbApplication.class, args);
    }

}
