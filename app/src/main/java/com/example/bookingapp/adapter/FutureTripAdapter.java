package com.example.bookingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookingapp.R;
import com.example.bookingapp.entity.FutureTrip;

import java.util.List;

public class FutureTripAdapter extends ArrayAdapter<FutureTrip> {

    private Context mContext;

    public FutureTripAdapter(Context context, List<FutureTrip> trips) {
        super(context, 0, trips);
        mContext = context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.lv_future_trip_item, parent, false);
        }
        FutureTrip trip = getItem(position);
        TextView itemName = listItem.findViewById(R.id.itemName);
        TextView itemDate = listItem.findViewById(R.id.itemDate);
        TextView itemStatus = listItem.findViewById(R.id.itemStatus);

        itemName.setText(trip.getName());
        itemDate.setText(trip.getDate());
        itemStatus.setText(trip.getStatus());

        return listItem;
    }
}

