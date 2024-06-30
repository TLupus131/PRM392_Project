package com.example.bookingapp.activity.ui.home;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookingapp.R;
import com.example.bookingapp.activity.ui.carRent.CarRentFragment;
import com.example.bookingapp.activity.ui.flight.FlightFragment;
import com.example.bookingapp.activity.ui.stay.StayFragment;
import com.example.bookingapp.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    int[][] states = new int[][] {
            new int[] { android.R.attr.state_selected },
            new int[] { -android.R.attr.state_selected }
    };

    int[] colors = new int[] {
            Color.WHITE,
            Color.BLACK
    };

    ColorStateList colorStateList = new ColorStateList(states, colors);


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setTabTextColors(colorStateList);
        tabLayout.setSelectedTabIndicatorColor(colorStateList.getDefaultColor());

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View customTabView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_tab_layout, null);
                ImageView icon = customTabView.findViewById(R.id.icon);
                TextView text = customTabView.findViewById(R.id.text);

                switch (i) {
                    case 0:
                        icon.setImageResource(R.drawable.ic_bed);
                        text.setText("Lưu trú");
                        break;
                    case 1:
                        icon.setImageResource(R.drawable.ic_car);
                        text.setText("Thuê xe");
                        break;
                    case 2:
                        icon.setImageResource(R.drawable.ic_plane);
                        text.setText("Chuyến bay");
                        break;
                }
                tab.setCustomView(customTabView);
            }
        }

        replaceFragment(new StayFragment());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        replaceFragment(new StayFragment());
                        break;
                    case 1:
                        replaceFragment(new CarRentFragment());
                        break;
                    case 2:
                        replaceFragment(new FlightFragment());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No action needed here
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No action needed here
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}