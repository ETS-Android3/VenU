package com.example.venu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    List<Event> eventList;
    Context context;

    public EventAdapter(Context context, List<Event> event){
        this.context = context;
        eventList = event;
        for(Event temp_event: eventList){
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
        Event event = eventList.get(position);
        holder.title.setText(event.getTitle());
        holder.venue.setText(event.getVenue());
        holder.date.setText(event.getDate());
        holder.price.setText(event.getPrice());

    }

    @Override
    public int getItemCount() {
        return eventList.size();
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
