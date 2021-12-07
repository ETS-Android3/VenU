package com.example.venu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.venu.adapters.BadgeAdapter;
import com.example.venu.adapters.FriendAdapter;
import com.example.venu.models.Badge;
import com.example.venu.models.Friend;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class OthersActivity extends AppCompatActivity {
    public static final String TAG = "OtherActivity";
    public static final int LOGIN_ACTIVITY_REQUEST_CODE = 11;
    private BottomNavigationView bottomNavigationView;
    ImageView ivProfilePicture;
    TextView tvBio;
    TextView tvUsername;
    TextView tvNumEvents;
    List<Badge> badges;
    List<Friend> friends;
    Button btnEditProfile;
    String username;
    Button btnAddFriend;
    Boolean friended = Boolean.FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvBio = findViewById(R.id.tvBio);
        tvUsername = findViewById(R.id.tvUsername);
        ivProfilePicture = findViewById(R.id.ivProfilePic);
        tvNumEvents = findViewById(R.id.tvNumShows);
        btnEditProfile = findViewById(R.id.btnEdit);
        btnAddFriend = findViewById(R.id.btnAdd);
        username = getIntent().getStringExtra("username");

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
                        Intent intentl = new Intent(OthersActivity.this, LoginActivity.class);
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
        query.whereEqualTo(Profile.KEY_USERNAME, username);
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> profiles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "issue with getting profile", e);
                    return;
                }
                for (ParseUser profile : profiles) {
                    if (profile.get("profilepicture") != null ){
                        Glide.with(OthersActivity.this).load(profile.getParseFile("profilepicture").getUrl()).into(ivProfilePicture);
                    }
                    tvUsername.setText(profile.getUsername());
                    tvBio.setText(String.valueOf(profile.get("bio")));
                    List<String> pastEvents = (List<String>) profile.get("pastevents");
                    tvNumEvents.setText("They have been to "+ pastEvents.size()+ " events!");
                    showFriends(profile.get("friends"));
                    showBadges(profile.get("badges"));
                    List<String> friendsList = (List<String>) profile.get("friends");
                    friended = friendsList.contains(ParseUser.getCurrentUser().getObjectId());
                    Log.i(TAG, String.valueOf(friended));
                    if (friended == Boolean.FALSE){
                        btnAddFriend.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "EditProfile login button");
                goEdit();
            }
        });
    }

    private void goEdit() {
        Intent i = new Intent(this, EditProfileActivity.class);
        startActivity(i);
        finish();
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void showFriends(Object friendArray){
        RecyclerView rvFriends = findViewById(R.id.rvFriends);
        friends = new ArrayList<>();
        // create the adapter
        FriendAdapter friendAdapter = new FriendAdapter(this, friends);
        // set the adapter to the recyclerView
        rvFriends.setAdapter(friendAdapter);
        // set the layout manager on the recyclerView
        rvFriends.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<String> objectIDs = (List<String>) friendArray;
        ParseQuery<ParseObject> friendParseQuery = ParseQuery.getQuery("_User");
        friendParseQuery.whereContainedIn("objectId", objectIDs);
        friendParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void done(List<ParseObject> friendsList, ParseException e) {
                if (e != null){
                    Log.e(TAG, "issue with getting homies", e);
                    return;
                }
                for (ParseObject friendObject : friendsList){
                    friends.add(new Friend(friendObject));
                }
                friendAdapter.notifyDataSetChanged();
            }
        });
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
