package com.example.venu.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Events {
    private Event[] events;

    public Events(Event[] events) {
        this.events = events;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
