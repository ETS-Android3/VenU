package com.example.venu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.venu.adapters.BadgeAdapter;
import com.example.venu.adapters.FriendAdapter;
import com.example.venu.fragments.ProfileEditFragment;
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
    String username;
    Button btnAddFriend;
    Boolean friended = Boolean.FALSE;
    String OtherUserObjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        tvBio = findViewById(R.id.tvBioOther);
        tvUsername = findViewById(R.id.tvUsernameOther);
        ivProfilePicture = findViewById(R.id.ivProfilePicOther);
        tvNumEvents = findViewById(R.id.tvNumShowsOther);
        btnAddFriend = findViewById(R.id.btnAddOther);
        username = getIntent().getStringExtra("username");

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
                    OtherUserObjectId = profile.getObjectId();
                    tvUsername.setText(profile.getUsername());
                    tvBio.setText(String.valueOf(profile.get("bio")));
                    List<String> pastEvents = (List<String>) profile.get("pastevents");
                    tvNumEvents.setText("They have been to "+ pastEvents.size()+ " events!");
                    showFriends(profile.get("friends"));
                    showBadges(profile.get("badges"));
                    List<String> yourFriendsList = (List<String>) ParseUser.getCurrentUser().get("friends");
                    friended = yourFriendsList.contains(OtherUserObjectId);
                    // Log.i(TAG, String.valueOf(friended));
                    if (friended == Boolean.FALSE){
                        btnAddFriend.setEnabled(true);
                    }else{
                        btnAddFriend.setEnabled(false);
                    }
                }
            }
        });

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_friend();
            }
        });
    }

    private void showFriends(Object friendArray){
        RecyclerView rvFriends = findViewById(R.id.rvFriendsOther);
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
        RecyclerView rvBadges = findViewById(R.id.rvBadgesOther);
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

    public void add_friend(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.add("friends", OtherUserObjectId);
        try {
            currentUser.save();
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        btnAddFriend.setEnabled(false);
    }
}
