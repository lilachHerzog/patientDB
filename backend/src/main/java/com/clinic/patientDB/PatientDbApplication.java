package com.clinic.patientDB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.clinic.patientDB.model")
//@EnableJpaRepositories(basePackages = "com.clinic.patientDB.jwt")
//@EntityScan(basePackages = "com.clinic.patientDB.jwt")
public class PatientDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatientDbApplication.class, args);
    }

}


//    TODO frontend
// todo dashboard
