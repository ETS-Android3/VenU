package com.example.venu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.venu.R;
import com.example.venu.models.Badge;

import java.util.List;

public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.ViewHolder>{

    Context context;
    List<Badge> badges;

    public BadgeAdapter(Context context, List<Badge> badges) {
        this.context = context;
        this.badges = badges;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View eventPreviewView = LayoutInflater.from(context).inflate(R.layout.activity_badge, parent, false);
        return new ViewHolder(eventPreviewView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Badge badge = badges.get(position);
        holder.bind(badge);
    }

    @Override
    public int getItemCount() {
        return badges.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvBadgeName;
        ImageView ivBadgeIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBadgeName = itemView.findViewById(R.id.tvBadgeName);
            ivBadgeIcon = itemView.findViewById(R.id.ivBadgeIcon);
        }

        public void bind(Badge badge) {
            tvBadgeName.setText(badge.getName());
            Glide.with(context).load(badge.getIconUrl()).override(50, 50).centerCrop().into(ivBadgeIcon);
        }
    }
}
