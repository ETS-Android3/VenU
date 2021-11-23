package com.example.venu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.venu.models.Event;
import com.example.venu.models.Venues;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    List<Event> events;
    Context context;

    public EventAdapter(Context context, List<Event> event){
        this.context = context;
        events = event;
        for(Event temp_event: events){
            Log.i("EventAdapter", temp_event.toString());
        }
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_preview, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.title.setText(event.getTitle());
        Venues venue = event.getEmbedded().getVenues()[0];
        holder.venue.setText(venue.getVenueName());
        holder.date.setText(event.getDates().getStart().getLocalDate());
        // holder.price.setText(event.getPrice());

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void clear(){
        events.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Event> eventList){
        events.addAll(eventList);
        notifyDataSetChanged();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{
        TextView title, venue, date, price, previewImage;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvTitle);
            venue = itemView.findViewById(R.id.tvVenue);
            date = itemView.findViewById(R.id.tvDate);
            price = itemView.findViewById(R.id.tvPrice);
        }
    }
}
