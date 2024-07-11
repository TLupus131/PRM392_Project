package com.wolf.bookingapp.activity.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.wolf.bookingapp.R;
import com.wolf.bookingapp.activity.AccountManagerActivity;
import com.wolf.bookingapp.activity.LoginActivity;
import com.wolf.bookingapp.activity.MainActivity;
import com.wolf.bookingapp.adapter.ButtonIconAdapter;
import com.wolf.bookingapp.databinding.FragmentSettingsBinding;
import com.wolf.bookingapp.entity.Item;
import com.wolf.bookingapp.entity.User;
import com.wolf.bookingapp.entity.UserManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private ListView listView;
    private ButtonIconAdapter adapter;
    private List<Item> itemList;
    private Button button;
    private ImageView imageView;
    private TextView textView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.lvMenuBtn);
        button = view.findViewById(R.id.btnToLogin);
        imageView = view.findViewById(R.id.imageViewAvatar);
        textView = view.findViewById(R.id.tvEmail);
        itemList = new ArrayList<>();
        User user = UserManager.getInstance().getAuthUser();
        if (user != null) {
            itemList.add(new Item(R.drawable.ic_user, "Quản lý tài khoản", AccountManagerActivity.class));
            button.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(user.getEmail());
            if (user.getAvatar() != null) {
                Picasso.get().load(user.getAvatar()).into(imageView);
            }
        } else {
            itemList.add(new Item(R.drawable.ic_login, "Đăng nhập hoặc tạo tài khoản", LoginActivity.class));
        }
        itemList.add(new Item(R.drawable.ic_wallet, "Tặng thưởng & Ví"));
        itemList.add(new Item(R.drawable.ic_like, "Đánh giá"));
        itemList.add(new Item(R.drawable.ic_question_list, "Câu hỏi cho chỗ nghỉ"));
        itemList.add(new Item(R.drawable.ic_discount, "Ưu đãi"));
        itemList.add(new Item(R.drawable.ic_setting, "Cài đặt"));
        itemList.add(new Item(R.drawable.ic_add_home, "Đăng chỗ nghỉ"));
        if (user != null) {
            itemList.add(new Item(R.drawable.ic_logout, "Đăng xuất", MainActivity.class));
        }
        adapter = new ButtonIconAdapter(getContext(), itemList);
        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
