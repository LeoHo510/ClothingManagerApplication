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
import com.example.appmanager.adapter.FragmentSalesAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentSales extends Fragment {
    BottomNavigationView salesNavi;
    ViewPager viewPager;
    FragmentSalesAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sales_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        salesNavi = view.findViewById(R.id.salesNavigation);
        viewPager = view.findViewById(R.id.viewPagerSales);
        adapter = new FragmentSalesAdapter(getChildFragmentManager(), 2);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: salesNavi.getMenu().findItem(R.id.sales);
                            break;
                    case 1: salesNavi.getMenu().findItem(R.id.addSales);
                            break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        salesNavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.sales) {
                    viewPager.setCurrentItem(0);
                } else if (menuItem.getItemId() == R.id.addSales) {
                    viewPager.setCurrentItem(1);
                }
                return true;
            }
        });
    }
}
