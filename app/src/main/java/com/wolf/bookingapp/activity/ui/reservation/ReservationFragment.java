package com.wolf.bookingapp.activity.ui.reservation;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.wolf.bookingapp.R;
import com.wolf.bookingapp.adapter.PropertyAdapter;
import com.wolf.bookingapp.adapter.ReservationAdapter;
import com.wolf.bookingapp.config.IPConfig;
import com.wolf.bookingapp.databinding.FragmentReservationBinding;
import com.wolf.bookingapp.entity.Comment;
import com.wolf.bookingapp.entity.Property;
import com.wolf.bookingapp.entity.Region;
import com.wolf.bookingapp.entity.Reservation;
import com.wolf.bookingapp.entity.User;
import com.wolf.bookingapp.entity.UserManager;
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
import java.util.ArrayList;
import java.util.List;

public class ReservationFragment extends Fragment {

    private FragmentReservationBinding binding;
    private ProgressBar progressBar;
    private User authUser = null;
    private List<Reservation> list;
    private ReservationAdapter adapter;
    private ListView listView;
    private ImageView ivListReservationEmpty;
    private TextView tvListReservationEmpty2, tvListReservationEmpty;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ReservationViewModel reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);

        binding = FragmentReservationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    int[][] states = new int[][]{new int[]{android.R.attr.state_selected}, new int[]{-android.R.attr.state_selected}};

    int[] colors = new int[]{Color.WHITE, Color.BLACK};

    ColorStateList colorStateList = new ColorStateList(states, colors);

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authUser = UserManager.getInstance().getAuthUser();
        listView = view.findViewById(R.id.lvReservation);
        progressBar = view.findViewById(R.id.progressBarReservation);
        ivListReservationEmpty = view.findViewById(R.id.ivListReservationEmpty);
        tvListReservationEmpty = view.findViewById(R.id.tvListReservationEmpty);
        tvListReservationEmpty2 = view.findViewById(R.id.tvListReservationEmpty2);

        list = new ArrayList<>();
        adapter = new ReservationAdapter(getContext(), list);
        listView.setAdapter(adapter);
        if (authUser != null) {
            new FetchReservation().execute();
        } else {
            tvListReservationEmpty.setText("Không có đặt chỗ");
            tvListReservationEmpty2.setText("Vui lòng đăng nhập hoặc tạo tài khoản để bắt đầu");
            ivListReservationEmpty.setVisibility(View.VISIBLE);
            tvListReservationEmpty.setVisibility(View.VISIBLE);
            tvListReservationEmpty2.setVisibility(View.VISIBLE);
        }

        TabLayout tabLayout = view.findViewById(R.id.tabLayout2);
        tabLayout.setTabTextColors(colorStateList);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorActiveTab));

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View customTabView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_tab_layout3, null);
                TextView text = customTabView.findViewById(R.id.text3);

                switch (i) {
                    case 0:
                        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        text.setText("Đang hoạt động");
                        break;
                    case 1:
                        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        text.setText("Đã qua");
                        break;
                    case 2:
                        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        text.setText("Đã hủy");
                        break;
                }
                tab.setCustomView(customTabView);
            }
        }
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
                URL url = new URL("http://" + IPConfig.IPv4 + ":8080/api/reservations/search?userId=" + authUser.getId());
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
                        Reservation reservation = new Reservation(id, userId, property, firstname, lastname, email, nationality, phone, quantity, checkInDate, checkOutDate, price);
                        list.add(reservation);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            if (list.isEmpty()) {
                ivListReservationEmpty.setVisibility(View.VISIBLE);
                tvListReservationEmpty2.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
