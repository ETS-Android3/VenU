package com.example.venu;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;


@ParseClassName("Profile")
public class Profile extends ParseObject {
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_BADGES = "badges";
    public static final String KEY_BIO = "bio";
    public static final String KEY_PFP = "profilepicture";
    public static final String KEY_FOLLOWERS = "friends";

    public ParseUser getUsername(){
        return getParseUser(KEY_USERNAME);
    }

    public ParseUser getBadges(){
        return getParseUser(KEY_BADGES);
    }

    public ParseUser getBio(){
        return getParseUser(KEY_BIO);
    }

    public ParseUser getProfilePicture(){
        return getParseUser(KEY_PFP);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_EMAIL);
    }

    public void setProfilePicture(ParseUser user){
        put(KEY_PFP, user);
    }

    public void setBio(ParseUser user){
        put(KEY_BIO, user);
    }

    public void setUser(ParseUser user){
        put(KEY_EMAIL, user);
    }
}