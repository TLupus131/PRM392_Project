package com.example.bookingapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.bookingapp.R;
import com.example.bookingapp.config.IPConfig;
import com.example.bookingapp.databinding.ActivityMainBinding;
import com.example.bookingapp.entity.User;
import com.example.bookingapp.entity.UserManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String savedEmail, savedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(navView, navController);
        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.navigation_settings);

        getAuthUser();

        User user = UserManager.getInstance().getAuthUser();
        if (user != null) {
            menuItem.setTitle("Hồ sơ của tôi");
        }
    }

    public void getAuthUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("H_FILE", MODE_PRIVATE);
        savedEmail = sharedPreferences.getString("EMAIL", null);
        savedPassword = sharedPreferences.getString("PASSWORD", null);
        if (savedEmail != null && savedPassword != null) {
            new FetchAuthenticatedUser().execute();
        }
    }

    private class FetchAuthenticatedUser extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL("http://" + IPConfig.IPv4 + ":8080/api/users/authentication?email=" + savedEmail + "&password=" + savedPassword);
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
                    JSONObject userObject = new JSONObject(s);
                    int id = userObject.getInt("id");
                    String name = userObject.getString("name");
                    String avatar = userObject.getString("avatar");
                    String email = userObject.getString("email");
                    String address = userObject.getString("address");
                    String nationality = userObject.getString("nationality");
                    String phone = userObject.getString("phone");
                    boolean gender = userObject.getBoolean("gender");
                    String dob = userObject.getString("dob");
                    String role = userObject.getString("role");
                    User user = new User(id, name, avatar, email, address, nationality, phone, gender, dob, role);
                    UserManager.getInstance().setAuthUser(user);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
