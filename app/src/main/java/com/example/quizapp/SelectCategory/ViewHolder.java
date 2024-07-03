package com.example.quizapp.SelectCategory;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView categoryName;
    private ClickListener mClickListener;

    public ViewHolder(View mView) {
        super(mView);
        categoryName = mView.findViewById(R.id.categoryName);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null) {
                    mClickListener.setOnItemClick(view, getAdapterPosition());
                }
            }
        });
    }

    public interface ClickListener {
        void setOnItemClick(View view, int position);
    }

    public void setOnClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
