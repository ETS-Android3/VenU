package com.example.venu.models;

import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("name")
    private String title;
    private Dates dates;
    private PriceRanges[] priceRanges;
    @SerializedName("_embedded")
    private Embedded embedded;

    public Event(String title, Dates dates, PriceRanges[] priceRanges, Embedded embedded) {
        this.title = title;
        this.dates = dates;
        this.priceRanges = priceRanges;
        this.embedded = embedded;
    }

    public String getTitle() {
        return title;
    }

    public Dates getDates() {
        return dates;
    }

    public PriceRanges[] getPriceRanges() {
        return priceRanges;
    }

    public Embedded getEmbedded() {
        return embedded;
    }
}
