package com.example.venu;

import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(ParsePastEvent.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("7DAhQZa1UPjaPcYiWPpfsPkZLaIp5MdQN8V5PTHX")
                .clientKey("x5K6p2gfZpQkjGSkyD5yQQyiWYAxVMcwq8a1zoJ3")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}