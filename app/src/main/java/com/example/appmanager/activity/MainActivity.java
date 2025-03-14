package com.example.appmanager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.appmanager.Model.Cate;
import com.example.appmanager.R;
import com.example.appmanager.adapter.CateApdapter;
import com.example.appmanager.adapter.FragmentAdapter;
import com.example.appmanager.retrofit.ApiClothing;
import com.example.appmanager.retrofit.RetrofitClient;
import com.example.appmanager.utils.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import www.sanju.motiontoast.MotionToast;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigationView;
    ViewPager viewPager;
    FragmentAdapter adapter;
    ImageView logout;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiClothing apiClothing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isConnect(getApplicationContext())){
            initView();
        }
    }

    private void initView() {
        apiClothing = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiClothing.class);
        logout = findViewById(R.id.logout_icon);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.user = null;
                Paper.book().delete("user_current");
                FirebaseAuth.getInstance().signOut();
                Intent logout = new Intent(getApplicationContext(), StartActivity2.class);
                startActivity(logout);
                finish();
            }
        });
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
                    case 4: navigationView.getMenu().findItem(R.id.statistics);
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
                } else if (menuItem.getItemId() == R.id.statistics) {
                    viewPager.setCurrentItem(4);
                }
                return true;
            }
        });
        if(Paper.book().contains("user_current")) {
            Utils.user = Paper.book().read("user_current");
            getToken();
        }
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String string) {
                        if (!TextUtils.isEmpty(string)) {
                            compositeDisposable.add(apiClothing.updateToken(Utils.user.getIduser(), string)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe());
                        }
                    }
                });
    }

    public boolean isConnect(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected());
    }
}
