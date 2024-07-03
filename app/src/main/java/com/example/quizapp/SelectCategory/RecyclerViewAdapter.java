package com.example.quizapp.SelectCategory;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;


import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<CategoryInfo> categoryList;
    private SelectCategory selectCategory;
    private ViewHolder viewHolder;

    public RecyclerViewAdapter(ArrayList<CategoryInfo> categoryList, SelectCategory selectCategory) {
        this.categoryList = categoryList;
        this.selectCategory = selectCategory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorylistview, parent, false);
        viewHolder = new ViewHolder(itemView);
        viewHolder.categoryName.setMinHeight((int) (parent.getHeight() * 0.2));
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void setOnItemClick(View view, int position) {
                selectCategory.startQuizActivity(position);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.categoryName.setText(categoryList.get(position).getName());

        if (position % 6 == 0) {
            holder.categoryName.setBackgroundColor(Color.parseColor("#90CAF9"));
        } else if (position % 6 == 1) {
            holder.categoryName.setBackgroundColor(Color.parseColor("#FFCC80"));
        } else if (position % 6 == 2) {
            holder.categoryName.setBackgroundColor(Color.parseColor("#A5D6A7"));
        } else if (position % 6 == 3) {
            holder.categoryName.setBackgroundColor(Color.parseColor("#FFAB91"));
        } else if (position % 6 == 4) {
            holder.categoryName.setBackgroundColor(Color.parseColor("#B39DDB"));
        } else {
            holder.categoryName.setBackgroundColor(Color.parseColor("#FFD180"));
        }

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
