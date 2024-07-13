package com.wolf.bookingapp.activity.ui.register;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.wolf.bookingapp.R;
import com.wolf.bookingapp.activity.ui.login.LoginStep2Fragment;
import com.wolf.bookingapp.config.IPConfig;
import com.wolf.bookingapp.config.UserAPI;
import com.wolf.bookingapp.databinding.FragmentRegisterBinding;
import com.wolf.bookingapp.response.UserResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private TextView tvReturnLogin;
    private Button btnRegister;
    private EditText edtEmail;
    private TextInputEditText edtPassword, edtConfirm;
    private ProgressBar progressBar;
    private RadioGroup rgGender;
    private RadioButton selectedGenderButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RegisterViewModel registerViewModel =
                new ViewModelProvider(this).get(RegisterViewModel.class);

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvReturnLogin = view.findViewById(R.id.tvReturnLogin);
        btnRegister = view.findViewById(R.id.btnRegister);
        edtEmail = view.findViewById(R.id.edtRegisterEmail);
        edtPassword = view.findViewById(R.id.edtRegisterPassword);
        edtConfirm = view.findViewById(R.id.edtRegisterPasswordConfirm);
        progressBar = view.findViewById(R.id.progressBarRegister);
        rgGender = view.findViewById(R.id.rgGender);
        tvReturnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new LoginStep2Fragment());
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtEmail.setBackgroundResource(R.drawable.custom_box_border_success);
                edtPassword.setBackgroundResource(R.drawable.custom_box_border_success);
                edtConfirm.setBackgroundResource(R.drawable.custom_box_border_success);
                rgGender.setBackgroundResource(R.drawable.custom_box_border_success);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                    builder.setMessage("Please enter your email")
                            .setTitle("Validation Error")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    edtEmail.setError("Email is required!");
                                    edtEmail.requestFocus();
                                    edtEmail.setBackgroundResource(R.drawable.custom_box_border_failed);
                                }
                            });
                    AlertDialog emailDialog = builder.create();
                    emailDialog.show();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()) {
                    builder.setMessage("Please re-enter your email")
                            .setTitle("Validation Error")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    edtEmail.setError("Valid email is required");
                                    edtEmail.requestFocus();
                                    edtEmail.setBackgroundResource(R.drawable.custom_box_border_failed);
                                }
                            });
                    AlertDialog emailDialog = builder.create();
                    emailDialog.show();
                    return;
                }
                if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                    builder.setMessage("Please enter your password")
                            .setTitle("Validation Error")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    edtPassword.setError("Password is required!");
                                    edtPassword.requestFocus();
                                    edtPassword.setBackgroundResource(R.drawable.custom_box_border_failed);
                                }
                            });
                    AlertDialog passwordDialog = builder.create();
                    passwordDialog.show();
                    return;
                }
                if (TextUtils.isEmpty(edtConfirm.getText().toString())) {
                    builder.setMessage("Please confirm your password")
                            .setTitle("Validation Error")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    edtConfirm.setError("Password confirmation is required!");
                                    edtConfirm.requestFocus();
                                    edtConfirm.setBackgroundResource(R.drawable.custom_box_border_failed);
                                }
                            });
                    AlertDialog confirmDialog = builder.create();
                    confirmDialog.show();
                    return;
                } else if (!edtPassword.getText().toString().equals(edtConfirm.getText().toString())) {
                    builder.setMessage("Passwords don't match! Please input again.")
                            .setTitle("Validation Error")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    edtPassword.setError("Passwords do not match!");
                                    edtConfirm.setError("Passwords do not match!");
                                    edtPassword.requestFocus();
                                    edtPassword.setBackgroundResource(R.drawable.custom_box_border_failed);
                                    edtConfirm.clearComposingText();
                                    edtConfirm.setBackgroundResource(R.drawable.custom_box_border_failed);
                                }
                            });
                    AlertDialog mismatchDialog = builder.create();
                    mismatchDialog.show();
                    return;
                }
                int selectedGenderId = rgGender.getCheckedRadioButtonId();
                if (selectedGenderId == -1) {
                    builder.setMessage("Please select your gender")
                            .setTitle("Validation Error")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    rgGender.setBackgroundResource(R.drawable.custom_box_border_failed);
                                }
                            });
                    AlertDialog genderDialog = builder.create();
                    genderDialog.show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                registerUser(edtEmail.getText().toString(), edtPassword.getText().toString(), selectedGenderButton.getText().toString());
            }
        });
    }


    private void registerUser(String email, String password, String gender) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(),
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("User registration successful")
                                    .setTitle("Success")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                            AlertDialog successDialog = builder.create();
                            successDialog.show();
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            firebaseUser.sendEmailVerification();
                            addUserToDatabase(email, password, gender);
                            replaceFragment(new LoginStep2Fragment());
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                edtPassword.setError("Your password is too weak! Password must contain alphabets, number and special characters");
                                edtPassword.requestFocus();
                                edtPassword.setBackgroundResource(R.drawable.custom_box_border_failed);
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                edtEmail.setError("Your email is invalid or already in use! Please enter again.");
                                edtEmail.requestFocus();
                                edtEmail.setBackgroundResource(R.drawable.custom_box_border_failed);
                            } catch (FirebaseAuthUserCollisionException e) {
                                edtEmail.setError("User is already registered with this email. Use another email.");
                                edtEmail.requestFocus();
                                edtEmail.setBackgroundResource(R.drawable.custom_box_border_failed);
                            } catch (Exception e) {
                                Log.e(getTag(), e.getMessage());
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage(e.getMessage())
                                        .setTitle("Error")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                AlertDialog errorDialog = builder.create();
                                errorDialog.show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void addUserToDatabase(String email, String password, String gender) {
        UserResponse userResponse = new UserResponse(0, email, password, gender);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + IPConfig.IPv4 + ":8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UserAPI userAPI = retrofit.create(UserAPI.class);
        Call<UserResponse> call = userAPI.register(userResponse);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("User registered successfully!")
                        .setTitle("Success")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog successDialog = builder.create();
                successDialog.show();
            }


            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Failed to register user: " + t.getMessage())
                        .setTitle("Error")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog errorDialog = builder.create();
                errorDialog.show();
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
