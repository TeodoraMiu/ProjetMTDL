package com.example.hopitaux;

public class Announcement {
    private String title;
    private String bloodType;
    private String reason;
    private String description;
    private String hospital;

    public Announcement(String title, String bloodType, String reason, String description, String hospital) {
        this.title = title;
        this.bloodType = bloodType;
        this.reason = reason;
        this.description = description;
        this.hospital = hospital;
    }

    public String getTitle() {
        return title;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getReason() {
        return reason;
    }

    public String getDescription() {
        return description;
    }

    public String getHospital() {
        return hospital;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
