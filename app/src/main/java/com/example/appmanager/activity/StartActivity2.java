package com.example.appmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.appmanager.R;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class StartActivity2 extends AppCompatActivity {
    ImageSlider imageSlider;
    AppCompatButton btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        initControl();
    }

    private void initControl() {
        List<SlideModel> list = new ArrayList<>();
        list.add(new SlideModel(R.drawable.product_template, null));
        list.add(new SlideModel(R.drawable.product_template_2, null));
        imageSlider.setImageList(list, ScaleTypes.FIT);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signin = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(signin);
                finish();
            }
        });
    }

    private void initView() {
        Paper.init(this);
        imageSlider = findViewById(R.id.startSlider);
        btnSignIn = findViewById(R.id.btnSignIn);
    }
}