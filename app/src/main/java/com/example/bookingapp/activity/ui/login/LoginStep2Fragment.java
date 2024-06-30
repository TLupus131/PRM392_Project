package com.example.bookingapp.activity.ui.login;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.content.ContextWrapper;
import com.example.bookingapp.R;
import com.example.bookingapp.activity.MainActivity;
import com.example.bookingapp.activity.ui.register.RegisterFragment;
import com.example.bookingapp.config.IPConfig;
import com.example.bookingapp.databinding.FragmentLoginStep2Binding;
import com.example.bookingapp.entity.User;
import com.example.bookingapp.entity.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginStep2Fragment extends Fragment {
    private FragmentLoginStep2Binding binding;
    private TextView tvToRegister, tvForgotPassword;
    private Button btnLogin;
    private EditText edtEmail;
    private TextInputEditText edtPassword;
    private User user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LoginStep2ViewModel loginStep2ViewModel =
                new ViewModelProvider(this).get(LoginStep2ViewModel.class);

        binding = FragmentLoginStep2Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvToRegister = view.findViewById(R.id.tvToRegister);
        tvForgotPassword = view.findViewById(R.id.tvForgotPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        edtEmail = view.findViewById(R.id.edtLoginEmail);
        edtPassword = view.findViewById(R.id.edtLoginPassword);
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "example@example.com";
                requestPasswordReset(email);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtEmail.setBackgroundResource(R.drawable.custom_box_border_success);
                edtPassword.setBackgroundResource(R.drawable.custom_box_border_success);
                boolean validate = true;
                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                    Toast.makeText(getContext(), "Please enter your email", Toast.LENGTH_LONG).show();
                    edtEmail.setError("Email is required!");
                    edtEmail.requestFocus();
                    edtEmail.setBackgroundResource(R.drawable.custom_box_border_failed);
                    validate = false;
                }
                if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                    Toast.makeText(getContext(), "Please enter your password", Toast.LENGTH_LONG).show();
                    edtPassword.setError("Password is required!");
                    edtPassword.requestFocus();
                    validate = false;
                    edtPassword.setBackgroundResource(R.drawable.custom_box_border_failed);
                }
                if (validate) {
                    new FetchAuthenticatedUser().execute();
                }
            }
        });

        tvToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new RegisterFragment());
            }
        });

    }

    private class FetchAuthenticatedUser extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL("http://" + IPConfig.IPv4 + ":8080/api/users/authentication?email=" + edtEmail.getText().toString() + "&password=" + edtPassword.getText().toString());
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
                    String password = userObject.getString("password");
                    user = new User(id, name, avatar, email, password, address, nationality, phone, gender, dob, role);
                    UserManager.getInstance().setAuthUser(user);
                    saveUserToPreference(user);
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                edtEmail.setError("Wrong email or password! Please enter again.");
                edtPassword.setError("Wrong email or password! Please enter again.");
                edtEmail.requestFocus();
                edtPassword.requestFocus();
                edtEmail.setBackgroundResource(R.drawable.custom_box_border_failed);
                edtPassword.setBackgroundResource(R.drawable.custom_box_border_failed);
            }
        }
    }

    public void saveUserToPreference(User user){
        if (user != null) {
            SharedPreferences sharedPreferences
                    = getContext().getSharedPreferences("H_FILE", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("EMAIL", user.getEmail());
            editor.putString("PASSWORD", user.getPassword());
            editor.apply();
        }
    }

    private void requestPasswordReset(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Password reset email sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to send reset email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_login, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
