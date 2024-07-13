package com.wolf.bookingapp.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wolf.bookingapp.R;
import com.wolf.bookingapp.adapter.IconAdapter;
import com.wolf.bookingapp.config.IPConfig;
import com.wolf.bookingapp.config.ReservationAPI;
import com.wolf.bookingapp.entity.Property;
import com.wolf.bookingapp.entity.User;
import com.wolf.bookingapp.entity.UserManager;
import com.wolf.bookingapp.response.ReservationResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookingConfirmActivity extends AppCompatActivity {
    private TextView property_name, check_in_date, check_out_date, total_price, final_price, room_and_customer_num;
    private RecyclerView recyclerView;
    private ImageButton btnRollback;
    private String totalPrice, finalPrice, firstname, lastname, phone, email, nationality;
    private Date checkInDate, checkOutDate;
    private int room, adult, children, days;
    private Property property;
    private Button btnMakeReservation;
    private User authUser = null;
    private ProgressBar progressBar;
    private boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirm);
        authUser = UserManager.getInstance().getAuthUser();
        property = (Property) getIntent().getSerializableExtra("property");
        room = getIntent().getIntExtra("room", 0);
        adult = getIntent().getIntExtra("adult", 0);
        children = getIntent().getIntExtra("children", 0);
        checkInDate = (Date) getIntent().getSerializableExtra("checkInDate");
        checkOutDate = (Date) getIntent().getSerializableExtra("checkOutDate");
        totalPrice = getIntent().getStringExtra("totalPrice");
        finalPrice = getIntent().getStringExtra("finalPrice");
        firstname = getIntent().getStringExtra("firstname");
        lastname = getIntent().getStringExtra("lastname");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        nationality = getIntent().getStringExtra("nationality");

        btnMakeReservation = findViewById(R.id.btnConfirm);
        property_name = findViewById(R.id.property_name2);
        recyclerView = findViewById(R.id.recycler_view_star_icons2);
        btnRollback = findViewById(R.id.btnRollback3);
        check_in_date = findViewById(R.id.checkInDate2);
        check_out_date = findViewById(R.id.checkOutDate2);
        total_price = findViewById(R.id.totalPrice2);
        final_price = findViewById(R.id.finalPrice2);
        room_and_customer_num = findViewById(R.id.roomAndCustomerNumber2);
        progressBar = findViewById(R.id.progressBarConfirm);

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(checkInDate);
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(checkOutDate);

        SimpleDateFormat dayFormat = new SimpleDateFormat("E", new Locale("vi", "VN"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("d", new Locale("vi", "VN"));
        SimpleDateFormat monthFormat = new SimpleDateFormat("M", new Locale("vi", "VN"));

        String sd = dayFormat.format(startDate.getTime());
        String startDay = dateFormat.format(startDate.getTime());
        String startMonth = monthFormat.format(startDate.getTime());

        String ed = dayFormat.format(endDate.getTime());
        String endDay = dateFormat.format(endDate.getTime());
        String endMonth = monthFormat.format(endDate.getTime());
        String formattedCheckInDate = sd + ", " + startDay + " thg " + startMonth;
        String formattedCheckOutDate = ed + ", " + endDay + " thg " + endMonth;

        long startTime = startDate.getTimeInMillis();
        long endTime = endDate.getTimeInMillis();
        long diffTime = endTime - startTime;
        days = (int) (diffTime / (1000 * 60 * 60 * 24));

        total_price.setText(totalPrice);
        total_price.setPaintFlags(total_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        final_price.setText(finalPrice);
        room_and_customer_num.setText(room + " phòng/căn, " + adult + " người lớn, " + children + " trẻ em");
        check_in_date.setText(formattedCheckInDate);
        check_out_date.setText(formattedCheckOutDate);
        property_name.setText(property.getName());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new IconAdapter(this, property.getStar()));

        btnRollback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new VerifyProperty().execute();

        btnMakeReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status) {
                    makeReservation();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BookingConfirmActivity.this);
                    builder.setMessage("Xin lỗi quý khách hiện đã hết phòng trống!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();                }
            }
        });
    }

    private class VerifyProperty extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL("http://" + IPConfig.IPv4 + ":8080/api/properties/verify?propertyId=" + property.getId() + "&checkInDate=" + checkInDate + "&checkOutDate=" + checkOutDate + "&room=" + room + "&adult=" + adult + "&children=" + children + "&petAllow=" + property.isPetsAllowed());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
            } catch (MalformedURLException e) {
                throw new RuntimeException();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            if (s != null && !s.isEmpty()) {
                status = true;
            }
        }
    }

    private void makeReservation() {
        double price = 0;
        Locale locale = new Locale("vi", "VN");
        NumberFormat vndFormat = NumberFormat.getCurrencyInstance(locale);
        try {
            Number number = vndFormat.parse(finalPrice);
            price = number.doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ReservationResponse reservationResponse = new ReservationResponse(authUser.getId(), property.getId(), firstname, lastname, email, nationality, phone, room, String.valueOf(checkInDate), String.valueOf(checkOutDate), price, adult, children, days);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + IPConfig.IPv4 + ":8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ReservationAPI reservationAPI = retrofit.create(ReservationAPI.class);
        Call<ReservationResponse> call = reservationAPI.add(reservationResponse);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<ReservationResponse>() {
            @Override
            public void onResponse(Call<ReservationResponse> call, Response<ReservationResponse> response) {
                if (response.isSuccessful()) {
                    ReservationResponse reservationResponse = response.body();

                    AlertDialog.Builder builder = new AlertDialog.Builder(BookingConfirmActivity.this);
                    builder.setMessage("Reservation added successfully! ID: " + reservationResponse.getId())
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(BookingConfirmActivity.this, BookingDetailActivity.class);
                                    intent.putExtra("property", property);
                                    intent.putExtra("checkInDate", check_in_date.getText().toString());
                                    intent.putExtra("checkOutDate", check_out_date.getText().toString());
                                    intent.putExtra("price", finalPrice);
                                    intent.putExtra("reservationId", reservationResponse.getId());
                                    progressBar.setVisibility(View.GONE);
                                    startActivity(intent);
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BookingConfirmActivity.this);
                    builder.setMessage("Failed to add reservation")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<ReservationResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingConfirmActivity.this);
                builder.setMessage("Failed to add reservation: " + t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}