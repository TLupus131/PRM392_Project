package com.wolf.bookingapp.activity.ui.stay;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.wolf.bookingapp.config.IPConfig;
import com.wolf.bookingapp.R;
import com.wolf.bookingapp.activity.ListPropertyActivity;
import com.wolf.bookingapp.adapter.FutureTripAdapter;
import com.wolf.bookingapp.adapter.RegionAdapter;
import com.wolf.bookingapp.databinding.FragmentStayBinding;
import com.wolf.bookingapp.entity.FutureTrip;
import com.wolf.bookingapp.entity.Region;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.datepicker.MaterialDatePicker.Builder;

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

public class StayFragment extends Fragment {

    private FragmentStayBinding binding;
    Button btnChooseDate, btnChooseCustomer, btnChooseLocation, btnSearch;
    private List<Region> regionList;
    private RegionAdapter regionAdapter;
    private Date currentCheckInDate, currentCheckOutDate;
    private int currentRoom, currentAdult, currentChildren;
    private boolean currentPetAllow;
    private ProgressBar progressBar;
    private TextView tvEmptyList, tvLatitude, tvLongitude;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StayViewModel stayViewModels =
                new ViewModelProvider(this).get(StayViewModel.class);

        binding = FragmentStayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnChooseDate = view.findViewById(R.id.btnChooseDate);
        btnChooseCustomer = view.findViewById(R.id.btnChooseCustomerNumber);
        btnChooseLocation = view.findViewById(R.id.btnChooseLocation);
        btnSearch = view.findViewById(R.id.btnSearch);
        tvLatitude = view.findViewById(R.id.tvLatitude);
        tvLongitude = view.findViewById(R.id.tvLongitude);

        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        startCal.add(Calendar.DAY_OF_MONTH, 1);
        endCal.add(Calendar.DAY_OF_MONTH, 2);

