package com.example.appmanager.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.appmanager.fragment.FragmentAddProduct;
import com.example.appmanager.fragment.FragmentViewProduct;

public class FragmentProductAdapter extends FragmentStatePagerAdapter {
    public FragmentProductAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new FragmentViewProduct();
            case 1: return new FragmentAddProduct();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
