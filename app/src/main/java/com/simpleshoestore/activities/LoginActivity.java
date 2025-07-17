package com.simpleshoestore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.simpleshoestore.R;
import com.simpleshoestore.utils.SharedPrefsHelper;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etPhone, etVerifyCode;
    private Button btnLogin, btnSendCode, btnVerifyLogin, btnRegister;
    private CheckBox cbRememberPassword;
    private TextView tvSwitchMode, tvForgotPassword;
    private boolean isLoginMode = true;
    private String sentVerifyCode = "";
    private SharedPrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefsHelper = new SharedPrefsHelper(this);

        // 检查是否已登录
        if (prefsHelper.isLoggedIn()) {
            startMainActivity();
            return;
        }

        initViews();
        setupClickListeners();
        loadRememberedCredentials();
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etPhone = findViewById(R.id.et_phone);
        etVerifyCode = findViewById(R.id.et_verify_code);
        btnLogin = findViewById(R.id.btn_login);
        btnSendCode = findViewById(R.id.btn_send_code);
        btnVerifyLogin = findViewById(R.id.btn_verify_login);
        btnRegister = findViewById(R.id.btn_register);
        cbRememberPassword = findViewById(R.id.cb_remember_password);
        tvSwitchMode = findViewById(R.id.tv_switch_mode);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);

        updateUIMode();
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> performLogin());
        btnSendCode.setOnClickListener(v -> sendVerifyCode());
        btnVerifyLogin.setOnClickListener(v -> performVerifyLogin());
        btnRegister.setOnClickListener(v -> performRegister());
        tvSwitchMode.setOnClickListener(v -> switchMode());
        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "请联系客服找回密码", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadRememberedCredentials() {
        if (prefsHelper.isRememberPassword()) {
            etUsername.setText(prefsHelper.getRememberedUsername());
            etPassword.setText(prefsHelper.getRememberedPassword());
            cbRememberPassword.setChecked(true);
        }
    }

    private void switchMode() {
        isLoginMode = !isLoginMode;
        updateUIMode();
    }

    private void updateUIMode() {
        if (isLoginMode) {
            findViewById(R.id.layout_normal_login).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_sms_login).setVisibility(View.GONE);
            findViewById(R.id.layout_register).setVisibility(View.GONE);
            tvSwitchMode.setText("验证码登录");
        } else {
            findViewById(R.id.layout_normal_login).setVisibility(View.GONE);
            findViewById(R.id.layout_sms_login).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_register).setVisibility(View.VISIBLE);
            tvSwitchMode.setText("账号密码登录");
        }
    }

    private void performLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        // 简单验证（实际项目中需要服务器验证）
        if (username.equals("admin") && password.equals("123456")) {
            // 保存登录状态
            prefsHelper.saveUserInfo(username, username + "@example.com");

            // 处理记住密码
            if (cbRememberPassword.isChecked()) {
                prefsHelper.saveRememberedCredentials(username, password);
            } else {
                prefsHelper.clearRememberedCredentials();
            }

            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            startMainActivity();
        } else {
            Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendVerifyCode() {
        String phone = etPhone.getText().toString().trim();
        if (phone.isEmpty() || phone.length() != 11) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        // 生成随机验证码
        sentVerifyCode = String.format("%04d", new Random().nextInt(10000));
        Toast.makeText(this, "验证码：" + sentVerifyCode + "（仅用于演示）", Toast.LENGTH_LONG).show();

        // 开始倒计时
        startCountDown();
    }

    private void startCountDown() {
        btnSendCode.setEnabled(false);
        new Thread(() -> {
            for (int i = 60; i > 0; i--) {
                int finalI = i;
                runOnUiThread(() -> btnSendCode.setText(finalI + "s"));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(() -> {
                btnSendCode.setText("发送验证码");
                btnSendCode.setEnabled(true);
            });
        }).start();
    }

    private void performVerifyLogin() {
        String phone = etPhone.getText().toString().trim();
        String code = etVerifyCode.getText().toString().trim();

        if (phone.isEmpty() || code.isEmpty()) {
            Toast.makeText(this, "请输入手机号和验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (code.equals(sentVerifyCode)) {
            prefsHelper.saveUserInfo("用户" + phone.substring(7), phone + "@example.com");
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            startMainActivity();
        } else {
            Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
        }
    }

    private void performRegister() {
        String phone = etPhone.getText().toString().trim();
        String code = etVerifyCode.getText().toString().trim();

        if (phone.isEmpty() || code.isEmpty()) {
            Toast.makeText(this, "请完善注册信息", Toast.LENGTH_SHORT).show();
            return;
        }

        if (code.equals(sentVerifyCode)) {
            prefsHelper.saveUserInfo("新用户" + phone.substring(7), phone + "@example.com");
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            startMainActivity();
        } else {
            Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}