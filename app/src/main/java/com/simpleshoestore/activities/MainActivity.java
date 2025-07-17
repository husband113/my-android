package com.simpleshoestore.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.simpleshoestore.R;
import com.simpleshoestore.fragments.HomeFragment;
import com.simpleshoestore.fragments.CartFragment;
import com.simpleshoestore.fragments.ProfileFragment;
import com.simpleshoestore.fragments.ChatFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化顶部导航栏（Toolbar）
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("鞋类商店"); // 设置标题

        bottomNavigation = findViewById(R.id.bottom_navigation);
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
        setupBottomNavigation();
    }

    // 加载菜单资源（顶部导航栏按钮）
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu); // 需创建menu文件
        return true;
    }

    // 顶部导航栏按钮点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cart) { // 购物车按钮
            loadFragment(new CartFragment());
            return true;
        } else if (id == R.id.action_settings) { // 设置按钮
            // 跳转设置页面
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                fragment = new HomeFragment();
                getSupportActionBar().setTitle("鞋类商店"); // 切换标题
            } else if (itemId == R.id.nav_cart) {
                fragment = new CartFragment();
                getSupportActionBar().setTitle("购物车");
            } else if (itemId == R.id.nav_chat) {
                fragment = new ChatFragment();
                getSupportActionBar().setTitle("客服中心");
            } else if (itemId == R.id.nav_profile) {
                fragment = new ProfileFragment();
                getSupportActionBar().setTitle("个人中心");
            }
            return loadFragment(fragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}