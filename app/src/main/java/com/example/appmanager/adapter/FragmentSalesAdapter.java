package com.example.appmanager.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.appmanager.fragment.FragmentAddSales;
import com.example.appmanager.fragment.FragmentViewSales;

public class FragmentSalesAdapter extends FragmentStatePagerAdapter {
    public FragmentSalesAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new FragmentViewSales();
            case 1: return new FragmentAddSales();
            default: return new FragmentViewSales();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
