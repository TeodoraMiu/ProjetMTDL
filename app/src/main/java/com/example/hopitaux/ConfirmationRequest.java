package com.example.hopitaux;

public class ConfirmationRequest {
    private String candidate;
    private String confirmed;
    private String day;
    private String hour;
    private String hospital;
    private String title;

    public ConfirmationRequest(String candidate, String confirmed, String day, String hour, String hospital, String title) {
        this.candidate = candidate;
        this.confirmed = confirmed;
        this.day = day;
        this.hour = hour;
        this.hospital = hospital;
        this.title = title;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
