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

import com.example.appmanager.Interface.ProductActionListener;
import com.example.appmanager.Model.Product;
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

public class UpdateProductActivity extends AppCompatActivity {
    TextInputEditText txtNameUpdate, txtUrl_ImageUpdate, txtPriceUpdate, txtInfoUpdate, txtInventoryQuantityUpdate, txtCategoryUpdate;
    AppCompatButton btnUpdate;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiClothing apiClothing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        initControl();
    }

    private void initControl() {
        Product product = (Product) getIntent().getSerializableExtra("product");
        if (product != null) {
            txtNameUpdate.setText(product.getName());
            txtUrl_ImageUpdate.setText(product.getUrl_img());
            txtPriceUpdate.setText(product.getPrice());
            txtInfoUpdate.setText(product.getInfo());
            txtInventoryQuantityUpdate.setText(String.valueOf(product.getInventory_quantity()));
            txtCategoryUpdate.setText(product.getCategory());
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = txtNameUpdate.getText().toString();
                    String url = txtUrl_ImageUpdate.getText().toString();
                    String price = txtPriceUpdate.getText().toString();
                    String info = txtInfoUpdate.getText().toString();
                    int inventory = Integer.parseInt(txtInventoryQuantityUpdate.getText().toString());
                    String category = txtCategoryUpdate.getText().toString();
                    compositeDisposable.add(apiClothing.updateProduct(product.getId(), name, url, price, info, inventory, category)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    resultModel -> {
                                        // Xử lý thành công
                                        if (resultModel.isSuccess()) {
                                            MotionToast.Companion.createToast(
                                                    UpdateProductActivity.this,
                                                    "Notice",
                                                    resultModel.getMessage(),
                                                    MotionToastStyle.SUCCESS,
                                                    MotionToast.GRAVITY_BOTTOM,
                                                    MotionToast.SHORT_DURATION,
                                                    Typeface.defaultFromStyle(R.font.helvetica_regular)
                                            );
                                            finish();
                                        } else {
                                            MotionToast.Companion.createToast(
                                                    UpdateProductActivity.this,
                                                    "Notice",
                                                    resultModel.getMessage(),
                                                    MotionToastStyle.ERROR,
                                                    MotionToast.GRAVITY_BOTTOM,
                                                    MotionToast.SHORT_DURATION,
                                                    Typeface.defaultFromStyle(R.font.helvetica_regular)
                                            );
                                        }
                                    },
                                    throwable -> {
                                        // Xử lý lỗi
                                        MotionToast.Companion.createToast(
                                                UpdateProductActivity.this,
                                                "Error",
                                                throwable.getMessage(),
                                                MotionToastStyle.ERROR,
                                                MotionToast.GRAVITY_BOTTOM,
                                                MotionToast.SHORT_DURATION,
                                                Typeface.defaultFromStyle(R.font.helvetica_regular)
                                        );
                                    }
                            )
                    );
                }
            });
        }
    }

    private void initView() {
        apiClothing = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiClothing.class);
        txtNameUpdate = findViewById(R.id.txtNameUpdate);
        txtUrl_ImageUpdate = findViewById(R.id.txtUrlImageUpdate);
        txtPriceUpdate = findViewById(R.id.txtPriceUpdate);
        txtInfoUpdate = findViewById(R.id.txtInfoUpdate);
        txtInventoryQuantityUpdate = findViewById(R.id.txtInventoryUpdate);
        txtCategoryUpdate = findViewById(R.id.txtCategoryUpdate);
        btnUpdate = findViewById(R.id.btnUpdate);
    }
}