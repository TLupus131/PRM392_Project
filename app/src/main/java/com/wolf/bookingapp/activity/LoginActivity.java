package com.wolf.bookingapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.wolf.bookingapp.R;
import com.wolf.bookingapp.activity.ui.login.LoginStep1Fragment;
import com.wolf.bookingapp.databinding.ActivityLoginBinding;
import com.wolf.bookingapp.databinding.ActivityMainBinding;

public class LoginActivity extends AppCompatActivity {
    private TextView textView;
    private ImageButton imageButton;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textView = findViewById(R.id.tvTermsAndConditions);
        imageButton = findViewById(R.id.btnClose);
        String text = "Qua việc đăng nhập hoặc tạo tài khoản,\nbạn đồng ý với các Điều khoản và Điều\nkiện cũng như Chính sách an toàn và\nBảo Mật của chúng tôi";
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);

        int start1 = text.indexOf("Điều khoản và Điều\nkiện");
        int end1 = start1 + "Điều khoản và Điều\nkiện".length();
        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), start1, end1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new UnderlineSpan(), start1, end1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        int start2 = text.indexOf("Chính sách an toàn và\nBảo Mật");
        int end2 = start2 + "Chính sách an toàn và\nBảo Mật".length();
        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new UnderlineSpan(), start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannable);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            LoginStep1Fragment loginStep1Fragment = new LoginStep1Fragment();
            fragmentTransaction.add(R.id.fragment_container_login, loginStep1Fragment);
            fragmentTransaction.commit();
        }
    }
}