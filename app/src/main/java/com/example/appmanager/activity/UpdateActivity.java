package com.example.appmanager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.appmanager.R;
import com.example.appmanager.adapter.FragmentAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class UpdateActivity extends AppCompatActivity {
    BottomNavigationView navigationView;
    ViewPager viewPager;
    FragmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initView();
    }

    private void initView() {
        navigationView = findViewById(R.id.navigationView);
        viewPager = findViewById(R.id.viewPager);
        adapter = new FragmentAdapter(getSupportFragmentManager(), 4);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: navigationView.getMenu().findItem(R.id.productManager);
                            break;
                    case 1: navigationView.getMenu().findItem(R.id.orderManager);
                            break;
                    case 2: navigationView.getMenu().findItem(R.id.saleManager);
                            break;
                    case 3: navigationView.getMenu().findItem(R.id.userManager);
                            break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.productManager) {
                    viewPager.setCurrentItem(0);
                } else if (menuItem.getItemId() == R.id.orderManager) {
                    viewPager.setCurrentItem(1);
                } else if (menuItem.getItemId() == R.id.saleManager) {
                    viewPager.setCurrentItem(2);
                } else if (menuItem.getItemId() == R.id.userManager) {
                    viewPager.setCurrentItem(3);
                }
                return true;
            }
        });
    }
}