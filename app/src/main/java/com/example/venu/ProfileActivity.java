package com.example.venu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    public static final String TAG = "ProfileActivity";
    public static final int LOGIN_ACTIVITY_REQUEST_CODE = 11;
    public static final int MAIN_ACTIVITY_REQUEST_CODE = 33;
    private BottomNavigationView bottomNavigationView;
    ImageView ProfilePicture;
    TextView tvBio;
    TextView tvUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvBio = findViewById(R.id.tvBio);
        tvUsername = findViewById(R.id.tvUsername);

        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.include(Profile.KEY_USERNAME);
        query.whereEqualTo(Profile.KEY_USERNAME, ParseUser.getCurrentUser().getUsername());
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> profiles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "issue with getting posts", e);
                    return;
                }
                for (ParseUser profile : profiles) {
                    tvUsername.setText(profile.getUsername());
                    tvBio.setText(String.valueOf(profile.get("bio")));


                    Log.i(TAG, profile.getUsername() + String.valueOf(profile.get("bio")) + profile.getParseFile("profilepicture").getUrl());
                }
            }
        });
    }
}
