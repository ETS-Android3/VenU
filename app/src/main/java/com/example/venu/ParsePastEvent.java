package com.example.venu;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseObject;


@ParseClassName("Event")
public class ParsePastEvent extends ParseObject {
    public static final String KEY_NAME = "Name";
    public static final String KEY_COST = "Cost";
    public static final String KEY_EVENT_ID = "eventID";
    public static final String KEY_DESC = "Description";

    public String getName() {
        return getString(KEY_NAME);
    }

    public String getCost() {
        return getString(KEY_COST);
    }

    public String getEventId() {
        return getString(KEY_EVENT_ID);
    }

    public String getDesc() {
        return getString(KEY_DESC);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public void setCost(String cost) {
        put(KEY_COST, cost);
    }

    public void setKeyEventId(String eventId) {
        put(KEY_EVENT_ID, eventId);
    }

    public void setDesc(String desc) {
        put(KEY_DESC, desc);
    }

}