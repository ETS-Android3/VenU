package com.example.venu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.venu.models.Event;

import org.parceler.Parcels;

public class EventDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Event event = Parcels.unwrap(getIntent().getParcelableExtra("event"));
    }
}