        currentCheckInDate = new Date(startCal.getTimeInMillis());
        currentCheckOutDate = new Date(endCal.getTimeInMillis());
        currentRoom = 1;
        currentAdult = 2;
        currentChildren = 0;
        currentPetAllow = false;
        setBtnText(startCal, endCal);
        btnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });

        btnChooseCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomersNumber();
            }
        });
        btnChooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocation();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnChooseLocation.getText().toString().equalsIgnoreCase("Nhập điểm đến của bạn")) {
                    searhProperties();
                } else {
                    Toast.makeText(getContext(), "Vui lòng nhập điểm đến bạn mong muốn", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void searhProperties() {
        String location = btnChooseLocation.getText().toString();
        String latitude = tvLatitude.getText().toString();
        String longitude = tvLongitude.getText().toString();
        Intent intent = new Intent(getContext(), ListPropertyActivity.class);
        intent.putExtra("location", location);
        intent.putExtra("checkInDate", currentCheckInDate);
        intent.putExtra("checkOutDate", currentCheckOutDate);
        intent.putExtra("room", currentRoom);
        intent.putExtra("adult", currentAdult);
        intent.putExtra("children", currentChildren);
        intent.putExtra("petAllow", currentPetAllow);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
    }

    private void setUpNumberPicker(Dialog dialog) {
        EditText edtRoom = dialog.findViewById(R.id.npRooms);
        EditText edtAdult = dialog.findViewById(R.id.npAdults);
        EditText edtChildren = dialog.findViewById(R.id.npChildren);
        ImageButton btnAddRoom = dialog.findViewById(R.id.btnAddRoom);
        ImageButton btnAddAdult = dialog.findViewById(R.id.btnAddAdult);
        ImageButton btnAddChild = dialog.findViewById(R.id.btnAddChild);
        ImageButton btnMinusRoom = dialog.findViewById(R.id.btnMinusRoom);
        ImageButton btnMinusAdult = dialog.findViewById(R.id.btnMinusAdult);
        ImageButton btnMinusChild = dialog.findViewById(R.id.btnMinusChild);
        Button btnConfirmCustomer = dialog.findViewById(R.id.btnConfirmCustomer);
        ToggleButton toggleButton = dialog.findViewById(R.id.togglePetAllow);
        edtRoom.setText(String.valueOf(currentRoom));
        edtAdult.setText(String.valueOf(currentAdult));
        edtChildren.setText(String.valueOf(currentChildren));
        if (currentPetAllow) {
            toggleButton.setChecked(true);
        }
        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMinusRoom.setEnabled(true);
                int currentValue = Integer.parseInt(edtRoom.getText().toString());
                if (currentValue < 30) {
                    addValue(edtRoom);
                    if (currentValue == 29) {
                        btnAddRoom.setEnabled(false);
                    }
                }
            }
        });

        btnMinusRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddRoom.setEnabled(true);
                int currentValue = Integer.parseInt(edtRoom.getText().toString());
                if (currentValue > 0) {
                    minusValue(edtRoom);
                    if (currentValue == 2) {
                        btnMinusRoom.setEnabled(false);
                    }
                }
            }
        });

        btnAddAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMinusAdult.setEnabled(true);
                int currentValue = Integer.parseInt(edtAdult.getText().toString());
                if (currentValue < 30) {
                    addValue(edtAdult);
                    if (currentValue == 29) {
                        btnAddAdult.setEnabled(false);
                    }
                }
            }
        });

        btnMinusAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddAdult.setEnabled(true);
                int currentValue = Integer.parseInt(edtAdult.getText().toString());
                if (currentValue > 0) {
                    minusValue(edtAdult);
                    if (currentValue == 2) {
                        btnMinusAdult.setEnabled(false);
                    }
                }
            }
        });

        btnAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMinusChild.setEnabled(true);
                int currentValue = Integer.parseInt(edtChildren.getText().toString());
                if (currentValue < 10) {
                    addValue(edtChildren);
                    if (currentValue == 9) {
                        btnAddChild.setEnabled(false);
                    }
                }
            }
        });

        btnMinusChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddChild.setEnabled(true);
                int currentValue = Integer.parseInt(edtChildren.getText().toString());
                if (currentValue > 0) {
                    minusValue(edtChildren);
                    if (currentValue == 1) {
                        btnMinusChild.setEnabled(false);
                    }
                }
            }
        });

        btnConfirmCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rooms = Integer.parseInt(edtRoom.getText().toString());
                if (rooms > 30) {
                    rooms = 30;
                } else if (rooms < 1) {
                    rooms = 1;
                }
                int adults = Integer.parseInt(edtAdult.getText().toString());
                if (adults > 30) {
                    adults = 30;
                } else if (adults < 1) {
                    adults = 1;
                }
                int children = Integer.parseInt(edtChildren.getText().toString());
                if (children > 10) {
                    children = 10;
                } else if (children < 0) {
                    children = 0;
                }
                currentRoom = Integer.parseInt(edtRoom.getText().toString());
                currentAdult = Integer.parseInt(edtAdult.getText().toString());
                currentChildren = Integer.parseInt(edtChildren.getText().toString());
                currentPetAllow = toggleButton.isChecked();
                btnChooseCustomer.setText(rooms + " phòng - " + adults + " người lớn - " + children + " trẻ em");
                dialog.cancel();
            }
        });
    }

    private void addValue(EditText editText) {
        int value = Integer.parseInt(editText.getText().toString());
        value = value + 1;
        editText.setText(String.valueOf(value));
    }

    private void minusValue(EditText editText) {
        int value = Integer.parseInt(editText.getText().toString());
        value = value - 1;
        editText.setText(String.valueOf(value));
    }

    private void showCalendar() {
        Builder<Pair<Long, Long>> builder = Builder.dateRangePicker();
        builder.setTitleText("Chọn");

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.now());

        builder.setCalendarConstraints(constraintsBuilder.build());
        final MaterialDatePicker<?> picker = builder.build();

        picker.show(getParentFragmentManager(), picker.toString());

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
                    setBtnText(startCal, endCal);

                    currentCheckInDate = new Date(startCal.getTimeInMillis());
                    currentCheckOutDate = new Date(endCal.getTimeInMillis());
                }
            }
        });
    }

    private Handler handler = new Handler();
    private Runnable apiCallRunnable;
    private String searchText;

    private void showLocation() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout_1);
        dialog.show();
        tvEmptyList = dialog.findViewById(R.id.tvEmptyList);
        progressBar = dialog.findViewById(R.id.progressBarSearchRegion);
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

        ImageButton btnReturn = dialog.findViewById(R.id.btnReturn);
        EditText editText = dialog.findViewById(R.id.edt_search_region);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (apiCallRunnable != null) {
                    handler.removeCallbacks(apiCallRunnable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                apiCallRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (!s.toString().isEmpty()) {
                            searchText = s.toString();
                            tvEmptyList.setVisibility(View.GONE);
                            new FetchRegion().execute();
                        }
                    }
                };
                handler.postDelayed(apiCallRunnable, 1000);
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        ListView listView = dialog.findViewById(R.id.lvSearchRegion);
        regionList = new ArrayList<>();
        regionAdapter = new RegionAdapter(getActivity(), regionList, btnChooseLocation, dialog, tvLatitude, tvLongitude);
        listView.setAdapter(regionAdapter);
    }

    private class FetchRegion extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL("http://" + IPConfig.IPv4 + ":8080/api/regions/search?text=" + searchText);
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
            regionList.clear();
            if (s != null && !s.isEmpty()) {
                try {
                    JSONArray propertiesArray = new JSONArray(s);

                    for (int i = 0; i < propertiesArray.length(); i++) {
                        JSONObject pObject = propertiesArray.getJSONObject(i);
                        int id = pObject.getInt("id");
                        String name = pObject.getString("name");
                        String description = pObject.getString("description");
                        String latitude = pObject.getString("latitude");
                        String longitude = pObject.getString("longitude");
                        Region region = new Region(id, name, description, latitude, longitude);
                        regionList.add(region);
                    }
                    regionAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            if (regionList.isEmpty()) {
                tvEmptyList.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showCustomersNumber() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout_2);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.CalendarAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        setUpNumberPicker(dialog);
    }

    private void setBtnText(Calendar startDate, Calendar endDate) {
        SimpleDateFormat dayFormat = new SimpleDateFormat("E", new Locale("vi", "VN"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("d", new Locale("vi", "VN"));
        SimpleDateFormat monthFormat = new SimpleDateFormat("M", new Locale("vi", "VN"));

        String startDayOfWeek = dayFormat.format(startDate.getTime());
        String startDay = dateFormat.format(startDate.getTime());
        String startMonth = monthFormat.format(startDate.getTime());

        String endDayOfWeek = dayFormat.format(endDate.getTime());
        String endDay = dateFormat.format(endDate.getTime());
        String endMonth = monthFormat.format(endDate.getTime());

        String formattedText = startDayOfWeek + ", " + startDay + " thg " + startMonth + " -> " +
                endDayOfWeek + ", " + endDay + " thg " + endMonth;

        btnChooseDate.setText(formattedText);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}