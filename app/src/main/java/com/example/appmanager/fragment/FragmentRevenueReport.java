package com.example.appmanager.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appmanager.R;
import com.example.appmanager.retrofit.ApiClothing;
import com.example.appmanager.retrofit.RetrofitClient;
import com.example.appmanager.utils.Utils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FragmentRevenueReport extends Fragment {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiClothing apiClothing;
    BarChart barChart;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.revenue_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        revenueReport();
    }

    private void initView(View v) {
        apiClothing = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiClothing.class);
        barChart = v.findViewById(R.id.barChart);
    }

    private void revenueReport() {
        settingBarChart();
        compositeDisposable.add(apiClothing.revenueReport()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        revenueReportModel -> {
                            if (revenueReportModel.isSuccess() && !revenueReportModel.getResult().isEmpty()) {
                                List<BarEntry> listData = new ArrayList<>();

                                // Kiểm tra dữ liệu trả về
                                for (int i = 0; i < revenueReportModel.getResult().size(); i++) {
                                    int thang = revenueReportModel.getResult().get(i).getMonth();
                                    float tongdoanhthu = revenueReportModel.getResult().get(i).getTotal_price();
                                    listData.add(new BarEntry(thang, tongdoanhthu));

                                    // In log để kiểm tra dữ liệu
                                    Log.d("API_DATA", "Month: " + thang + ", Total Revenue: " + tongdoanhthu);
                                }

                                if (!listData.isEmpty()) {
                                    BarDataSet barDataSet = new BarDataSet(listData, "Revenue Sales Report");
                                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                    barDataSet.setValueTextSize(12f);
                                    barDataSet.setValueTextColor(Color.BLACK);

                                    BarData data = new BarData(barDataSet);
                                    barChart.setData(data);
                                    barChart.invalidate();  // Cập nhật biểu đồ
                                    barChart.animateXY(1000, 1000);
                                } else {
                                    // Nếu dữ liệu trống
                                    Log.d("API_DATA", "No valid data to display");
                                    barChart.clear();
                                    barChart.invalidate();
                                }
                            } else {
                                Log.d("API_DATA", "No data available from API or API failed");
                            }
                        },
                        throwable -> {
                            // Xử lý lỗi nếu có
                            Log.e("API_ERROR", "Error fetching data: " + throwable.getMessage());
                        }
                ));
    }

    private void settingBarChart() {
        barChart.getDescription().setEnabled(true);
        barChart.setDrawValueAboveBar(true); // Để giá trị hiển thị trên cột
        barChart.setFitBars(true); // Đảm bảo các cột vừa khít với biểu đồ

        // Cấu hình trục X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setAxisMinimum(1);
        xAxis.setAxisMaximum(12);  // Đặt phạm vi từ tháng 1 đến tháng 12
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Đặt trục X ở dưới
        xAxis.setGranularity(1f); // Bước nhảy giữa các giá trị
        xAxis.setDrawGridLines(false); // Tắt lưới dọc
        xAxis.setLabelCount(12, true); // Hiển thị đủ nhãn từ 1 đến 12

        // Cấu hình trục Y bên trái (đây là trục biểu thị giá trị)
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0); // Đặt giá trị tối thiểu của trục Y là 0
        leftAxis.setDrawGridLines(true); // Hiển thị lưới ngang

        // Cấu hình trục Y bên phải (có thể không cần, nên tắt)
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false); // Tắt trục Y bên phải vì không cần thiết
    }
}
