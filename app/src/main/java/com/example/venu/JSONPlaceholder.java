package com.example.venu;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JSONPlaceholder {

    @GET("events.json?apikey=AWF87vjxgIFWapa37mJcNS88i8dfh2Qk")
    Call<List<Event>> getEvent();
}
