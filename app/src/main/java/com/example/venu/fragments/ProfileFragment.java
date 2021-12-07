package com.example.venu.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.venu.Profile;
import com.example.venu.R;
import com.example.venu.adapters.BadgeAdapter;
import com.example.venu.adapters.FriendAdapter;
import com.example.venu.models.Badge;
import com.example.venu.models.Friend;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    public static final int LOGIN_ACTIVITY_REQUEST_CODE = 11;
    ImageView ivProfilePicture;
    TextView tvBio;
    TextView tvUsername;
    TextView tvNumEvents;
    TextView tvFollowers;
    List<Badge> badges;
    List<Friend> friends;
    Button btnEditProfile;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        tvBio = v.findViewById(R.id.tvBio);
        tvUsername = v.findViewById(R.id.tvUsername);
        ivProfilePicture = v.findViewById(R.id.ivProfilePic);
        tvNumEvents = v.findViewById(R.id.tvNumShows);
        tvFollowers = v.findViewById(R.id.tvFollowers);
        btnEditProfile = v.findViewById(R.id.btnAdd);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                    if (profile.get("profilepicture") != null ){
                        System.out.println("URL SHOULD BE " + profile.getParseFile("profilepicture").getUrl());
                        Glide.with(getContext()).load(profile.getParseFile("profilepicture").getUrl()).into(ivProfilePicture);
                    }
                    tvUsername.setText(profile.getUsername());
                    tvBio.setText(String.valueOf(profile.get("bio")));
                    List<String> pastEvents = (List<String>) profile.get("pastevents");
                    List<String> friends = (List<String>) profile.get("friends");
                    tvNumEvents.setText(String.valueOf(pastEvents.size()));
                    tvFollowers.setText(String.valueOf(friends.size()));
                    showFriends(profile.get("friends"), view);
                    showBadges(profile.get("badges"), view);
                }
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment nextFrag= new ProfileEditFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContainer, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
    private void showBadges(Object badgeArray, View view){
        if(badgeArray == null){return;}
        RecyclerView rvBadges = view.findViewById(R.id.rvBadges);
        badges = new ArrayList<>();
        // create the adapter
        BadgeAdapter badgeAdapter = new BadgeAdapter(getContext(), badges);
        // set the adapter to the recyclerView
        rvBadges.setAdapter(badgeAdapter);
        // set the layout manager on the recyclerView
        rvBadges.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
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
    private void showFriends(Object friendArray, View view){
        if(friendArray == null){return;}    // do not load friends if friendsArray is null
        RecyclerView rvFriends = view.findViewById(R.id.rvFriends);
        friends = new ArrayList<>();
        // create the adapter
        FriendAdapter friendAdapter = new FriendAdapter(getContext(), friends);
        // set the adapter to the recyclerView
        rvFriends.setAdapter(friendAdapter);
        // set the layout manager on the recyclerView
        rvFriends.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
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
}