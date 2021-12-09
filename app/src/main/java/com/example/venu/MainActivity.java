package com.example.venu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.example.venu.fragments.MainFeedFragment;
import com.example.venu.fragments.ProfileFragment;
import com.example.venu.fragments.SearchFragment;
import com.example.venu.models.Event;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final int LOGIN_ACTIVITY_REQUEST_CODE = 11;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        Log.d(TAG, "action_home pressed in bottom navigation bar");
                        fragment = new MainFeedFragment();
                        break;
                    case R.id.action_search:
                        Log.d(TAG, "action_search pressed in bottom navigation bar");
                        fragment = new SearchFragment();
                        break;
                    case R.id.action_profile:
                        Log.i(TAG, "action_profile pressed in bottom navigation bar");
                        fragment = new ProfileFragment();
                        // goProfileActivity();
                        break;
                    case R.id.action_logout:
                        Log.i(TAG, "action_logout pressed in bottom navigation bar");
                        ParseUser.logOut();
                        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                        Intent intentl = new Intent(MainActivity.this, LoginActivity.class);
                        startActivityForResult(intentl, LOGIN_ACTIVITY_REQUEST_CODE);
                        return true;
                    default:
                        fragment = new MainFeedFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        fragmentManager.beginTransaction()
                .replace(R.id.flContainer, new MainFeedFragment())
                .addToBackStack(null)
                .commit();

    }
}