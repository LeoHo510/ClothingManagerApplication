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
import com.example.appmanager.adapter.FragmentProductAdapter;
import com.example.appmanager.retrofit.ApiClothing;
import com.example.appmanager.retrofit.RetrofitClient;
import com.example.appmanager.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class FragmentProduct extends Fragment {
    BottomNavigationView navigationView;
    ViewPager viewPager;
    FragmentProductAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View v) {
        navigationView = v.findViewById(R.id.productNavi);
        viewPager = v.findViewById(R.id.viewPagerProduct);
        adapter = new FragmentProductAdapter(getChildFragmentManager(), 2);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: navigationView.getMenu().findItem(R.id.productMenu);
                            break;
                    case 1: navigationView.getMenu().findItem(R.id.addProduct);
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
                if (menuItem.getItemId() == R.id.productMenu) {
                    viewPager.setCurrentItem(0);
                } else if (menuItem.getItemId() == R.id.addProduct) {
                    viewPager.setCurrentItem(1);
                }
                return true;
            }
        });
    }
}
