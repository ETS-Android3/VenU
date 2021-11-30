package com.example.venu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class ProfileActivity extends AppCompatActivity {
    public static final String TAG = "ProfileActivity";
    public static final int LOGIN_ACTIVITY_REQUEST_CODE = 11;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_search:
                        Log.d(TAG, "Search menu pressed");
                        break;
                    case R.id.action_profile:
                        Log.d(TAG, "Profile pressed");
                        break;
                    case R.id.action_logout:
                        ParseUser.logOut();
                        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivityForResult(intent, LOGIN_ACTIVITY_REQUEST_CODE);
                        return true;
                    default:
                        break;
                }
                return true;
            }
        });

    }
}
