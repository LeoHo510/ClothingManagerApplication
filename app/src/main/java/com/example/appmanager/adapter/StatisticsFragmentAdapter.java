package com.example.appmanager.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.appmanager.fragment.FragmentRevenueReport;
import com.example.appmanager.fragment.FragmentSalesReport;

public class StatisticsFragmentAdapter extends FragmentStatePagerAdapter {
    public StatisticsFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new FragmentSalesReport();
            case 1: return new FragmentRevenueReport();
            default: return new FragmentSalesReport();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
