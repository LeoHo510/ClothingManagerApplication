package com.example.appmanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.appmanager.Model.Cate;
import com.example.appmanager.R;
import com.example.appmanager.adapter.CateApdapter;
import com.example.appmanager.retrofit.ApiClothing;
import com.example.appmanager.retrofit.RetrofitClient;
import com.example.appmanager.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import www.sanju.motiontoast.MotionToast;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ImageView btnSearch;
    NavigationView navigationView;
    ListView listView;
    CateApdapter apdapter;
    List<Cate> list;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiClothing apiClothing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (isConnect(getApplicationContext())){
            initData();
            getSupportToolBar();
            getEventClick();
        }
    }
    
    private void getEventClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent update = new Intent(getApplicationContext(), UpdateActivity.class);
                        startActivity(update);
                        break;
                    case 1:
                        Intent statistics = new Intent(getApplicationContext(), StatisticsActivity.class);
                        startActivity(statistics);
                        break;
                }
            }
        });
    }

    private void getSupportToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.drawable.ic_list);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void initData() {
        compositeDisposable.add(apiClothing.getCate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        cateModel -> {
                            if (cateModel.isSuccess()) {
                                list.clear();
                                list.addAll(cateModel.getResult());
                                apdapter.notifyDataSetChanged();
                                Log.d("GetCate", new Gson().toJson(cateModel.getResult()));
                            } else {
                                Log.e("GetCate", "No data received");
                            }
                        },
                        throwable -> {
                            Log.e("GetCate", "Error: " + throwable.getMessage());
                        }
                ));
    }
    private void initView() {
        apiClothing = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiClothing.class);
        toolbar = findViewById(R.id.mainToolBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        btnSearch = findViewById(R.id.btnSearch);
        navigationView = findViewById(R.id.navigationview);
        listView = findViewById(R.id.mainListView);
        list = new ArrayList<>();
        apdapter = new CateApdapter(getApplicationContext(), list);
        listView.setAdapter(apdapter);

    }

    public boolean isConnect(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected());
    }
}
