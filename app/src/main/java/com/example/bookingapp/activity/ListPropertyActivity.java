package com.example.bookingapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.example.bookingapp.activity.ui.home.HomeFragment;
import com.example.bookingapp.activity.ui.stay.StayFragment;
import com.example.bookingapp.config.IPConfig;
import com.example.bookingapp.R;
import com.example.bookingapp.adapter.PropertyAdapter;
import com.example.bookingapp.entity.Comment;
import com.example.bookingapp.entity.Property;
import com.example.bookingapp.entity.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.tabs.TabLayout;

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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ListPropertyActivity extends AppCompatActivity {
    private ListView listView;
    private PropertyAdapter adapter;
    private List<Property> list;
    private String location, latitude, longitude;
    private Date checkInDate, checkOutDate;
    private int room, adult, children;
    private boolean petAllow;
    private ImageButton btnReturn;
    private ProgressBar progressBar;
    private TextView tvLocation, tvDate, tvEmptyList;

    private int lastCheckedRadioButtonId = R.id.radioTopChoice;

    int[][] states = new int[][]{
            new int[]{android.R.attr.state_selected},
            new int[]{-android.R.attr.state_selected}
    };

    int[] colors = new int[]{
            Color.WHITE,
            Color.BLACK
    };

    ColorStateList colorStateList = new ColorStateList(states, colors);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_property);
        tvEmptyList = findViewById(R.id.tvEmptyPropertyList);
        progressBar = findViewById(R.id.progressBar);
        btnReturn = findViewById(R.id.btnReturnHome);
        tvLocation = findViewById(R.id.tvLocation);
        tvDate = findViewById(R.id.tvDate);
        listView = findViewById(R.id.lvListProperty);
        location = getIntent().getStringExtra("location");
        checkInDate = (Date) getIntent().getSerializableExtra("checkInDate");
        checkOutDate = (Date) getIntent().getSerializableExtra("checkOutDate");
        room = getIntent().getIntExtra("room", 1);
        adult = getIntent().getIntExtra("adult", 1);
        children = getIntent().getIntExtra("children", 0);
        petAllow = getIntent().getBooleanExtra("petAllow", false);
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(checkInDate);
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(checkOutDate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("d", new Locale("vi", "VN"));
        SimpleDateFormat monthFormat = new SimpleDateFormat("M", new Locale("vi", "VN"));

        String startDay = dateFormat.format(startDate.getTime());
        String startMonth = monthFormat.format(startDate.getTime());

        String endDay = dateFormat.format(endDate.getTime());
        String endMonth = monthFormat.format(endDate.getTime());

        String formattedText = startDay + " thg " + startMonth + " - " + endDay + " thg " + endMonth;
        tvDate.setText(formattedText);
        tvLocation.setText(location);

        long startTime = startDate.getTimeInMillis();
        long endTime = endDate.getTimeInMillis();
        long diffTime = endTime - startTime;
        int daysDiff = (int) (diffTime / (1000 * 60 * 60 * 24));

        list = new ArrayList<>();
        adapter = new PropertyAdapter(this, list, room, daysDiff, adult, children, checkInDate, checkOutDate);
        listView.setAdapter(adapter);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvEmptyList.setVisibility(View.GONE);
        new FetchProperty().execute();

        TabLayout tabLayout = findViewById(R.id.tabLayout2);
        tabLayout.setTabTextColors(colorStateList);
        tabLayout.setSelectedTabIndicatorColor(colorStateList.getDefaultColor());
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View customTabView = LayoutInflater.from(ListPropertyActivity.this).inflate(R.layout.custom_tab_layout2, null);
                ImageView icon = customTabView.findViewById(R.id.icon2);
                TextView text = customTabView.findViewById(R.id.text2);

                switch (i) {
                    case 0:
                        icon.setImageResource(R.drawable.ic_sort);
                        text.setText("Sắp xếp");
                        break;
                    case 1:
                        icon.setImageResource(R.drawable.ic_filter);
                        text.setText("Lọc");
                        break;
                    case 2:
                        icon.setImageResource(R.drawable.ic_map);
                        text.setText("Bản đồ");
                        break;
                }
                tab.setCustomView(customTabView);
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    showSort();
                } else if (tab.getPosition() == 2) {
                    showMap();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    showSort();
                } else if (tab.getPosition() == 2) {
                    showMap();
                }
            }
        });
    }

    private void showMap() {
        final Dialog dialog = new Dialog(ListPropertyActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout_4);
        dialog.show();
        ImageButton imageButton = dialog.findViewById(R.id.btnReturn2);
        TextView textView1 = dialog.findViewById(R.id.tvMapLocation);
        TextView textView2 = dialog.findViewById(R.id.tvMapDate);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        textView1.setText(tvLocation.getText());
        textView2.setText(tvDate.getText());
        MapView mapView = dialog.findViewById(R.id.map);
        mapView.onCreate(dialog.onSaveInstanceState());
        mapView.onResume();

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.LocationAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        dialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                try {
                    LatLng location2 = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(location2);
                    markerOptions.title(location);
                    googleMap.addMarker(markerOptions);

                    float zoomLevel = 12.0f;
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location2, zoomLevel));
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        });
    }


    private void showSort() {
        final Dialog dialog = new Dialog(ListPropertyActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout_3);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.CalendarAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        RadioGroup radioGroup = dialog.findViewById(R.id.radioSortGroup);
        radioGroup.check(lastCheckedRadioButtonId);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (lastCheckedRadioButtonId != -1) {
                    RadioButton lastCheckedRadioButton = group.findViewById(lastCheckedRadioButtonId);
                    if (lastCheckedRadioButton != null) {
                        lastCheckedRadioButton.setChecked(false);
                    }
                }
                lastCheckedRadioButtonId = checkedId;

                if (checkedId == R.id.radioTopChoice) {
                    adapter.setList(list);
                } else if (checkedId == R.id.radioLowToHigh) {
                    List<Property> sortedListLowToHigh = new ArrayList<>(list);
                    Collections.sort(sortedListLowToHigh, Comparator.comparingDouble(Property::getPrice));
                    adapter.setList(sortedListLowToHigh);
                } else if (checkedId == R.id.radioHighToLow) {
                    List<Property> sortedListHighToLow = new ArrayList<>(list);
                    Collections.sort(sortedListHighToLow, (p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                    adapter.setList(sortedListHighToLow);
                } else if (checkedId == R.id.radioCustomerRating) {

                }
                dialog.cancel();
            }
        });
    }


    private class FetchProperty extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL("http://" + IPConfig.IPv4 + ":8080/api/properties/search?location=" + location + "&checkInDate=" + checkInDate + "&checkOutDate=" + checkOutDate + "&room=" + room + "&adult=" + adult + "&children=" + children + "&petAllow=" + petAllow);
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
                    JSONArray propertiesArray = new JSONArray(s);

                    for (int i = 0; i < propertiesArray.length(); i++) {
                        JSONObject pObject = propertiesArray.getJSONObject(i);
                        List<String> listImg = new ArrayList<>();
                        List<Comment> listCmt = new ArrayList<>();
                        int id = pObject.getInt("id");
                        String name = pObject.getString("name");
                        int adultCapacity = pObject.getInt("adultCapacity");
                        int childrenCapacity = pObject.getInt("childrenCapacity");
                        boolean petsAllowed = pObject.getBoolean("petsAllowed");
                        String address = pObject.getString("address");
                        double price = pObject.getDouble("price");
                        double discount = pObject.getDouble("discount");
                        int star = pObject.getInt("star");
                        int singleBed = pObject.getInt("singleBed");
                        int doubleBed = pObject.getInt("doubleBed");
                        int bedRoom = pObject.getInt("bedRoom");
                        int quantity = pObject.getInt("quantity");
                        JSONObject propertyType = pObject.getJSONObject("propertyType");
                        JSONObject region = pObject.getJSONObject("region");
                        JSONArray imgsArray = pObject.getJSONArray("propertyImgs");
                        for (int j = 0; j < imgsArray.length(); j++) {
                            JSONObject imgObject = imgsArray.getJSONObject(j);
                            String imgSrc = imgObject.getString("src");
                            listImg.add(imgSrc);
                        }
                        JSONArray cmtsArray = pObject.getJSONArray("comments");
                        for (int j = 0; j < cmtsArray.length(); j++) {
                            JSONObject cmtObject = cmtsArray.getJSONObject(j);
                            String cmtContent = cmtObject.getString("content");
                            JSONObject userObject = cmtObject.getJSONObject("user");
                            String userName = userObject.getString("name");
                            String userAvatar = userObject.getString("avatar");
                            String userNationality = userObject.getString("nationality");
                            Comment newComment = new Comment(cmtContent, userName, userAvatar, userNationality);
                            listCmt.add(newComment);
                        }
                        String type = propertyType.getString("name");
                        String regionName = region.getString("name");
                        Property property = new Property(id, name, adultCapacity, childrenCapacity, petsAllowed, address, price, discount, star, singleBed, doubleBed, bedRoom, quantity, listImg, listCmt, type, regionName);
                        list.add(property);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            if (list.isEmpty()) {
                tvEmptyList.setVisibility(View.VISIBLE);
            }
        }
    }
}
