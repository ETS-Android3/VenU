package com.example.venu.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.venu.EventDetailActivity;
import com.example.venu.R;
import com.example.venu.models.Event;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{

    Context context;
    List<Event> events;

    public interface EventDetailFragmentListener{
        void OnEventClick(Parcelable wrapped_event);
    }

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
        ImageView ivPhotoLargest;
        LinearLayout preview_container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvVenue = itemView.findViewById(R.id.tvVenue);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvCity = itemView.findViewById(R.id.tvCity);
            ivPhotoLargest = itemView.findViewById(R.id.ivPhotoLargest);
            preview_container = itemView.findViewById(R.id.preview_container);
        }

        public void bind(Event event) {
            if(!event.getTitle().isEmpty()){tvTitle.setText(event.getTitle());}
            if(!event.getVenue_name().isEmpty()){tvVenue.setText(event.getVenue_name());}
            if(!event.getMin_price().isEmpty()){tvPrice.setText("$"+event.getMin_price());}
            if(!event.getDate().isEmpty()){tvDate.setText(event.getDate());}
            if(!event.getVenue_city().isEmpty()){tvCity.setText(event.getVenue_city()+", "+event.getVenue_state_abv());}
            Glide.with(context).load(event.getLargest_image_url()).into(ivPhotoLargest);
            Glide.with(context)
                    .load(event.getLargest_image_url())
                    .circleCrop() // scale image to fill the entire ImageView
                    .transform(new RoundedCornersTransformation(10, 5))
                    .into(ivPhotoLargest);
            preview_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Parcelable wrapped_event = Parcels.wrap(event);
                    Intent i = new Intent(context, EventDetailActivity.class);
                    i.putExtra("event", wrapped_event);
                    context.startActivity(i);
                }
            });
        }
    }
}
