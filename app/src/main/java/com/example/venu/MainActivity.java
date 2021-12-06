package com.example.venu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.venu.adapters.EventAdapter;
import com.example.venu.models.Event;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "MainActivity";
    public static final String BASE_EVENTS_URL = "https://app.ticketmaster.com/discovery/v2/events?apikey=7elxdku9GGG5k8j0Xm8KWdANDgecHMV0&locale=*";
    public static final String BASE_CATEGORY_PARAM = "&classificationName=";
    private String url = BASE_EVENTS_URL;
    private int selected;
    private Spinner spCategories;
    private RecyclerView rvEvents;
    private BottomNavigationView bottomNavigationView;
    public static final int LOGIN_ACTIVITY_REQUEST_CODE = 11;
    public static final int PROFILE_ACTIVITY_REQUEST_CODE = 22;
    List<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvEvents = findViewById(R.id.rvEvents);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                switch (menuItem.getItemId()) {
                    case R.id.action_search:
                        Log.d(TAG, "Search menu pressed");
                        break;
                    case R.id.action_profile:
                        goProfileActivity();
                        return true;
                    case R.id.action_logout:
                        ParseUser.logOut();
                        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                        Intent intentl = new Intent(MainActivity.this, LoginActivity.class);
                        startActivityForResult(intentl, LOGIN_ACTIVITY_REQUEST_CODE);
                        return true;
                    default:
                        break;
                }
                return true;
            }
        });

        spCategories = findViewById(R.id.spCategories);

        List<String> categories = new ArrayList<String>();
        categories.add("Sports");
        categories.add("Music");
        categories.add("Film");

        if (getIntent().hasExtra("selection")) {
            selected = getIntent().getIntExtra("selection", 0);
            url = BASE_EVENTS_URL + BASE_CATEGORY_PARAM + categories.get(selected);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spCategories.setAdapter(dataAdapter);
        spCategories.setSelection(selected, false);
        spCategories.setOnItemSelectedListener(this);

        events = new ArrayList<>();

        // create the adapter
        EventAdapter eventAdapter = new EventAdapter(this, events);
        // set the adapter to the recyclerView
        rvEvents.setAdapter(eventAdapter);
        // set the layout manager on the recyclerView
        rvEvents.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        Log.i(TAG, "About to request from " + url);
        client.get(url, new JsonHttpResponseHandler() {
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
                Log.e(TAG, "onFailure ERROR: "+response, throwable);
            }
        });


    }

    private void goProfileActivity() {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        //Toast.makeText(adapterView.getContext(), "Selected " + item, Toast.LENGTH_LONG).show();
        Log.i(TAG, "Spinner item " + item + " selected");
        //spCategories.setSelection(0, false);
        //this.recreate();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("selection", i);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}