package com.simpleshoestore.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.simpleshoestore.R;
import com.simpleshoestore.activities.AddShoeActivity;
import com.simpleshoestore.utils.SharedPrefsHelper;

public class ProfileFragment extends Fragment {

    private TextView tvUsername, tvEmail, tvOrderCount;
    private Button btnOrderHistory, btnSettings, btnAddShoe;
    private SharedPrefsHelper prefsHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        prefsHelper = new SharedPrefsHelper(getContext());
        initViews(view);
        setupClickListeners();
        loadUserInfo();

        return view;
    }

    private void initViews(View view) {
        tvUsername = view.findViewById(R.id.tv_username);
        tvEmail = view.findViewById(R.id.tv_email);
        tvOrderCount = view.findViewById(R.id.tv_order_count);
        btnOrderHistory = view.findViewById(R.id.btn_order_history);
        btnSettings = view.findViewById(R.id.btn_settings);
        btnAddShoe = view.findViewById(R.id.btn_add_shoe);
    }

    private void setupClickListeners() {
        btnOrderHistory.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new OrderHistoryFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        btnSettings.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new SettingsFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        btnAddShoe.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddShoeActivity.class);
            startActivity(intent);
        });
    }

    private void loadUserInfo() {
        String username = prefsHelper.getUsername();
        String email = prefsHelper.getEmail();

        tvUsername.setText(username.isEmpty() ? "未知用户" : username);
        tvEmail.setText(email.isEmpty() ? "未设置邮箱" : email);
        tvOrderCount.setText("历史订单：5 笔");
    }
}