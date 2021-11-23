package com.example.venu.models;

public class PriceRanges {
    private String type;
    private String currency;
    private float min;
    private float max;

    public PriceRanges(String type, String currency, float min, float max) {
        this.type = type;
        this.currency = currency;
        this.min = min;
        this.max = max;
    }

    public String getType() {
        return type;
    }

    public String getCurrency() {
        return currency;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }


}
