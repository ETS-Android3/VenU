package com.example.venu.models;

public class Venues {
    private String name;
    private String postalCode;

    public Venues(String name, String postalCode) {
        this.name = name;
        this.postalCode = postalCode;
    }

    public String getVenueName() {
        return name;
    }

    public String getPostalCode() {
        return postalCode;
    }
}
