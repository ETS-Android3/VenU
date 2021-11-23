package com.example.venu.models;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JSONPlaceholder {

    @GET("events.json?apikey=AWF87vjxgIFWapa37mJcNS88i8dfh2Qk")
    Call<JSONResponse> getEvent();
}
