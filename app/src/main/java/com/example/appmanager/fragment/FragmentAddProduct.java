package com.example.appmanager.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

public class FragmentAddProduct extends Fragment {
    TextInputEditText txtName, txtUrlImage, txtPrice, txtInfo, txtInventoryQuantity, txtCategory;
    AppCompatButton btnAdd;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiClothing apiClothing;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_product_layout, container, false);
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
                try {
                    String name = txtName.getText().toString();
                    String image = txtUrlImage.getText().toString();
                    String price = txtPrice.getText().toString();
                    String info = txtInfo.getText().toString();
                    String inventory = txtInventoryQuantity.getText().toString();
                    String category = txtCategory.getText().toString();

                    if (name.isEmpty()) {
                        throw new IllegalArgumentException("Don't left blank name");
                    } else if (image.isEmpty()) {
                        throw new IllegalArgumentException("Don't left blank image");
                    } else if (price.isEmpty()) {
                        throw new IllegalArgumentException("Don't left blank price");
                    } else if (info.isEmpty()) {
                        throw new IllegalArgumentException("Don't left blank info");
                    } else if (inventory.isEmpty()) {
                        throw new IllegalArgumentException("Don't left blank inventory");
                    } else if (category.isEmpty()) {
                        throw new IllegalArgumentException("Don't left blank category");
                    } else {
                        compositeDisposable.add(apiClothing.addProduct(name, image, price, info, Integer.parseInt(inventory), category)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        productModel -> {
                                            if (productModel.isSuccess()) {
                                                 MotionToast.Companion.createToast((Activity) getContext(),
                                                         "Notice",
                                                         "Success",
                                                         MotionToastStyle.SUCCESS,
                                                         MotionToast.GRAVITY_BOTTOM,
                                                         MotionToast.SHORT_DURATION,
                                                         ResourcesCompat.getFont(getContext(), R.font.helvetica_regular));
                                                 txtName.setText("");
                                                 txtCategory.setText("");
                                                 txtInfo.setText("");
                                                 txtPrice.setText("");
                                                 txtUrlImage.setText("");
                                                 txtInventoryQuantity.setText("");
                                            }
                                        },
                                        throwable -> {
                                            Log.d("Error", throwable.getMessage());
                                            MotionToast.Companion.createToast((Activity) getContext(),
                                                    "Notice",
                                                    throwable.getMessage(),
                                                    MotionToastStyle.ERROR,
                                                    MotionToast.GRAVITY_BOTTOM,
                                                    MotionToast.SHORT_DURATION,
                                                    ResourcesCompat.getFont(getContext(), R.font.helvetica_regular));
                                        }
                                ));
                    }
                } catch (IllegalArgumentException e) {
                    MotionToast.Companion.createToast((Activity) getContext(),
                            "Notice",
                            e.getMessage(),
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(getContext(), R.font.helvetica_regular));
                }
            }
        });
    }

    private void initView(View view) {
        apiClothing = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiClothing.class);
        txtName = view.findViewById(R.id.txtNameProduct);
        txtUrlImage = view.findViewById(R.id.txtUrlImage);
        txtPrice = view.findViewById(R.id.txtPrice);
        txtInfo = view.findViewById(R.id.txtInfo);
        txtInventoryQuantity = view.findViewById(R.id.txtInventory);
        txtCategory = view.findViewById(R.id.txtCategory);
        btnAdd = view.findViewById(R.id.btnAdd);
    }
}
