package com.example.venu.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Event {

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
    String preview_image_url;
    String banner_image_url;
    String largest_image_url;
    String primary_classification;
    String genre;

    public Event(JSONObject jsonObject) throws JSONException {

        DecimalFormat df = new DecimalFormat("0.00");

        // get elements from root of json before diving deeper
        title = jsonObject.getString("name");
        id = jsonObject.getString("id");
        event_url = jsonObject.getString("url");

        // retrieve image JSONArray to find objects containing image info
        JSONArray images_from_json = jsonObject.getJSONArray("images");
        preview_image_url = images_from_json.getJSONObject(0).getString("url"); //default if 4:3 not found
        for (int i = 0; i < images_from_json.length(); i++){
            JSONObject obj = images_from_json.getJSONObject(i);
            if (obj.getString("ratio").equals("4_3")){
                preview_image_url = images_from_json.getJSONObject(i).getString("url"); // 4:3 ratio
                break;
            }
        }
        banner_image_url = images_from_json.getJSONObject(1).getString("url");  // default if 16:9 not found
        for (int i = 0; i < images_from_json.length(); i++){
            JSONObject obj = images_from_json.getJSONObject(i);
            if (obj.getString("ratio").equals("16_9")){
                preview_image_url = images_from_json.getJSONObject(i).getString("url"); // 16:9 ratio
                break;
            }
        }
        int max_height = 0;
        for (int i = 0; i < images_from_json.length(); i++){
            JSONObject obj = images_from_json.getJSONObject(i);
            if (obj.getInt("height") > max_height){
                largest_image_url = images_from_json.getJSONObject(i).getString("url");
                max_height = obj.getInt("height");
            }
        }

        JSONArray prices_from_json = jsonObject.getJSONArray("priceRanges");
        float min_price_float = Float.parseFloat(prices_from_json.getJSONObject(0).getString("min"));
        float max_price_float = Float.parseFloat(prices_from_json.getJSONObject(0).getString("max"));
        min_price = df.format(min_price_float);
        max_price = df.format(max_price_float);

        JSONArray classifications_from_json = jsonObject.getJSONArray("classifications");
        primary_classification = classifications_from_json.getJSONObject(0).getJSONObject("segment").getString("name");
        genre = classifications_from_json.getJSONObject(0).getJSONObject("genre").getString("name");

        date = jsonObject.getJSONObject("dates").getJSONObject("start").getString("localDate");

        JSONArray venues_from_json = jsonObject.getJSONObject("_embedded").getJSONArray("venues");
        venue_name = venues_from_json.getJSONObject(0).getString("name");
        venue_postalCode = venues_from_json.getJSONObject(0).getString("postalCode");
        venue_city = venues_from_json.getJSONObject(0).getJSONObject("city").getString("name");
        venue_state_abv = venues_from_json.getJSONObject(0).getJSONObject("state").getString("stateCode");

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

    public String getPreview_image_url() {
        return preview_image_url;
    }

    public String getBanner_image_url() {
        return banner_image_url;
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
