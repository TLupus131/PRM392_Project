package com.example.bookingapp.activity.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookingapp.R;
import com.example.bookingapp.activity.ui.register.RegisterFragment;
import com.example.bookingapp.adapter.ButtonIconAdapter;
import com.example.bookingapp.databinding.FragmentLoginStep1Binding;
import com.example.bookingapp.databinding.FragmentSettingsBinding;
import com.example.bookingapp.entity.Item;

import java.util.List;

public class LoginStep1Fragment extends Fragment {

    private FragmentLoginStep1Binding binding;
    private Button btnToLogin, btnToRegister;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LoginStep1ViewModel loginStep1ViewModel =
                new ViewModelProvider(this).get(LoginStep1ViewModel.class);

        binding = FragmentLoginStep1Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnToLogin = view.findViewById(R.id.btnToLogin2);
        btnToRegister = view.findViewById(R.id.btnToRegister);

        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new LoginStep2Fragment());
            }
        });

        btnToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new RegisterFragment());
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
