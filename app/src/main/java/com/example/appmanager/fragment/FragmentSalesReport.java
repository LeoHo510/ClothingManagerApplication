package com.example.appmanager.fragment;

import android.os.Bundle;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FragmentSalesReport extends Fragment {
    PieChart pieChart;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiClothing apiClothing;
    List<PieEntry> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sales_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        getData();
    }

    private void getData() {
        compositeDisposable.add(apiClothing.salesReport()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        salesReportModel -> {
                            if (salesReportModel.isSuccess()) {
                                list.clear(); // Clear list to avoid adding duplicate entries
                                for (int i = 0; i < salesReportModel.getResult().size(); i++) {
                                    String name = salesReportModel.getResult().get(i).getName();
                                    int total = salesReportModel.getResult().get(i).getTotal();
                                    list.add(new PieEntry(total, name)); // Use 'total' as the first argument
                                }
                                PieDataSet pieDataSet = new PieDataSet(list, "Sales Report");
                                pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                pieDataSet.setValueTextSize(12f);

                                PieData data = new PieData(pieDataSet); // Correctly assign dataset
                                data.setValueFormatter(new PercentFormatter(pieChart)); // Pass the chart to PercentFormatter

                                pieChart.setData(data);
                                pieChart.animateXY(1000, 1000);
                                pieChart.setUsePercentValues(true);
                                pieChart.getDescription().setEnabled(true); // Hide the description
                                pieChart.invalidate(); // Refresh the chart
                            }
                        },
                        throwable -> {
                            // Handle error
                        }
                ));
    }

    private void initView(View view) {
        apiClothing = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiClothing.class);
        list = new ArrayList<PieEntry>();
        pieChart = view.findViewById(R.id.pieChart);
    }
}
