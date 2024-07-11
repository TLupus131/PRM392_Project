package com.example.bookingapp.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookingapp.R;
import com.example.bookingapp.entity.Property;
import com.example.bookingapp.entity.User;
import com.example.bookingapp.entity.UserManager;

import java.sql.Date;

public class BookingFormActivity extends AppCompatActivity {

    private EditText edtFormFirstName, edtFormLastName, edtFormEmail, edtFormPhone;
    private Button btnToBookingConfirmation;
    private TextView tvTotalPrice, tvFinalPrice;
    private int room, adult, children;
    private String totalPrice, finalPrice;
    private Date checkInDate, checkOutDate;
    private Property property;
    private User authUser = null;
    private ImageButton btnRollback;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> nationalityAdapter;
    private String selectedCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_form);
        edtFormFirstName = findViewById(R.id.edtFormFirstName);
        edtFormLastName = findViewById(R.id.edtFormLastName);
        edtFormEmail = findViewById(R.id.edtFormEmail);
        edtFormPhone = findViewById(R.id.edtFormPhone);
        btnToBookingConfirmation = findViewById(R.id.btnToBookingConfirmation);
        tvTotalPrice = findViewById(R.id.totalPrice);
        tvFinalPrice = findViewById(R.id.finalPrice);
        btnRollback = findViewById(R.id.btnRollback2);
        autoCompleteTextView = findViewById(R.id.actvNationality);

        property = (Property) getIntent().getSerializableExtra("property");
        room = getIntent().getIntExtra("room", 0);
        adult = getIntent().getIntExtra("adult", 0);
        children = getIntent().getIntExtra("children", 0);
        checkInDate = (Date) getIntent().getSerializableExtra("checkInDate");
        checkOutDate = (Date) getIntent().getSerializableExtra("checkOutDate");
        totalPrice = getIntent().getStringExtra("totalPrice");
        finalPrice = getIntent().getStringExtra("finalPrice");
        nationalityAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.countries_array));
        AutoCompleteTextView actvNationality = findViewById(R.id.actvNationality);
        actvNationality.setAdapter(nationalityAdapter);

        authUser = UserManager.getInstance().getAuthUser();

        if (authUser != null && authUser.getNationality() != null && !authUser.getNationality().isEmpty()) {
            actvNationality.setText(authUser.getNationality());
        }

        if (authUser != null) {
            if (!authUser.getEmail().equals("null")) {
                edtFormEmail.setText(authUser.getEmail());
            }
            if (!authUser.getPhone().equals("null")) {
                edtFormPhone.setText(authUser.getPhone());
            }
        }

        tvTotalPrice.setText(totalPrice);
        tvTotalPrice.setPaintFlags(tvTotalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvFinalPrice.setText(finalPrice);

        btnRollback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnToBookingConfirmation.setOnClickListener(v -> {
            if (validate()) {
                selectedCountry = actvNationality.getText().toString();
                Intent intent = new Intent(BookingFormActivity.this, BookingConfirmActivity.class);
                intent.putExtra("property", property);
                intent.putExtra("room", room);
                intent.putExtra("adult", adult);
                intent.putExtra("children", children);
                intent.putExtra("checkInDate", checkInDate);
                intent.putExtra("checkOutDate", checkOutDate);
                intent.putExtra("totalPrice", totalPrice);
                intent.putExtra("finalPrice", finalPrice);
                intent.putExtra("email", edtFormEmail.getText().toString());
                intent.putExtra("phone", edtFormPhone.getText().toString());
                intent.putExtra("nationality", selectedCountry);
                intent.putExtra("firstname", edtFormFirstName.getText().toString());
                intent.putExtra("lastname", edtFormLastName.getText().toString());
                startActivity(intent);
            }
        });

        setFocusEditText(edtFormFirstName);
        setFocusEditText(edtFormLastName);
        setFocusEditText(edtFormEmail);
        setFocusEditText(edtFormPhone);

    }

    private void setFocusEditText(EditText edt) {
        edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LinearLayout layout = findViewById(R.id.layoutContent);
                layout.setMinimumHeight(300);
            }
        });
    }

    private boolean validate() {
        boolean status = true;
        if (edtFormFirstName.getText().toString().isEmpty()) {
            edtFormFirstName.setError("First name is required!");
            edtFormFirstName.requestFocus();
            edtFormFirstName.setBackgroundResource(R.drawable.custom_box_border_failed);
            status = false;
        }

        if (edtFormLastName.getText().toString().isEmpty()) {
            edtFormLastName.setError("Last name is required!");
            edtFormLastName.requestFocus();
            edtFormLastName.setBackgroundResource(R.drawable.custom_box_border_failed);
            status = false;
        }

        if (edtFormEmail.getText().toString().isEmpty()) {
            edtFormEmail.setError("Email is required!");
            edtFormEmail.requestFocus();
            edtFormEmail.setBackgroundResource(R.drawable.custom_box_border_failed);
            status = false;
        }

        if (autoCompleteTextView.getText().toString().isEmpty()) {
            autoCompleteTextView.setError("Nationality is required!");
            autoCompleteTextView.requestFocus();
            autoCompleteTextView.setBackgroundResource(R.drawable.custom_box_border_failed);
            status = false;
        }


        if (edtFormPhone.getText().toString().isEmpty()) {
            edtFormPhone.setError("Phone is required!");
            edtFormPhone.requestFocus();
            edtFormPhone.setBackgroundResource(R.drawable.custom_box_border_failed);
            status = false;
        }
        return status;
    }
}