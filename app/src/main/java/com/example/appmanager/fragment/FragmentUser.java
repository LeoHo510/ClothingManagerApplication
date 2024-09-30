package com.example.appmanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanager.Model.User;
import com.example.appmanager.R;
import com.example.appmanager.adapter.UserAdapter;
import com.example.appmanager.retrofit.ApiClothing;
import com.example.appmanager.retrofit.RetrofitClient;
import com.example.appmanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FragmentUser extends Fragment {
    RecyclerView recyclerView;
    UserAdapter adapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiClothing apiClothing;
    List<User> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = new ArrayList<>();
        apiClothing = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiClothing.class);
        recyclerView = view.findViewById(R.id.userRecycleView);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        adapter = new UserAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
        getUser();
    }

    private void getUser() {
        compositeDisposable.add(apiClothing.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()) {
                                list.clear();
                                list.addAll(userModel.getResult());
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }
}
