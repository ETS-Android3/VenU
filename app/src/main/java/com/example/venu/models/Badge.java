package com.example.venu.models;

import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Badge {
    String name;
    String badgeIcon;
    public Badge(ParseObject badge) {
        name = badge.get("Name").toString();
        badgeIcon = badge.getParseFile("Image").getUrl();
    }
    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return badgeIcon;
    }

/*    public static List<Badge> getBadgeList(ParseObject objectArray) {
        List<Badge> badges = new ArrayList<>();
        for(int i = 0; i < objectArray.length(); i++){
            badges.add(new Badge(eventJsonArray.getJSONObject(i)));
        }
        return events;
    }*/
}
