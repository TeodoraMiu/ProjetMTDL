package com.example.hopitaux;

public class Appointment {
    private String title;
    private String day;
    private String hour;
    private Double places;

    public Appointment(String title, String day, String hour, Double places) {
        this.title = title;
        this.day = day;
        this.hour = hour;
        this.places = places;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Double getPlaces() {
        return places;
    }

    public void setPlaces(Double places) {
        this.places = places;
    }
}
