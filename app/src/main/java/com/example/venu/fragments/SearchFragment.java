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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.venu.Profile;
import com.example.venu.R;
import com.example.venu.adapters.EventAdapter;
import com.example.venu.adapters.FriendAdapter;
import com.example.venu.models.Event;
import com.example.venu.models.Friend;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    public static final String TAG = "SearchFragment";
    public static final String BASE_EVENTS_URL = "https://app.ticketmaster.com/discovery/v2/events?apikey=7elxdku9GGG5k8j0Xm8KWdANDgecHMV0&locale=*";
    EditText etSearch;
    TextView tvUsers;
    TextView tvEvents;
    ImageButton btnSearch;
    RecyclerView rvUserSearchResults;
    RecyclerView rvEventSearchResults;
    List<Event> events;
    List<Friend> friends;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        etSearch = v.findViewById(R.id.etSearch);
        btnSearch = v.findViewById(R.id.btnSearch);
        rvUserSearchResults = v.findViewById(R.id.rvUserSearchResults);
        rvEventSearchResults = v.findViewById(R.id.rvEventSearchResults);
        tvUsers = v.findViewById(R.id.textView);
        tvEvents = v.findViewById(R.id.textView2);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // make result elements invisible on load
        tvEvents.setVisibility(view.GONE);
        tvUsers.setVisibility(view.GONE);
        rvUserSearchResults.setVisibility(view.GONE);
        rvEventSearchResults.setVisibility(view.GONE);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_users(v, etSearch.getText().toString());
                search_events(v, etSearch.getText().toString());
            }
        });
    }

    public void search_events(View v, String keywords){
        keywords.replaceAll("\\s", "%20");
        String url = BASE_EVENTS_URL+"&keyword="+keywords;
        Log.d(TAG, "url is "+url+"keywords is "+keywords);

        events = new ArrayList<>();

        // create the adapter
        EventAdapter eventAdapter = new EventAdapter(getContext(), events);
        // set the adapter to the recyclerView
        rvEventSearchResults.setAdapter(eventAdapter);
        // set the layout manager on the recyclerView
        rvEventSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess of API call");
                JSONObject jsonObject = json.jsonObject;
                try {
                    // root of the ticketmaster json response
                    JSONObject embedded_from_json = jsonObject.getJSONObject("_embedded");
                    // events array containing all necessary info
                    JSONArray events_from_json = embedded_from_json.getJSONArray("events");
                    events.addAll(Event.getEventList(events_from_json));
                    eventAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Events: "+events.size());
                    if(events.size()==0){
                        //Toast.makeText(getContext(), "No Events Found", Toast.LENGTH_SHORT).show();
                        tvEvents.setVisibility(v.GONE);
                        rvEventSearchResults.setVisibility(v.GONE);
                    }else{
                        tvEvents.setVisibility(v.VISIBLE);
                        rvEventSearchResults.setVisibility(v.VISIBLE);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Exception upon retrieving events", e);
                    //Toast.makeText(getContext(), "No Events Found", Toast.LENGTH_SHORT).show();
                    tvEvents.setVisibility(v.GONE);
                    rvEventSearchResults.setVisibility(v.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure of API call: "+response, throwable);
            }
        });
    }
    public void search_users(View v, String keywords){
        friends = new ArrayList<>();

        // create the adapter
        FriendAdapter friendAdapter = new FriendAdapter(getContext(), friends);
        // set the adapter to the recyclerView
        rvUserSearchResults.setAdapter(friendAdapter);
        // set the layout manager on the recyclerView
        rvUserSearchResults.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.include(Profile.KEY_USERNAME);
        query.whereMatches(Profile.KEY_USERNAME, keywords);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e != null){
                    Log.e(TAG, "issue with getting homies", e);
                    return;
                }
                for (ParseObject user : users){
                    friends.add(new Friend(user));
                }
                friendAdapter.notifyDataSetChanged();
                if(friends.size()==0){
                    //Toast.makeText(getContext(), "No Users Found", Toast.LENGTH_SHORT).show();
                    tvUsers.setVisibility(v.GONE);
                    rvUserSearchResults.setVisibility(v.GONE);
                }else {
                    tvUsers.setVisibility(v.VISIBLE);
                    rvUserSearchResults.setVisibility(v.VISIBLE);
                }
            }
        });
    }
}