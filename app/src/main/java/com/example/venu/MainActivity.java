package com.example.venu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.venu.adapters.EventAdapter;
import com.example.venu.models.Event;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final String BASE_EVENTS_URL = "https://app.ticketmaster.com/discovery/v2/events?apikey=7elxdku9GGG5k8j0Xm8KWdANDgecHMV0&locale=*";
    private BottomNavigationView bottomNavigationView;
    List<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvEvents = findViewById(R.id.rvEvents);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                switch (menuItem.getItemId()) {
                    case R.id.action_search:
                        Log.d(TAG, "Search menu pressed");
                        break;
                    case R.id.action_profile:
                        Log.d(TAG, "Search profile pressed");
                        break;
                    case R.id.action_logout:
                        Log.d(TAG, "logout button pressed");
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        events = new ArrayList<>();

        // create the adapter
        EventAdapter eventAdapter = new EventAdapter(this, events);
        // set the adapter to the recyclerView
        rvEvents.setAdapter(eventAdapter);
        // set the layout manager on the recyclerView
        rvEvents.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(BASE_EVENTS_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    // root of the ticketmaster json response
                    JSONObject embedded_from_json = jsonObject.getJSONObject("_embedded");
                    // events array containing all necessary info
                    JSONArray events_from_json = embedded_from_json.getJSONArray("events");
                    events.addAll(Event.getEventList(events_from_json));
                    eventAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Events: "+events.size());
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Exception upon retrieving events", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure ERROR", throwable);
            }
        });


    }
}