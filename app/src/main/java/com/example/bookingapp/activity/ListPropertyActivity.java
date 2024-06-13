package com.example.bookingapp.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingapp.config.IPConfig;
import com.example.bookingapp.R;
import com.example.bookingapp.adapter.PropertyAdapter;
import com.example.bookingapp.entity.Property;

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
import java.util.ArrayList;
import java.util.List;

public class ListPropertyActivity extends AppCompatActivity {
    private ListView listView;
    private PropertyAdapter adapter;
    private List<Property> list;
    private String location;
    private Date checkInDate, checkOutDate;
    private int room, adult, children;
    private boolean petAllow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_property);
        listView = findViewById(R.id.lvListProperty);
        list = new ArrayList<>();
        adapter = new PropertyAdapter(this, list);
        listView.setAdapter(adapter);
        location = getIntent().getStringExtra("location");
        checkInDate = (Date) getIntent().getSerializableExtra("checkInDate");
        checkOutDate = (Date) getIntent().getSerializableExtra("checkOutDate");
        room = getIntent().getIntExtra("room", 1);
        adult = getIntent().getIntExtra("adult", 1);
        children = getIntent().getIntExtra("children", 0);
        petAllow = getIntent().getBooleanExtra("petAllow", false);

        new FetchProduct().execute();
    }

    private class FetchProduct extends AsyncTask<Void, Void, String> {

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
            if (s != null && !s.isEmpty()) {
                try {
                    JSONArray propertiesArray = new JSONArray(s);

                    for (int i = 0; i < propertiesArray.length(); i++) {
                        JSONObject pObject = propertiesArray.getJSONObject(i);
                        List<String> listImg = new ArrayList<>();
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

                        JSONArray imgsArray = pObject.getJSONArray("propertyImgs");
                        for (int j = 0; j < imgsArray.length(); j++) {
                            JSONObject imgObject = imgsArray.getJSONObject(j);
                            String imgSrc = imgObject.getString("src");
                            listImg.add(imgSrc);
                        }

                        Property property = new Property(id, name, adultCapacity, childrenCapacity, petsAllowed, address, price, discount, star, singleBed, doubleBed, bedRoom, quantity, listImg);
                        list.add(property);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
