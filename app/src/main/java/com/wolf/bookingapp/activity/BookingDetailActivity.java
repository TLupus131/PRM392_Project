package com.wolf.bookingapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.wolf.bookingapp.R;
import com.wolf.bookingapp.entity.Property;
import com.squareup.picasso.Picasso;

public class BookingDetailActivity extends AppCompatActivity {

    private String checkInDate, checkOutDate, price;
    private Property property;
    private TextView tvReservationDetail, tvReservationDate, tvTotalPrice, tvPropertyName;
    private ImageButton btnClose;
    private ImageView imageView;
    private Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        property = (Property) getIntent().getSerializableExtra("property");
        checkInDate = getIntent().getStringExtra("checkInDate");
        checkOutDate = getIntent().getStringExtra("checkOutDate");
        price = getIntent().getStringExtra("price");
        id = getIntent().getIntExtra("reservationId", 0);

        imageView = findViewById(R.id.imageViewProperty);
        btnClose = findViewById(R.id.btnClose2);
        tvReservationDetail = findViewById(R.id.tvReservationDetail);
        tvReservationDate = findViewById(R.id.tvReservationDate);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvPropertyName = findViewById(R.id.tvPropertyName);

        Picasso.get().load(property.getListImage().get(0)).into(imageView);
        tvPropertyName.setText(property.getName());
        tvTotalPrice.setText(price);
        tvReservationDate.setText(checkInDate + " - " + checkOutDate);

        tvReservationDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingDetailActivity.this, ReservationManagerActivity.class);
                intent.putExtra("reservationId", id);
                intent.putExtra("date", checkInDate + " - " + checkOutDate);
                startActivity(intent);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Cannot roll back!", Toast.LENGTH_SHORT).show();
    }
}