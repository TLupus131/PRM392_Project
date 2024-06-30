package com.example.bookingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookingapp.R;
import com.example.bookingapp.activity.ReservationManagerActivity;
import com.example.bookingapp.entity.Region;
import com.example.bookingapp.entity.Reservation;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ReservationAdapter extends BaseAdapter {
    private final Context context;
    private final List<Reservation> list;

    public ReservationAdapter(Context context, List<Reservation> list) {
        this.context = context;
        this.list = list;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_reservation_item, parent, false);
            holder = new ViewHolder();
            holder.imageViewProperty = convertView.findViewById(R.id.imageViewProperty2);
            holder.tvPropertyName = convertView.findViewById(R.id.tvPropertyName2);
            holder.tvTotalPrice = convertView.findViewById(R.id.tvTotalPrice2);
            holder.tvReservationDate = convertView.findViewById(R.id.tvReservationDate2);
            holder.toReservationDetail = convertView.findViewById(R.id.tvToReservationDetail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Reservation reservation = list.get(position);
        if (reservation != null) {
            NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            double price = reservation.getProperty().getPrice();

            SimpleDateFormat dateFormat = new SimpleDateFormat("d", new Locale("vi", "VN"));
            SimpleDateFormat monthFormat = new SimpleDateFormat("M", new Locale("vi", "VN"));

            String startDay = dateFormat.format(reservation.getCheckInDate().getTime());
            String startMonth = monthFormat.format(reservation.getCheckInDate().getTime());

            String endDay = dateFormat.format(reservation.getCheckOutDate().getTime());
            String endMonth = monthFormat.format(reservation.getCheckOutDate().getTime());
            String formattedCheckInDate = startDay + " thg " + startMonth;
            String formattedCheckOutDate = endDay + " thg " + endMonth;

            Picasso.get().load(reservation.getProperty().getListImage().get(0)).into(holder.imageViewProperty);
            holder.tvPropertyName.setText(reservation.getProperty().getName());
            holder.tvTotalPrice.setText(vndFormat.format(price));
            holder.tvReservationDate.setText(formattedCheckInDate + " - " + formattedCheckOutDate);
            holder.toReservationDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ReservationManagerActivity.class);
                    intent.putExtra("reservation", reservation);
                    intent.putExtra("date", holder.tvReservationDate.getText().toString());
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView imageViewProperty;
        TextView tvPropertyName, tvTotalPrice, tvReservationDate, toReservationDetail;
    }
}
