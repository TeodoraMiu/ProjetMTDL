package com.example.hopitaux;

public final class Donor {
    private static Donor instance;

    private String name;
    private String email;
    private String address;
    private String city;
    private String age;
    private String bloodType;
    private String selectedAnnouncementTitle;

    public Donor() {

    }

    public static Donor getInstance() {
        if (instance == null) {
            instance = new Donor();
        }
        return instance;
    }

    public static void setInstance(Donor instance) {
        Donor.instance = instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelectedAnnouncementTitle() {
        return selectedAnnouncementTitle;
    }

    public void setSelectedAnnouncementTitle(String selectedAnnouncementTitle) {
        this.selectedAnnouncementTitle = selectedAnnouncementTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
}
