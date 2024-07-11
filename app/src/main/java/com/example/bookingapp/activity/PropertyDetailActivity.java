package com.example.bookingapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.R;
import com.example.bookingapp.adapter.CommentAdapter;
import com.example.bookingapp.adapter.IconAdapter;
import com.example.bookingapp.config.CommentAPI;
import com.example.bookingapp.config.IPConfig;
import com.example.bookingapp.entity.Comment;
import com.example.bookingapp.entity.Property;
import com.example.bookingapp.entity.User;
import com.example.bookingapp.entity.UserManager;
import com.example.bookingapp.response.CommentResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

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

public class PropertyDetailActivity extends AppCompatActivity {

    private Property property;
    private int room, adult, day, children;
    private TextView property_name, check_in_date, check_out_date, total_price, final_price, price_for, room_and_customer_num, tvNoComment, tvAddress;
    private RecyclerView recyclerView;
    private ImageView img1, img2, img3, img4, img5;
    private ImageButton btnRollback, btnSubmitComment;
    private String totalPrice, finalPrice;
    private Date checkInDate, checkOutDate;
    private Button btnToBookingForm;
    private ListView lvComment;
    private CommentAdapter adapter;
    private EditText edtComment;
    private User authUser = null;
    private MapView mapView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_detail);
        property = (Property) getIntent().getSerializableExtra("property");
        room = getIntent().getIntExtra("room", 0);
        adult = getIntent().getIntExtra("adult", 0);
        day = getIntent().getIntExtra("day", 0);
        children = getIntent().getIntExtra("children", 0);
        checkInDate = (Date) getIntent().getSerializableExtra("checkInDate");
        checkOutDate = (Date) getIntent().getSerializableExtra("checkOutDate");
        totalPrice = getIntent().getStringExtra("totalPrice");
        finalPrice = getIntent().getStringExtra("finalPrice");

        fab = findViewById(R.id.fab);
        tvAddress = findViewById(R.id.tvAddress);
        mapView = findViewById(R.id.propertyMap);
        btnSubmitComment = findViewById(R.id.btnSubmitComment);
        edtComment = findViewById(R.id.commentEditText);
        lvComment = findViewById(R.id.commentListView);
        tvNoComment = findViewById(R.id.tvNoComment);
        btnToBookingForm = findViewById(R.id.btnToBookingForm);
        property_name = findViewById(R.id.property_name);
        recyclerView = findViewById(R.id.recycler_view_star_icons);
        btnRollback = findViewById(R.id.btnRollback);
        check_in_date = findViewById(R.id.checkInDate);
        check_out_date = findViewById(R.id.checkOutDate);
        total_price = findViewById(R.id.totalPrice);
        final_price = findViewById(R.id.finalPrice);
        room_and_customer_num = findViewById(R.id.roomAndCustomerNumber);
        price_for = findViewById(R.id.price_for);
        img1 = findViewById(R.id.property_img1);
        img2 = findViewById(R.id.property_img2);
        img3 = findViewById(R.id.property_img3);
        img4 = findViewById(R.id.property_img4);
        img5 = findViewById(R.id.property_img5);


        tvAddress.setText(property.getAddress());
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        fab.setImageResource(R.drawable.ic_message);
        fab.setColorFilter(ContextCompat.getColor(PropertyDetailActivity.this, android.R.color.white), PorterDuff.Mode.SRC_IN);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMessageLayout();
            }
        });

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                try {
                    LatLng location2 = new LatLng(21.012536865366116, 105.52554668706067);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(location2);
                    markerOptions.title(property.getName());
                    googleMap.addMarker(markerOptions);
                    float zoomLevel = 15.0f;
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location2, zoomLevel));
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        });

        authUser = UserManager.getInstance().getAuthUser();

        if (property.getListComment().isEmpty()) {
            tvNoComment.setVisibility(View.VISIBLE);
        } else {
            adapter = new CommentAdapter(this, property.getListComment());
            lvComment.setAdapter(adapter);
            setListViewHeightBasedOnChildren(lvComment);
        }
        btnToBookingForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (authUser != null) {
                    Intent intent = new Intent(PropertyDetailActivity.this, BookingFormActivity.class);
                    intent.putExtra("property", property);
                    intent.putExtra("room", room);
                    intent.putExtra("adult", adult);
                    intent.putExtra("day", day);
                    intent.putExtra("children", children);
                    intent.putExtra("checkInDate", checkInDate);
                    intent.putExtra("checkOutDate", checkOutDate);
                    intent.putExtra("totalPrice", totalPrice);
                    intent.putExtra("finalPrice", finalPrice);
                    startActivity(intent);
                } else {
                    Toast.makeText(PropertyDetailActivity.this, "Please login to make a reservation", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtComment.getText().toString().isEmpty() && authUser != null) {
                    addComment(edtComment.getText().toString(), authUser.getId(), property.getId());
                } else if (authUser == null) {
                    Toast.makeText(PropertyDetailActivity.this, "Please login to leave a comment!", Toast.LENGTH_LONG).show();
                }
            }
        });

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

        if (property.getListImage().size() >= 5) {
            Picasso.get().load(property.getListImage().get(0)).into(img1);
            Picasso.get().load(property.getListImage().get(1)).into(img2);
            Picasso.get().load(property.getListImage().get(2)).into(img3);
            Picasso.get().load(property.getListImage().get(3)).into(img4);
            Picasso.get().load(property.getListImage().get(4)).into(img5);
        }
        String text = "Giá cho " + day + " đêm, " + adult + " người lớn";
        if (children > 0) {
            text += ", " + children + " trẻ em";
        }

        price_for.setText(text);
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
    }

    private void openMessageLayout() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.contact_layout);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.CalendarAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        ImageButton button = dialog.findViewById(R.id.btnRollback1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void addComment(String content, int userId, int propertyId) {
        List<Comment> list = property.getListComment();
        CommentResponse commentResponse = new CommentResponse(content, userId, propertyId);
        list.add(new Comment(content, authUser.getName(), authUser.getAvatar(), authUser.getNationality()));
        adapter = new CommentAdapter(this, list);
        lvComment.setAdapter(adapter);
        setListViewHeightBasedOnChildren(lvComment);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + IPConfig.IPv4 + ":8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CommentAPI commentAPI = retrofit.create(CommentAPI.class);
        Call<CommentResponse> call = commentAPI.add(commentResponse);
        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                edtComment.setText("");
                Toast.makeText(PropertyDetailActivity.this, "Comment added successful!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Toast.makeText(PropertyDetailActivity.this, "Failed to add comment: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}