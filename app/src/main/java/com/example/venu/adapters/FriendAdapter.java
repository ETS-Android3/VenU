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
import com.example.venu.models.Friend;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>{

    Context context;
    List<Friend> friends;

    public FriendAdapter(Context context, List<Friend> friends) {
        this.context = context;
        this.friends = friends;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View eventPreviewView = LayoutInflater.from(context).inflate(R.layout.activity_friend, parent, false);
        return new ViewHolder(eventPreviewView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend friend = friends.get(position);
        holder.bind(friend);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvFUsername;
        ImageView ivFriendPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFUsername = itemView.findViewById(R.id.tvFUsername);
            ivFriendPicture = itemView.findViewById(R.id.ivFriendPicture);
        }

        public void bind(Friend friend) {
            tvFUsername.setText(friend.getName());
            Glide.with(context).load(friend.getfriendPhotoUrl()).override(50, 50).centerCrop().into(ivFriendPicture);
        }
    }
}
