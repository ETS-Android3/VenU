package com.example.venu.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Parcel
public class Event {

    public static final String TAG = "Event";

    String title;
    String id;
    String event_url;
    String venue_name;
    String venue_city;
    String venue_state_abv;
    String venue_postalCode;
    String min_price;
    String max_price;
    String date;
    String largest_image_url;
    String primary_classification;
    String genre;

    public Event(){
    }

    public Event(JSONObject jsonObject) throws JSONException {

        DecimalFormat df = new DecimalFormat("0.00");

        // get elements from root of json before diving deeper
        title = jsonObject.getString("name");
        id = jsonObject.getString("id");
        event_url = jsonObject.getString("url");

        // retrieve image JSONArray to find objects containing image info
        JSONArray images_from_json = jsonObject.getJSONArray("images");
        int max_height = 0;
        for (int i = 0; i < images_from_json.length(); i++){
            JSONObject obj = images_from_json.getJSONObject(i);
            if (obj.getInt("height") > max_height){
                largest_image_url = images_from_json.getJSONObject(i).getString("url");
                max_height = obj.getInt("height");
            }
        }

        try{
            JSONArray prices_from_json = jsonObject.getJSONArray("priceRanges");
            float min_price_float = Float.parseFloat(prices_from_json.getJSONObject(0).getString("min"));
            float max_price_float = Float.parseFloat(prices_from_json.getJSONObject(0).getString("max"));
            min_price = df.format(min_price_float);
            max_price = df.format(max_price_float);
        }catch(JSONException e){
            min_price = "";
            max_price = "";
        }

        JSONArray classifications_from_json = jsonObject.getJSONArray("classifications");

        try{
            primary_classification = classifications_from_json.getJSONObject(0).getJSONObject("segment").getString("name");
        }catch(JSONException e){
            primary_classification = "";
        }
        try{
            genre = classifications_from_json.getJSONObject(0).getJSONObject("genre").getString("name");
        }catch(JSONException e){
            genre = "";
        }


        date = jsonObject.getJSONObject("dates").getJSONObject("start").getString("localDate");

        JSONArray venues_from_json = jsonObject.getJSONObject("_embedded").getJSONArray("venues");
        venue_name = venues_from_json.getJSONObject(0).getString("name");
        try{
        venue_postalCode = venues_from_json.getJSONObject(0).getString("postalCode");
        }
        catch (JSONException e){
            venue_postalCode = "";
        }
        venue_city = venues_from_json.getJSONObject(0).getJSONObject("city").getString("name");
        try {
            venue_state_abv = venues_from_json.getJSONObject(0).getJSONObject("state").getString("stateCode");
        }
        catch (JSONException e){
            venue_state_abv = "";
        }

    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getEvent_url() {
        return event_url;
    }

    public String getVenue_name() {
        return venue_name;
    }

    public String getVenue_city() {
        return venue_city;
    }

    public String getVenue_state_abv() {
        return venue_state_abv;
    }

    public String getVenue_postalCode() {
        return venue_postalCode;
    }

    public String getMin_price() {
        return min_price;
    }

    public String getMax_price() {
        return max_price;
    }

    public String getDate() {
        return date;
    }

    public String getLargest_image_url() { return largest_image_url; }

    public String getPrimary_classification() {
        return primary_classification;
    }

    public String getGenre() {
        return genre;
    }

    public static List<Event> getEventList(JSONArray eventJsonArray) throws JSONException {
        List<Event> events = new ArrayList<>();
        for(int i = 0; i < eventJsonArray.length(); i++){
            events.add(new Event(eventJsonArray.getJSONObject(i)));
        }
        return events;
    }
}
