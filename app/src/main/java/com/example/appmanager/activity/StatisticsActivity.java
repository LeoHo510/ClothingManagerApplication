package com.example.appmanager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.appmanager.R;
import com.example.appmanager.adapter.StatisticsFragmentAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StatisticsActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    StatisticsFragmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        initView();
    }

    private void initView() {
        bottomNavigationView = findViewById(R.id.reportNavi);
        viewPager = findViewById(R.id.reportViewPager);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.sales_report) {
                    viewPager.setCurrentItem(0);
                } else if (menuItem.getItemId() == R.id.sales_revenue_report) {
                    viewPager.setCurrentItem(1);
                }
                return true;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: bottomNavigationView.getMenu().findItem(R.id.sales_report);
                            break;
                    case 1: bottomNavigationView.getMenu().findItem(R.id.sales_revenue_report);
                            break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapter = new StatisticsFragmentAdapter(getSupportFragmentManager(), 2);
        viewPager.setAdapter(adapter);
    }
}