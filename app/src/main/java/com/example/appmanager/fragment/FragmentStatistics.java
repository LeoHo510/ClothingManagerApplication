package com.example.appmanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.appmanager.R;
import com.example.appmanager.adapter.StatisticsFragmentAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentStatistics extends Fragment {
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    StatisticsFragmentAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        bottomNavigationView = view.findViewById(R.id.reportNavi);
        viewPager = view.findViewById(R.id.reportViewPager);
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
        adapter = new StatisticsFragmentAdapter(getChildFragmentManager(), 2);
        viewPager.setAdapter(adapter);
    }
}
