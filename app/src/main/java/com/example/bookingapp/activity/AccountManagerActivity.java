package com.example.bookingapp.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingapp.R;
import com.example.bookingapp.config.IPConfig;
import com.example.bookingapp.config.UserAPI;
import com.example.bookingapp.entity.User;
import com.example.bookingapp.entity.UserManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountManagerActivity extends AppCompatActivity {

    private LinearLayout layoutNationality, layoutName, layoutGender, layoutPhone, layoutEmail, layoutAddress, layoutPassword, layoutDob;

    private TextView tvName, tvNationality, tvGender, tvEmail, tvDob, tvAddress, tvPhone;

    private User user;

    private ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);

        layoutName = findViewById(R.id.layoutName);
        layoutAddress = findViewById(R.id.layoutAddress);
        layoutNationality = findViewById(R.id.layoutNationality);
        layoutDob = findViewById(R.id.layoutDob);
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutGender = findViewById(R.id.layoutGender);
        layoutPhone = findViewById(R.id.layoutPhone);
        layoutPassword = findViewById(R.id.layoutPassword);
        tvName = findViewById(R.id.tvProfileName);
        tvNationality = findViewById(R.id.tvProfileNationality);
        tvGender = findViewById(R.id.tvProfileGender);
        tvEmail = findViewById(R.id.tvProfileEmail);
        tvDob = findViewById(R.id.tvProfileDob);
        tvAddress = findViewById(R.id.tvProfileAddress);
        tvPhone = findViewById(R.id.tvProfilePhone);
        button = findViewById(R.id.btnRollback5);
        user = UserManager.getInstance().getAuthUser();
        if (user != null) {
            if (!user.getName().equals("null")) {
                tvName.setText(user.getName());
                tvName.setTextColor(Color.BLACK);
                tvName.setTypeface(null, Typeface.BOLD);
            }
            if (!user.getEmail().equals("null")) {
                tvEmail.setText(user.getEmail());
                tvEmail.setTextColor(Color.BLACK);
                tvEmail.setTypeface(null, Typeface.BOLD);
            }
            if (!user.getAddress().equals("null")) {
                tvAddress.setText(user.getAddress());
                tvAddress.setTextColor(Color.BLACK);
                tvAddress.setTypeface(null, Typeface.BOLD);
            }
            if (!user.getPhone().equals("null")) {
                tvPhone.setText(user.getPhone());
                tvPhone.setTextColor(Color.BLACK);
                tvPhone.setTypeface(null, Typeface.BOLD);
            }
            if (user.isGender()) {
                tvGender.setText("Nam");
            } else {
                tvGender.setText("Nữ");
            }
            tvGender.setTextColor(Color.BLACK);
            tvGender.setTypeface(null, Typeface.BOLD);

            if (!user.getNationality().equals("null")) {
                tvNationality.setText(user.getNationality());
                tvNationality.setTextColor(Color.BLACK);
                tvNationality.setTypeface(null, Typeface.BOLD);
            }
            if (!user.getDob().equals("null")) {
                tvDob.setText(formatDateString(user.getDob()));
                tvDob.setTextColor(Color.BLACK);
                tvDob.setTypeface(null, Typeface.BOLD);
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layoutName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditerLayout("Tên", tvName.getText().toString(), "Nhập họ và tên", tvName);
            }
        });

        layoutPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditerPasswordLayout(tvName);
            }
        });

        layoutEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditerLayout("Địa chỉ email", tvEmail.getText().toString(), "Nhập địa chỉ email", tvEmail);
            }
        });

        layoutDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDobEditor(tvDob);
            }
        });

        layoutGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderEditer(tvGender);
            }
        });

        layoutNationality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditerLayout("Quốc tịch", tvNationality.getText().toString(), "Nhập quốc tịch", tvNationality);
            }
        });

        layoutAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditerLayout("Địa chỉ", tvAddress.getText().toString(), "Nhập địa chỉ", tvAddress);
            }
        });

        layoutPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditerLayout("Số điện thoại", tvPhone.getText().toString(), "Nhập số điện thoại", tvPhone);
            }
        });
    }

    private void update(int id, String type, String holder, TextView tvHolder, Dialog dialog) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + IPConfig.IPv4 + ":8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UserAPI userAPI = retrofit.create(UserAPI.class);
        Call<Void> call = null;
        switch (type) {
            case "Tên":
                call = userAPI.update(id, holder, "null", "null", "null", "null", "null", "null", "null");
                user.setName(holder);
                break;
            case "Địa chỉ email":
                call = userAPI.update(id, "null", holder, "null", "null", "null", "null", "null", "null");
                user.setEmail(holder);
                break;
            case "Mật khẩu":
                call = userAPI.update(id, "null", "null", holder, "null", "null", "null", "null", "null");
                user.setPassword(holder);
                break;
            case "Địa chỉ":
                call = userAPI.update(id, "null", "null", "null", holder, "null", "null", "null", "null");
                user.setAddress(holder);
                break;
            case "Số điện thoại":
                call = userAPI.update(id, "null", "null", "null", "null", holder, "null", "null", "null");
                user.setPhone(holder);
                break;
            case "Quốc tịch":
                call = userAPI.update(id, "null", "null", "null", "null", "null", holder, "null", "null");
                user.setNationality(holder);
                break;
            case "Ngày sinh":
                call = userAPI.update(id, "null", "null", "null", "null", "null", "null", holder, "null");
                user.setDob(holder);
                break;
            case "Giới tính":
                call = userAPI.update(id, "null", "null", "null", "null", "null", "null", "null", holder);
                if (holder.equals("male")) {
                    user.setGender(true);
                } else if (holder.equals("female")) {
                    user.setGender(false);
                }
                break;
        }
        if (call != null) {
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(AccountManagerActivity.this, "Updated user successful!", Toast.LENGTH_LONG).show();
                    UserManager.getInstance().setAuthUser(user);
                    if (!type.equals("Mật khẩu") && !type.equals("Giới tính") && !type.equals("Ngày sinh")) {
                        tvHolder.setText(holder);
                        tvHolder.setTextColor(Color.BLACK);
                        tvHolder.setTypeface(null, Typeface.BOLD);
                    } else if (type.equals("Giới tính")) {
                        if (holder.equals("male")) {
                            tvHolder.setText("Nam");
                        } else {
                            tvHolder.setText("Nữ");
                        }
                    } else if (type.equals("Ngày sinh")) {
                        tvHolder.setText(formatDateString(holder));
                        tvHolder.setTextColor(Color.BLACK);
                        tvHolder.setTypeface(null, Typeface.BOLD);
                    }
                    dialog.cancel();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(AccountManagerActivity.this, "Failed to update user: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public static String formatDateString(String dateStr) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd 'thg' M yyyy", Locale.US);

        Date date = null;
        try {
            date = inputFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (date != null) ? outputFormat.format(date) : "";
    }

    public void openEditerPasswordLayout(TextView tvHolder) {
        final Dialog dialog = new Dialog(AccountManagerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout_profile_2);
        dialog.show();
        ImageButton imageButton = dialog.findViewById(R.id.btnRollback7);
        Button btnSavePassword = dialog.findViewById(R.id.btnSavePassword);
        LinearLayout layout = dialog.findViewById(R.id.footer2);
        TextInputEditText edtOldPassword = dialog.findViewById(R.id.edtOldPassword);
        TextInputEditText edtNewPassword = dialog.findViewById(R.id.edtNewPassword);
        TextInputEditText edtConfirmPassword = dialog.findViewById(R.id.edtConfirmPassword);

        edtOldPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                layout.setPadding(0, 0, 0, 1000);
            }
        });

        edtNewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                layout.setPadding(0, 0, 0, 1000);
            }
        });

        edtConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                layout.setPadding(0, 0, 0, 1000);
            }
        });

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
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        btnSavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status = true;
                if (!edtOldPassword.getText().toString().equals(user.getPassword())) {
                    edtOldPassword.setError("Incorrect password!");
                    edtOldPassword.requestFocus();
                    edtOldPassword.setBackgroundResource(R.drawable.custom_box_border_failed);
                    status = false;
                }
                if (TextUtils.isEmpty(edtNewPassword.getText().toString())) {
                    edtNewPassword.setError("Password is required!");
                    edtNewPassword.requestFocus();
                    edtNewPassword.setBackgroundResource(R.drawable.custom_box_border_failed);
                    edtConfirmPassword.setBackgroundResource(R.drawable.custom_box_border_failed);
                    status = false;
                } else if (!edtConfirmPassword.getText().toString().equals(edtNewPassword.getText().toString())) {
                    edtConfirmPassword.setError("Password doesn't match!");
                    edtConfirmPassword.requestFocus();
                    edtConfirmPassword.setBackgroundResource(R.drawable.custom_box_border_failed);
                    status = false;
                }
                if (status) {
                    update(user.getId(), "Mật khẩu", edtNewPassword.getText().toString(), tvHolder, dialog);
                }
            }
        });
    }

    public void showGenderEditer(TextView tvHolder) {
        final Dialog dialog = new Dialog(AccountManagerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout_profile_3);
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
        RadioGroup radioGroup = dialog.findViewById(R.id.radioGender);
        if (user.isGender()) {
            radioGroup.check(R.id.radioMale);
        } else {
            radioGroup.check(R.id.radioFemale);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioMale) {
                    update(user.getId(), "Giới tính", "male", tvHolder, dialog);
                } else if (checkedId == R.id.radioFemale) {
                    update(user.getId(), "Giới tính", "female", tvHolder, dialog);
                }
            }
        });
    }

    public void showDobEditor(TextView tvHolder) {
        final Dialog dialog = new Dialog(AccountManagerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout_profile_4);
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

        Button btn = dialog.findViewById(R.id.btnSaveDob);

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        NumberPicker numberPickerDay = dialog.findViewById(R.id.numberPickerDay);
        NumberPicker numberPickerMonth = dialog.findViewById(R.id.numberPickerMonth);
        NumberPicker numberPickerYear = dialog.findViewById(R.id.numberPickerYear);

        numberPickerDay.setMinValue(1);
        numberPickerDay.setMaxValue(31);
        numberPickerDay.setValue(currentDay);
        numberPickerDay.setWrapSelectorWheel(false);

        numberPickerMonth.setMinValue(1);
        numberPickerMonth.setMaxValue(12);
        numberPickerMonth.setValue(currentMonth);
        numberPickerMonth.setWrapSelectorWheel(false);

        numberPickerYear.setMinValue(1900);
        numberPickerYear.setMaxValue(currentYear);
        numberPickerYear.setValue(currentYear);
        numberPickerYear.setWrapSelectorWheel(false);

        numberPickerMonth.setOnValueChangedListener((picker, oldVal, newVal) -> updateDays(numberPickerDay, numberPickerMonth, numberPickerYear));
        numberPickerYear.setOnValueChangedListener((picker, oldVal, newVal) -> updateDays(numberPickerDay, numberPickerMonth, numberPickerYear));

        updateDays(numberPickerDay, numberPickerMonth, numberPickerYear);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(user.getId(), "Ngày sinh", numberPickerYear.getValue() + "-" + numberPickerMonth.getValue() + "-" + numberPickerDay.getValue(), tvHolder, dialog);
            }
        });
    }

    private void updateDays(NumberPicker numberPickerDay, NumberPicker numberPickerMonth, NumberPicker numberPickerYear) {
        int month = numberPickerMonth.getValue();
        int year = numberPickerYear.getValue();
        int maxDays = getDaysInMonth(month, year);
        numberPickerDay.setMinValue(1);
        numberPickerDay.setMaxValue(maxDays);

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (year == currentYear) {
            numberPickerMonth.setMaxValue(currentMonth);
            numberPickerDay.setMaxValue(currentDay);
        } else {
            numberPickerMonth.setMaxValue(12);
            numberPickerDay.setMaxValue(getDaysInMonth(month, year));
        }
    }

    private int getDaysInMonth(int month, int year) {
        switch (month) {
            case 2:
                return (isLeapYear(year)) ? 29 : 28;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 31;
        }
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }


    public void openEditerLayout(String tvName, String edtText, String edtHint, TextView tvHolder) {
        final Dialog dialog = new Dialog(AccountManagerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout_profile_1);
        dialog.show();
        ImageButton imageButton = dialog.findViewById(R.id.btnRollback6);
        TextView textViewTitle = dialog.findViewById(R.id.tvTitleHolder);
        TextView textViewHolder = dialog.findViewById(R.id.tvHolder);
        EditText editTextHolder = dialog.findViewById(R.id.edtHolder);
        Button btnHolder = dialog.findViewById(R.id.btnSaveUser);
        LinearLayout layout = dialog.findViewById(R.id.footer);

        textViewTitle.setText(tvName);
        textViewHolder.setText(tvName + " *");
        if (edtText.equals("null")) {
            editTextHolder.setHint(edtHint);
        } else {
            editTextHolder.setText(edtText);
        }

        editTextHolder.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                layout.setPadding(0, 0, 0, 1000);
            }
        });

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
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        btnHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editTextHolder.getText().toString())) {
                    Toast.makeText(dialog.getContext(), "Please enter your " + tvName, Toast.LENGTH_LONG).show();
                    editTextHolder.setError(tvName + " is required!");
                    editTextHolder.requestFocus();
                    editTextHolder.setBackgroundResource(R.drawable.custom_box_border_failed);
                } else {
                    update(user.getId(), tvName, editTextHolder.getText().toString(), tvHolder, dialog);
                }
            }
        });
    }
}