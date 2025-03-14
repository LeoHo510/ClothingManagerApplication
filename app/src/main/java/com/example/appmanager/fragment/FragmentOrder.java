package com.example.appmanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanager.Model.Order;
import com.example.appmanager.R;
import com.example.appmanager.adapter.OrderAdapter;
import com.example.appmanager.retrofit.ApiClothing;
import com.example.appmanager.retrofit.RetrofitClient;
import com.example.appmanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FragmentOrder extends Fragment {
    RecyclerView recyclerView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiClothing apiClothing;
    OrderAdapter adapter;
    List<Order> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiClothing = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiClothing.class);
        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.orderRecycleView);
        adapter = new OrderAdapter(view.getContext(), list);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        compositeDisposable.add(apiClothing.getOrder(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        orderModel -> {
                            if(orderModel.isSuccess()) {
                                list.clear();
                                list.addAll(orderModel.getList());
                                adapter.notifyDataSetChanged();
                            }
                        },
                        throwable -> {

                        }
                ));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.dispose();
        }
    }
}
