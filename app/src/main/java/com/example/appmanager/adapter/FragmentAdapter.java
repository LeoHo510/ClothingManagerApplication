package com.example.appmanager.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.appmanager.fragment.FragmentOrder;
import com.example.appmanager.fragment.FragmentProduct;
import com.example.appmanager.fragment.FragmentSales;
import com.example.appmanager.fragment.FragmentStatistics;
import com.example.appmanager.fragment.FragmentUser;

public class FragmentAdapter extends FragmentStatePagerAdapter {
    public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new FragmentProduct();
            case 1: return new FragmentOrder();
            case 2: return new FragmentSales();
            case 3: return new FragmentUser();
            case 4: return new FragmentStatistics();
            default: return new FragmentProduct();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
