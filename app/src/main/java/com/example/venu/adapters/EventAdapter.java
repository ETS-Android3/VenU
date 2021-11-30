package com.example.venu.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.venu.R;
import com.example.venu.models.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{

    Context context;
    List<Event> events;

    public EventAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View eventPreviewView = LayoutInflater.from(context).inflate(R.layout.event_preview, parent, false);
        return new ViewHolder(eventPreviewView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvVenue;
        TextView tvPrice;
        TextView tvDate;
        TextView tvCity;
        ImageView ivPhotoPreview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvVenue = itemView.findViewById(R.id.tvVenue);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvCity = itemView.findViewById(R.id.tvCity);
            ivPhotoPreview = itemView.findViewById(R.id.ivPhotoPreview);
        }

        public void bind(Event event) {
            tvTitle.setText(event.getTitle());
            tvVenue.setText(event.getVenue_name());
            tvPrice.setText("$"+event.getMin_price());
            tvDate.setText(event.getDate());
            tvCity.setText(event.getVenue_city()+", "+event.getVenue_state_abv());
            Glide.with(context).load(event.getPreview_image_url()).into(ivPhotoPreview);
        }
    }
}
