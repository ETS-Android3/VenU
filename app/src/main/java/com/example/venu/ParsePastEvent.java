package com.example.venu;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseObject;


@ParseClassName("Event")
public class ParsePastEvent extends ParseObject {
    public static final String KEY_TICKETMASTER_ID = "ticketmaster_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_VENUE = "venue";
    public static final String KEY_CITY = "city";
    public static final String KEY_DATE = "date";
    public static final String KEY_ATTENDEES = "attendees";
    public static final String KEY_IMAGE = "bannerURL";

    public String getTicketmasterId() {
        return getString(KEY_TICKETMASTER_ID);
    }

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public String getVenue() {
        return getString(KEY_VENUE);
    }

    public String getCity() {
        return getString(KEY_CITY);
    }

    public String getDate() {
        return getString(KEY_DATE);
    }

    public ParseObject getAttendees() {
        return getParseObject(KEY_ATTENDEES);
    }

    public String getImage() {
        return getString(KEY_IMAGE);
    }

    public void setTicketmasterId(String ticketmaster_id){
        put(KEY_TICKETMASTER_ID, ticketmaster_id);
    }

    public void setTitle(String title){
        put(KEY_TITLE, title);
    }

    public void setVenue(String venue){
        put(KEY_VENUE, venue);
    }

    public void setCity(String city){
        put(KEY_CITY, city);
    }

    public void setDate(String date){
        put(KEY_DATE, date);
    }

    public void setAttendees(String[] attendees){
        put(KEY_ATTENDEES, attendees);
    }

    public void setImage(String image_url){
        put(KEY_IMAGE, image_url);
    }
}