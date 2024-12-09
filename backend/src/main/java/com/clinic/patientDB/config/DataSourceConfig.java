//package com.clinic.patientDB.config;
//
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class DataSourceConfig {
//
//    @Bean
//    public DataSource dataSource() {
//        return DataSourceBuilder.create()
//                .url("jdbc:h2:file:./data/patientDB;DB_CLOSE_ON_EXIT=FALSE")
//                .username("sa")
//                .password("")
//                .driverClassName("org.h2.Driver")
//                .build();
//    }
//}
//
