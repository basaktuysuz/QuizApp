package com.example.quizapp.Avatar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.quizapp.R;

import java.util.List;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder> {

    private Context context;
    private List<Avatar> avatarList;
    private OnItemClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public interface OnItemClickListener {
        void onItemClick(String imageUrl);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AvatarAdapter(Context context, List<Avatar> avatarList) {
        this.context = context;
        this.avatarList = avatarList;
    }

    @NonNull
    @Override
    public AvatarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.avatarlistview, parent, false);
        return new AvatarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarViewHolder holder, int position) {
        Avatar avatar = avatarList.get(position);
        Glide.with(context).load(avatar.getImageUrl()).placeholder(R.drawable.loading).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);


        if (selectedPosition == position) {
            holder.outlineView.setVisibility(View.VISIBLE);
        } else {
            holder.outlineView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return avatarList.size();
    }

    public class AvatarViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        View outlineView;

        public AvatarViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.avatarImageButton);
            outlineView = itemView.findViewById(R.id.outline_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(avatarList.get(position).getImageUrl());

                        // Update selected position and notify adapter
                        selectedPosition = position;
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }
}

