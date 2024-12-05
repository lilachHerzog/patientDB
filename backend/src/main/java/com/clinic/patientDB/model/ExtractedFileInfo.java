package com.clinic.patientDB.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractedFileInfo {
    public static String filenameRegex = "\\{?(\\d{6} \\d{4})?}?\\s?(\\d{6})(?:_([^_]+(?:_[^_]+)*))?_([A-Za-z])?(\\d+)([A-Za-z])?\\.(pdf|doc)";

    private LocalDateTime updateDateAndTime;
    private LocalDate visitDate;
    private String name;
    private String idType;
    private Long id;
    private String actionType;
    private String fileType;

    public static LocalDate extractVisitDate(String date) {
        DateTimeFormatter visitTimeFormatter = DateTimeFormatter.ofPattern("ddMMyy");
        return LocalDate.parse(date, visitTimeFormatter);
    }
    public static LocalDateTime extractUpdatetDate(String date) {
        if (date == null){
            return null;
        }
        DateTimeFormatter updateFormatter = DateTimeFormatter.ofPattern("ddMMyy HHmm");
        return LocalDateTime.parse(date, updateFormatter);
    }

    public ExtractedFileInfo(String filename) {
        Pattern pattern = Pattern.compile(filenameRegex);
        Matcher matcher = pattern.matcher(filename);

        if (matcher.matches()) {
            // יצירת אובייקט ושמירת הערכים
            this.updateDateAndTime =  extractUpdatetDate(matcher.group(1));
            this.visitDate = extractVisitDate(matcher.group(2));
            this.name = matcher.group(3);
            this.idType = matcher.group(4);
            this.id = Long.parseLong(matcher.group(5));
            this.actionType = matcher.group(6);
            this.fileType = matcher.group(7);
        } else {
            System.out.println(filename + " does not match the pattern.");
        }
    }

    public LocalDateTime getUpdateDateAndTime() {
        return updateDateAndTime;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public String getName() {
        return name;
    }

    public String getIdType() {
        return idType;
    }

    public Long getId() {
        return id;
    }

    public String getActionType() {
        return actionType;
    }

    public String getFileType() {
        return fileType;
    }
}

