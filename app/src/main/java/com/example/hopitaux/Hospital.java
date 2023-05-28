package com.example.hopitaux;

public final class Hospital {
    private static Hospital instance;

    private String name;
    private String address;
    private String email;
    private String selectedAnnouncement;

    public static Hospital getInstance() {
        if (instance == null) {
            instance = new Hospital();
        }
        return instance;
    }

    public Hospital() {
    }

    public static void setInstance(Hospital instance) {
        Hospital.instance = instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSelectedAnnouncement() {
        return selectedAnnouncement;
    }

    public void setSelectedAnnouncement(String selectedAnnouncement) {
        this.selectedAnnouncement = selectedAnnouncement;
    }
}
