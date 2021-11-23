package com.example.venu.models;

import com.example.venu.models.Event;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JSONResponse {
    @SerializedName("_embedded")
    @Expose
    private Events eventsArray;

    public JSONResponse(Events eventsArray) {
        this.eventsArray = eventsArray;
    }

    public Events getEventsArray(){
        return eventsArray;
    }

    public void setEventsArray(Events eventsArray){
        this.eventsArray = eventsArray;
    }
}
