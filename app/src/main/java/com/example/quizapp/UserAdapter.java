package com.example.quizapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quizapp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<DocumentSnapshot> userList;

    public UserAdapter(List<DocumentSnapshot> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboardlistview, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        DocumentSnapshot document = userList.get(position);
        String userName = document.getString("name");
        Long userScore = document.getLong("score");
        String url = document.getString("URL");


        holder.userNameTextView.setText(userName);
        holder.userScoreTextView.setText(userScore + " QP");
        // Use Glide to load the image into the avatar ImageView
        Glide.with(holder.avatar.getContext())
                .load(url)
                .placeholder(R.drawable.loading) // optional placeholder// optional error drawable
                .into(holder.avatar);


        int listNumber = position + 4;
        holder.listNoTextview.setText(String.valueOf(listNumber));
    }

    @Override
    public int getItemCount() {
        int limit = 6;
        return Math.min(userList.size(), limit);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;
        TextView userScoreTextView;
        TextView listNoTextview;
        ImageView avatar;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.leaderName);
            userScoreTextView = itemView.findViewById(R.id.leaderScore);
            listNoTextview = itemView.findViewById(R.id.listNo);
            avatar=itemView.findViewById(R.id.leaderAvatar);
        }
    }
}
