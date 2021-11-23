package com.example.venu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.venu.models.Event;
import com.example.venu.models.JSONPlaceholder;
import com.example.venu.models.JSONResponse;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    RecyclerView rvEventPreviews;
    List<Event> events;
    EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://app.ticketmaster.com/discovery/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        // Init the list of tweets and adapter
        events = new ArrayList<>();
        adapter = new EventAdapter(this, events);

        rvEventPreviews = findViewById(R.id.rvEventPreviews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvEventPreviews.setLayoutManager(layoutManager);
        rvEventPreviews.setAdapter(adapter);

        JSONPlaceholder jsonPlaceholder = retrofit.create(JSONPlaceholder.class);
        Call<JSONResponse> call = jsonPlaceholder.getEvent();
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                JSONResponse jsonResponse = response.body();
                events = (Arrays.asList(jsonResponse.getEventsArray().getEvents()));
                adapter.clear();
                adapter.addAll(events);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }
        });
    }
}