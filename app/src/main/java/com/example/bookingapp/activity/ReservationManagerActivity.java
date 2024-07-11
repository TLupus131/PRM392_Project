package com.example.bookingapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.R;
import com.example.bookingapp.adapter.IconAdapter;
import com.example.bookingapp.config.IPConfig;
import com.example.bookingapp.config.ReservationAPI;
import com.example.bookingapp.entity.Property;
import com.example.bookingapp.entity.Reservation;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReservationManagerActivity extends AppCompatActivity {
    private Reservation reservation;
    private String date;
    private TextView tvPropertyName, tvReservationDate, tvChangeDate, tvPropertyLocation;
    private Button btnCancelReservation;
    private ImageButton btnRollback;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Integer id;
    private Date currentCheckInDate, currentCheckOutDate, holderCheckInDate, holderCheckOutDate;
    private boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_manager);

        id = getIntent().getIntExtra("reservationId", 0);
        reservation = (Reservation) getIntent().getSerializableExtra("reservation");
        date = getIntent().getStringExtra("date");

        tvPropertyName = findViewById(R.id.tvPropertyName3);
        tvReservationDate = findViewById(R.id.tvReservationDate3);
        tvChangeDate = findViewById(R.id.tvChangeDate);
        tvPropertyLocation = findViewById(R.id.tvPropertyLocation);
        recyclerView = findViewById(R.id.recycler_view_star_icons2);
        btnRollback = findViewById(R.id.btnRollback4);
        progressBar = findViewById(R.id.progressBarCancel);
        btnCancelReservation = findViewById(R.id.btnCancelReservation);

        if (reservation == null && id != 0) {
            new FetchReservation().execute();
        }

        if (reservation != null) {
            tvPropertyName.setText(reservation.getProperty().getName());
            tvPropertyLocation.setText(reservation.getProperty().getAddress());
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(new IconAdapter(this, reservation.getProperty().getStar()));
            currentCheckInDate = reservation.getCheckInDate();
            currentCheckOutDate = reservation.getCheckOutDate();
        }
        tvReservationDate.setText(date);

        btnRollback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCancelReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có chắc chắn muốn hủy đặt chỗ này không?");

                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(reservation.getId());
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        tvChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar(currentCheckInDate, currentCheckOutDate);
            }
        });
    }

    private void showCalendar(Date checkInDate, Date checkOutDate) {
        Calendar checkInCal = Calendar.getInstance();
        checkInCal.setTime(checkInDate);
        checkInCal.add(Calendar.DAY_OF_MONTH, 1);

        Calendar checkOutCal = Calendar.getInstance();
        checkOutCal.setTime(checkOutDate);
        checkOutCal.add(Calendar.DAY_OF_MONTH, 1);
        Date newCheckInDate = new Date(checkInCal.getTimeInMillis());
        Date newCheckOutDate = new Date(checkOutCal.getTimeInMillis());

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Chọn");

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.now());

        builder.setSelection(new Pair<>(newCheckInDate.getTime(), newCheckOutDate.getTime()));

        builder.setCalendarConstraints(constraintsBuilder.build());
        final MaterialDatePicker<?> picker = builder.build();

        picker.show(getSupportFragmentManager(), picker.toString());

        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                if (selection instanceof Pair) {
                    Pair<Long, Long> dateRange = (Pair<Long, Long>) selection;
                    Long startDate = dateRange.first;
                    Long endDate = dateRange.second;

                    Calendar startCal = Calendar.getInstance();
                    Calendar endCal = Calendar.getInstance();

                    startCal.setTimeInMillis(startDate);
                    endCal.setTimeInMillis(endDate);
                    holderCheckInDate = new Date(startCal.getTimeInMillis());
                    holderCheckOutDate = new Date(endCal.getTimeInMillis());

                    new VerifyProperty().execute();
                }
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
                URL url = new URL("http://" + IPConfig.IPv4 + ":8080/api/properties/verify?propertyId=" + reservation.getProperty().getId() + "&checkInDate=" + holderCheckInDate + "&checkOutDate=" + holderCheckOutDate + "&room=" + reservation.getQuantity() + "&adult=" + reservation.getProperty().getAdultCapacity() + "&children=" + reservation.getProperty().getChildrenCapacity() + "&petAllow=" + reservation.getProperty().isPetsAllowed());
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
            AlertDialog.Builder builder = new AlertDialog.Builder(ReservationManagerActivity.this);
            if (!status) {
                builder.setTitle("Không có phòng trống vào ngày này");
                builder.setMessage("Xin lỗi bạn, ngày bạn yêu cầu không còn phòng trống. Vui lòng nhập ngày tháng khác và thử lại");
                builder.setPositiveButton("Thử ngày khác", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            } else {
                builder.setTitle("Có phòng trống vào ngày này");
                builder.setMessage("Ngày bạn yêu cầu còn phòng trống. Bạn có chắc chắn muốn đổi ngày không!");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        update(reservation.getId(), holderCheckInDate, holderCheckOutDate);
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void setText(Calendar startDate, Calendar endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d", new Locale("vi", "VN"));
        SimpleDateFormat monthFormat = new SimpleDateFormat("M", new Locale("vi", "VN"));

        String startDay = dateFormat.format(startDate.getTime());
        String startMonth = monthFormat.format(startDate.getTime());

        String endDay = dateFormat.format(endDate.getTime());
        String endMonth = monthFormat.format(endDate.getTime());

        String formattedText = startDay + " thg " + startMonth + " - " + endDay + " thg " + endMonth;

        tvReservationDate.setText(formattedText);
    }

    private class FetchReservation extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL("http://" + IPConfig.IPv4 + ":8080/api/reservations/" + id);
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
                try {
                    JSONObject pObject = new JSONObject(s);
                    int id = pObject.getInt("id");
                    int userId = pObject.getJSONObject("user").getInt("id");
                    JSONObject object = pObject.getJSONObject("property");
                    JSONArray imgs = object.getJSONArray("propertyImgs");
                    String firstname = pObject.getString("first_name");
                    String lastname = pObject.getString("last_name");
                    String email = pObject.getString("email");
                    String nationality = pObject.getString("nationality");
                    String phone = pObject.getString("phone");
                    int quantity = pObject.getInt("quantity");
                    Date checkInDate = Date.valueOf(pObject.getString("checkInDate"));
                    Date checkOutDate = Date.valueOf(pObject.getString("checkOutDate"));
                    Double price = pObject.getJSONObject("reservationDetail").getDouble("totalPrice");
                    List<String> imgList = new ArrayList<>();
                    for (int j = 0; j < imgs.length(); j++) {
                        imgList.add(imgs.getJSONObject(j).getString("src"));
                    }
                    Property property = new Property(object.getInt("id"), object.getString("name"), object.getInt("adultCapacity"), object.getInt("childrenCapacity"), object.getBoolean("petsAllowed"), object.getString("address"), object.getDouble("price"), object.getDouble("discount"), object.getInt("star"), object.getInt("singleBed"), object.getInt("doubleBed"), object.getInt("bedRoom"), object.getInt("quantity"), object.getJSONObject("propertyType").getString("name"), imgList);
                    reservation = new Reservation(id, userId, property, firstname, lastname, email, nationality, phone, quantity, checkInDate, checkOutDate, price);
                    tvPropertyName.setText(reservation.getProperty().getName());
                    tvPropertyLocation.setText(reservation.getProperty().getAddress());
                    recyclerView.setLayoutManager(new LinearLayoutManager(ReservationManagerActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    recyclerView.setAdapter(new IconAdapter(ReservationManagerActivity.this, reservation.getProperty().getStar()));
                    currentCheckInDate = reservation.getCheckInDate();
                    currentCheckOutDate = reservation.getCheckOutDate();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void delete(int id) {
        progressBar.setVisibility(View.VISIBLE);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + IPConfig.IPv4 + ":8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ReservationAPI reservationAPI = retrofit.create(ReservationAPI.class);
        Call<Void> call = reservationAPI.delete(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(ReservationManagerActivity.this, "Deleted reservation successful!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ReservationManagerActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ReservationManagerActivity.this, "Failed to delete reservation: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void update(int id, Date newCheckInDate, Date newCheckOutDate) {
        progressBar.setVisibility(View.VISIBLE);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + IPConfig.IPv4 + ":8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ReservationAPI reservationAPI = retrofit.create(ReservationAPI.class);
        Call<Void> call = reservationAPI.update(id, newCheckInDate, newCheckOutDate);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(ReservationManagerActivity.this, "Updated reservation successful!", Toast.LENGTH_LONG).show();
                Calendar checkInCal = Calendar.getInstance();
                checkInCal.setTime(holderCheckInDate);
                Calendar checkOutCal = Calendar.getInstance();
                checkOutCal.setTime(holderCheckOutDate);
                setText(checkInCal, checkOutCal);
                currentCheckInDate = holderCheckInDate;
                currentCheckOutDate = holderCheckOutDate;
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ReservationManagerActivity.this, "Failed to update reservation: " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}