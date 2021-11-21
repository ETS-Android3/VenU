package com.example.venu;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;


@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_EMAIL = "email";


    public ParseUser getUser() {
        return getParseUser(KEY_EMAIL);

    }

    public void setUser(ParseUser user){
        put(KEY_EMAIL, user);
    }
}