package com.example.venu.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.venu.R;
import com.example.venu.adapters.EventAdapter;
import com.example.venu.models.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFeedFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    public static final String TAG = "MainFeedFragment";
    public static final String BASE_EVENTS_URL = "https://app.ticketmaster.com/discovery/v2/events?apikey=7elxdku9GGG5k8j0Xm8KWdANDgecHMV0&locale=*";
    public static final String BASE_CATEGORY_PARAM = "&classificationName=";
    private String url = BASE_EVENTS_URL;
    private int selected;
    private Spinner spCategories;
    private RecyclerView rvEvents;
    final FragmentManager fragmentManager = getFragmentManager();
    List<Event> events;

    public interface MainFeedFragmentListener{
        void OnEventClick(Parcelable wrapped_event);
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFeed.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFeedFragment newInstance(String param1, String param2) {
        MainFeedFragment fragment = new MainFeedFragment();
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
        return inflater.inflate(R.layout.fragment_main_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvEvents = view.findViewById(R.id.rvEvents);
        spCategories = view.findViewById(R.id.spCategories);

        spCategories = view.findViewById(R.id.spCategories);
        spCategories.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Sports");
        categories.add("Music");
        categories.add("Film");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategories.setAdapter(dataAdapter);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        //Toast.makeText(adapterView.getContext(), "Selected " + item, Toast.LENGTH_LONG).show();
        Log.i(TAG, "Spinner item " + item + " selected");
        queryEvents(item);
    }

    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void queryEvents(String category){
        events = new ArrayList<>();
        String url;
        if (category == ""){
            url = BASE_EVENTS_URL;
        }else{
            url = BASE_EVENTS_URL + BASE_CATEGORY_PARAM + category;
        }

        // create the adapter
        EventAdapter eventAdapter = new EventAdapter(getContext(), events);
        // set the adapter to the recyclerView
        rvEvents.setAdapter(eventAdapter);
        // set the layout manager on the recyclerView
        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));

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
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Exception upon retrieving events", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure of API call: "+response, throwable);
            }
        });
    }

}