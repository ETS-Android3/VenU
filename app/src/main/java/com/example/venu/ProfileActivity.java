package com.example.venu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.venu.adapters.BadgeAdapter;
import com.example.venu.models.Badge;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    public static final String TAG = "ProfileActivity";
    private Context context;
    public static final int LOGIN_ACTIVITY_REQUEST_CODE = 11;
    public static final int MAIN_ACTIVITY_REQUEST_CODE = 33;
    private BottomNavigationView bottomNavigationView;
    ImageView ivProfilePicture;
    TextView tvBio;
    TextView tvUsername;
    TextView tvNumEvents;
    List<Badge> badges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvBio = findViewById(R.id.tvBio);
        tvUsername = findViewById(R.id.tvUsername);
        ivProfilePicture = findViewById(R.id.ivProfilePic);
        tvNumEvents = findViewById(R.id.tvNumShows);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                switch (menuItem.getItemId()) {
                    case R.id.action_search:
                        goMainActivity();
                        return true;
                    case R.id.action_profile:
                        Log.i(TAG, "ProfileActivity pressed");
                        break;
                    case R.id.action_logout:
                        ParseUser.logOut();
                        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                        Intent intentl = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivityForResult(intentl, LOGIN_ACTIVITY_REQUEST_CODE);
                        return true;
                    default:
                        break;
                }
                return true;
            }
        });


        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.include(Profile.KEY_USERNAME);
        query.whereEqualTo(Profile.KEY_USERNAME, ParseUser.getCurrentUser().getUsername());
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> profiles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "issue with getting profile", e);
                    return;
                }
                for (ParseUser profile : profiles) {
                    Glide.with(ProfileActivity.this).load(profile.getParseFile("profilepicture").getUrl()).into(ivProfilePicture);
                    tvUsername.setText(profile.getUsername());
                    tvBio.setText(String.valueOf(profile.get("bio")));
                    List<String> pastEvents = (List<String>) profile.get("pastevents");
                    tvNumEvents.setText("You have been to "+ pastEvents.size()+ " events!");
                    showBadges(profile.get("badges"));
                }
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void showBadges(Object badgeArray){
        RecyclerView rvBadges = findViewById(R.id.rvBadges);
        badges = new ArrayList<>();
        // create the adapter
        BadgeAdapter badgeAdapter = new BadgeAdapter(this, badges);
        // set the adapter to the recyclerView
        rvBadges.setAdapter(badgeAdapter);
        // set the layout manager on the recyclerView
        rvBadges.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<String> objectIDs = (List<String>) badgeArray;
        ParseQuery<ParseObject> badgeParseQuery = ParseQuery.getQuery("Badges");
        badgeParseQuery.whereContainedIn("objectId", objectIDs);
        badgeParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void done(List<ParseObject> badgeList, ParseException e) {
                if (e != null){
                    Log.e(TAG, "issue with getting badges", e);
                    return;
                }
                for (ParseObject badgeObject : badgeList){
                    badges.add(new Badge(badgeObject));
                }
                badgeAdapter.notifyDataSetChanged();
            }
        });
    }
}
