package com.example.venu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.venu.models.Event;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.List;

public class EventDetailActivity extends AppCompatActivity {

    public static final String TAG = "EventDetailActivity";
    TextView tvTitleDetail;
    TextView tvVenueDetail;
    TextView tvCityDetail;
    TextView tvDateDetail;
    TextView tvPriceRange;
    ImageView ivBanner;
    Button btnPurchase;
    Button btnAttend;
    ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        currentUser = ParseUser.getCurrentUser();

        tvTitleDetail = findViewById(R.id.tvTitleDetail);
        tvVenueDetail = findViewById(R.id.tvVenueDetail);
        tvCityDetail = findViewById(R.id.tvCityDetail);
        tvDateDetail = findViewById(R.id.tvDateDetail);
        tvPriceRange = findViewById(R.id.tvPriceRange);
        ivBanner = findViewById(R.id.ivBanner);
        btnPurchase = findViewById(R.id.btnPurchase);
        btnAttend = findViewById(R.id.btnAttend);

        Event event = Parcels.unwrap(getIntent().getParcelableExtra("event"));
        String eventId = event.getId();

        try {
            set_attend_button(btnAttend, eventId);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(!event.getTitle().isEmpty()){tvTitleDetail.setText(event.getTitle());}
        if(!event.getVenue_name().isEmpty()){tvVenueDetail.setText(event.getVenue_name());}
        if(!event.getVenue_city().isEmpty()){tvCityDetail.setText(event.getVenue_city()+", "+event.getVenue_state_abv());}
        if(!event.getDate().isEmpty()){tvDateDetail.setText(event.getDate());}
        tvPriceRange.setText("$"+event.getMin_price()+" - $"+event.getMax_price());
        Glide.with(this).load(event.getLargest_image_url()).into(ivBanner);

        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getEvent_url()));
                startActivity(browserIntent);
            }
        });

        btnAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String past_event_id = get_past_event_id(event.getId());
                    if(past_event_id == null){
                        // create new event and add it to user's past events
                        Log.i(TAG, "Event with id "+event.getId()+" not found. Create new event");

                        // TODO: edit Event object in back4app to reflect usage and fill with data accordingly
                        ParsePastEvent new_event = new ParsePastEvent();
                        new_event.setName(event.getTitle());
                        new_event.setKeyEventId(event.getId());
                        new_event.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e != null){
                                    Log.e(TAG, "Error while creating new event", e);
                                }
                                Log.i(TAG, "Event created successfully.");
                                currentUser.add("pastevents", new_event.getObjectId());
                                try {
                                    currentUser.save();
                                } catch (ParseException parseException) {
                                    parseException.printStackTrace();
                                }
                                btnAttend.setEnabled(false);
                            }
                        });
                    }else{
                        // add existing event to user's past events
                        Log.i(TAG, "Event with id "+event.getId()+" found: "+past_event_id);
                        currentUser.add("pastevents", past_event_id);
                        try {
                            currentUser.save();
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                        btnAttend.setEnabled(false);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String get_past_event_id(String eventId) throws ParseException {
        String past_event_id = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.whereEqualTo("eventID",eventId);
        query.setLimit(1);

        List<ParseObject> query_results = query.find();
        if (query_results.size() == 0){
            return null;
        }
        for(ParseObject result : query_results){
            Log.i(TAG, result.getObjectId());
            past_event_id = result.getObjectId();
        }
        return past_event_id;
    }

    private void set_attend_button(Button btnAttend,String eventId) throws ParseException {
        Object events_attended = currentUser.get("pastevents"); // Past Event object references grabbed from current user
        String past_event_id = get_past_event_id(eventId);  // Object ID of the event we are currently viewing (null if not in database)
        List<String> objectIDs = (List<String>) events_attended; // events_attended in List<String> form
        // set button to enabled if eventId is not in user's pastevents
        if(past_event_id == null || !objectIDs.contains(past_event_id)){
            btnAttend.setEnabled(true);
            return;
        }
        // set button to disabled if eventId already appears in user's pastevents
        btnAttend.setEnabled(false);
        return;
    }
}