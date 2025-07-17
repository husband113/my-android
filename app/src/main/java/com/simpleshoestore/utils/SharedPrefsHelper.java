package com.simpleshoestore.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {
    private static final String PREF_NAME = "ShoeStorePrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_REMEMBER_PASSWORD = "remember_password";
    private static final String KEY_REMEMBERED_USERNAME = "remembered_username";
    private static final String KEY_REMEMBERED_PASSWORD = "remembered_password";
    private static final String KEY_NOTIFICATIONS_ENABLED = "notifications_enabled";
    private static final String KEY_AUTO_LOGIN_ENABLED = "auto_login_enabled";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SharedPrefsHelper(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveUserInfo(String username, String email) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public void logout() {
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_EMAIL);
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, "");
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }

    // 记住密码功能
    public void saveRememberedCredentials(String username, String password) {
        editor.putBoolean(KEY_REMEMBER_PASSWORD, true);
        editor.putString(KEY_REMEMBERED_USERNAME, username);
        editor.putString(KEY_REMEMBERED_PASSWORD, password);
        editor.apply();
    }

    public void clearRememberedCredentials() {
        editor.putBoolean(KEY_REMEMBER_PASSWORD, false);
        editor.remove(KEY_REMEMBERED_USERNAME);
        editor.remove(KEY_REMEMBERED_PASSWORD);
        editor.apply();
    }

    public boolean isRememberPassword() {
        return prefs.getBoolean(KEY_REMEMBER_PASSWORD, false);
    }

    public String getRememberedUsername() {
        return prefs.getString(KEY_REMEMBERED_USERNAME, "");
    }

    public String getRememberedPassword() {
        return prefs.getString(KEY_REMEMBERED_PASSWORD, "");
    }

    // 设置功能
    public void setNotificationsEnabled(boolean enabled) {
        editor.putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled);
        editor.apply();
    }

    public boolean isNotificationsEnabled() {
        return prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true);
    }

    public void setAutoLoginEnabled(boolean enabled) {
        editor.putBoolean(KEY_AUTO_LOGIN_ENABLED, enabled);
        editor.apply();
    }

    public boolean isAutoLoginEnabled() {
        return prefs.getBoolean(KEY_AUTO_LOGIN_ENABLED, false);
    }
}