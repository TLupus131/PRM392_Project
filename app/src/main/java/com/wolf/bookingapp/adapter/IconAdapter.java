package com.wolf.bookingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wolf.bookingapp.R;

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconViewHolder> {
    private Context context;
    private int iconCount;

    public IconAdapter(Context context, int iconCount) {
        this.context = context;
        this.iconCount = iconCount;
    }

    @NonNull
    @Override
    public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_icon, parent, false);
        return new IconViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return iconCount;
    }

    public static class IconViewHolder extends RecyclerView.ViewHolder {
        public IconViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

