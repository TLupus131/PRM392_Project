package com.example.bookingapp.activity.ui.carRent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookingapp.databinding.FragmentCarRentBinding;

public class CarRentFragment extends Fragment {

    private FragmentCarRentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CarRentViewModel carRentViewModel =
                new ViewModelProvider(this).get(CarRentViewModel.class);

        binding = FragmentCarRentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}