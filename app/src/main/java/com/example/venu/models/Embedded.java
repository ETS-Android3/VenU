package com.example.venu.models;

public class Embedded {
    private Venues[] venues;

    public Embedded(Venues[] venues) {
        this.venues = venues;
    }

    public Venues[] getVenues() {
        return venues;
    }
}
