package com.example.bookingapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookingapp.R;
import com.example.bookingapp.activity.ui.stay.StayFragment;
import com.example.bookingapp.entity.Property;
import com.example.bookingapp.entity.Region;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RegionAdapter extends BaseAdapter {

    private final Context context;
    private final List<Region> list;
    private final Button button;
    private final Dialog dialog;
    private final TextView latitude, longitude;

    public RegionAdapter(Context context, List<Region> list, Button button, Dialog dialog, TextView latitude, TextView longitude) {
        this.context = context;
        this.list = list;
        this.button = button;
        this.dialog = dialog;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_region_item, parent, false);
            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.tvRegionName);
            holder.tvDescription = convertView.findViewById(R.id.tvRegionDescription);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Region region = list.get(position);
        if (region != null) {
            holder.tvName.setText(region.getName());
            holder.tvDescription.setText(region.getDescription());
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (region != null) {
                    button.setText(region.getName());
                    latitude.setText(region.getLatitude());
                    longitude.setText(region.getLongitude());
                }
                dialog.cancel();
            }
        });
        return convertView;
    }


    static class ViewHolder {
        TextView tvName, tvDescription;
    }
}
