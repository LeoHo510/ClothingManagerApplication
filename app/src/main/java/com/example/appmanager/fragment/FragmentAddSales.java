package com.example.appmanager.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.appmanager.R;
import com.example.appmanager.retrofit.ApiClothing;
import com.example.appmanager.retrofit.RetrofitClient;
import com.example.appmanager.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class FragmentAddSales extends Fragment {
    TextInputEditText info, url;
    AppCompatButton btnAdd;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiClothing apiClothing;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_sales_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initControl();
    }

    private void initControl() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String infoSales = info.getText().toString();
                String urlSales = url.getText().toString();

                if (infoSales.isEmpty()) {
                    throw new IllegalArgumentException("Don't left blank info");
                } else if (urlSales.isEmpty()) {
                    throw new IllegalArgumentException("Don't left blank image");
                } else {
                    compositeDisposable.add(apiClothing.addsale(urlSales, infoSales)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    resultModel -> {
                                        if (resultModel.isSuccess()) {
                                            MotionToast.Companion.createToast((Activity) getContext(),
                                                    "Notice",
                                                    "Success",
                                                    MotionToastStyle.SUCCESS,
                                                    MotionToast.GRAVITY_BOTTOM,
                                                    MotionToast.SHORT_DURATION,
                                                    ResourcesCompat.getFont(getContext(), R.font.helvetica_regular));
                                            url.setText("");
                                            info.setText("");
                                        }
                                    },
                                    throwable -> {
                                        MotionToast.Companion.createToast((Activity) getContext(),
                                                "Notice",
                                                "Error",
                                                MotionToastStyle.ERROR,
                                                MotionToast.GRAVITY_BOTTOM,
                                                MotionToast.SHORT_DURATION,
                                                ResourcesCompat.getFont(getContext(), R.font.helvetica_regular));
                                    }
                            ));
                }
            }
        });
    }

    private void initView(View v) {
        apiClothing = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiClothing.class);
        info = v.findViewById(R.id.txtInfo);
        url = v.findViewById(R.id.txtImageSales);
        btnAdd = v.findViewById(R.id.btnAddSales);
    }
}
