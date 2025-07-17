package com.simpleshoestore.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.simpleshoestore.R;
import com.simpleshoestore.activities.LoginActivity;
import com.simpleshoestore.utils.SharedPrefsHelper;
import com.simpleshoestore.utils.CartManager;

public class SettingsFragment extends Fragment {

    private LinearLayout llAccountInfo, llNotifications, llPrivacy, llAbout, llLogout, llClearCache;
    private Switch switchNotifications, switchAutoLogin;
    private TextView tvVersion, tvCacheSize;
    private SharedPrefsHelper prefsHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        prefsHelper = new SharedPrefsHelper(getContext());
        initViews(view);
        setupClickListeners();
        loadSettings();

        return view;
    }

    private void initViews(View view) {
        llAccountInfo = view.findViewById(R.id.ll_account_info);
        llNotifications = view.findViewById(R.id.ll_notifications);
        llPrivacy = view.findViewById(R.id.ll_privacy);
        llAbout = view.findViewById(R.id.ll_about);
        llLogout = view.findViewById(R.id.ll_logout);
        llClearCache = view.findViewById(R.id.ll_clear_cache);

        switchNotifications = view.findViewById(R.id.switch_notifications);
        switchAutoLogin = view.findViewById(R.id.switch_auto_login);

        tvVersion = view.findViewById(R.id.tv_version);
        tvCacheSize = view.findViewById(R.id.tv_cache_size);
    }

    private void setupClickListeners() {
        llAccountInfo.setOnClickListener(v -> {
            showAccountInfoDialog();
        });

        llNotifications.setOnClickListener(v -> {
            switchNotifications.toggle();
        });

        llPrivacy.setOnClickListener(v -> {
            showPrivacyPolicy();
        });

        llAbout.setOnClickListener(v -> {
            showAboutDialog();
        });

        llClearCache.setOnClickListener(v -> {
            showClearCacheDialog();
        });

        llLogout.setOnClickListener(v -> {
            showLogoutDialog();
        });

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefsHelper.setNotificationsEnabled(isChecked);
            Toast.makeText(getContext(), isChecked ? "通知已开启" : "通知已关闭", Toast.LENGTH_SHORT).show();
        });

        switchAutoLogin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefsHelper.setAutoLoginEnabled(isChecked);
            Toast.makeText(getContext(), isChecked ? "自动登录已开启" : "自动登录已关闭", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadSettings() {
        switchNotifications.setChecked(prefsHelper.isNotificationsEnabled());
        switchAutoLogin.setChecked(prefsHelper.isAutoLoginEnabled());
        tvVersion.setText("版本 1.0.0");
        tvCacheSize.setText("缓存大小: 12.5MB");
    }

    private void showAccountInfoDialog() {
        String username = prefsHelper.getUsername();
        String email = prefsHelper.getEmail();

        String message = "用户名：" + username + "\n" +
                "邮箱：" + email + "\n" +
                "注册时间：2024-01-01";

        new AlertDialog.Builder(getContext())
                .setTitle("账户信息")
                .setMessage(message)
                .setPositiveButton("确定", null)
                .setNeutralButton("编辑", (dialog, which) -> {
                    Toast.makeText(getContext(), "编辑功能开发中", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void showPrivacyPolicy() {
        String privacyText = "隐私政策\n\n" +
                "1. 信息收集\n" +
                "我们收集您主动提供的信息，包括用户名、邮箱等基本信息。\n\n" +
                "2. 信息使用\n" +
                "我们使用收集的信息来提供和改进我们的服务。\n\n" +
                "3. 信息保护\n" +
                "我们采用适当的安全措施来保护您的个人信息。\n\n" +
                "4. 信息共享\n" +
                "我们不会向第三方出售、交易或转让您的个人信息。";

        new AlertDialog.Builder(getContext())
                .setTitle("隐私政策")
                .setMessage(privacyText)
                .setPositiveButton("确定", null)
                .show();
    }

    private void showAboutDialog() {
        String aboutText = "鞋店应用 v1.0.0\n\n" +
                "这是一款专业的鞋类购物应用，提供优质的购物体验。\n\n" +
                "主要功能：\n" +
                "• 商品浏览和搜索\n" +
                "• 购物车管理\n" +
                "• AI智能客服\n" +
                "• 订单管理\n" +
                "• 商品上架\n\n" +
                "开发团队：鞋店开发小组\n" +
                "联系邮箱：support@shoestore.com";

        new AlertDialog.Builder(getContext())
                .setTitle("关于应用")
                .setMessage(aboutText)
                .setPositiveButton("确定", null)
                .setNeutralButton("检查更新", (dialog, which) -> {
                    Toast.makeText(getContext(), "当前已是最新版本", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void showClearCacheDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("清除缓存")
                .setMessage("确定要清除应用缓存吗？这将删除临时文件和图片缓存。")
                .setPositiveButton("确定", (dialog, which) -> {
                    // 模拟清除缓存过程
                    clearAppCache();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void clearAppCache() {
        // 模拟清除缓存的过程
        new Thread(() -> {
            try {
                // 模拟清除时间
                Thread.sleep(1000);

                // 更新UI
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        tvCacheSize.setText("缓存大小: 0MB");
                        Toast.makeText(getContext(), "缓存已清除", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("退出登录")
                .setMessage("确定要退出登录吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    performLogout();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void performLogout() {
        // 清除用户数据
        prefsHelper.logout();
        CartManager.getInstance().clearCart();

        // 跳转到登录页面
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        Toast.makeText(getContext(), "已退出登录", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 刷新设置状态
        loadSettings();
    }
}