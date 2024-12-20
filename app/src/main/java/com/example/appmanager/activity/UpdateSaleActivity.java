
package com.example.appmanager.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appmanager.Model.Sales;
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

public class UpdateSaleActivity extends AppCompatActivity {
    TextInputEditText info, url;
    AppCompatButton btnUpdate;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiClothing apiClothing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_sale);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        initControl();
    }

    private void initControl() {
        Sales sales = (Sales) getIntent().getSerializableExtra("sale");
        if (sales != null) {
            info.setText(sales.getInfo());
            url.setText(sales.getUrl());
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String i = info.getText().toString();
                    String u = url.getText().toString();
                    compositeDisposable.add(apiClothing.updateSale(sales.getId(), u, i)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    resultModel -> {
                                        if (resultModel.isSuccess()) {
                                            MotionToast.Companion.createToast(
                                                    UpdateSaleActivity.this,
                                                    "Notice",
                                                    resultModel.getMessage(),
                                                    MotionToastStyle.SUCCESS,
                                                    MotionToast.GRAVITY_BOTTOM,
                                                    MotionToast.SHORT_DURATION,
                                                    Typeface.defaultFromStyle(R.font.helvetica_regular)
                                            );
                                            finish();
                                        }
                                    },
                                    throwable -> {
                                        MotionToast.Companion.createToast(
                                                UpdateSaleActivity.this,
                                                "Notice",
                                                throwable.getMessage(),
                                                MotionToastStyle.ERROR,
                                                MotionToast.GRAVITY_BOTTOM,
                                                MotionToast.SHORT_DURATION,
                                                Typeface.defaultFromStyle(R.font.helvetica_regular)
                                        );
                                    }
                            ));
                }
            });
        }
    }

    private void initView() {
        apiClothing = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiClothing.class);
        info = findViewById(R.id.txtInfoSalesUpdate);
        url = findViewById(R.id.txtImageSalesUpdate);
        btnUpdate = findViewById(R.id.btnUpdateSales);
    }
}