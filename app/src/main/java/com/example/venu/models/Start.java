package com.example.venu.models;

public class Start {
    private String localDate;
    private String localTime;
    private String dateTime;

    public Start(String localDate, String localTime, String dateTime) {
        this.localDate = localDate;
        this.localTime = localTime;
        this.dateTime = dateTime;
    }

    public String getLocalDate() {
        return localDate;
    }

    public String getLocalTime() {
        return localTime;
    }

    public String getDateTime() {
        return dateTime;
    }

}
