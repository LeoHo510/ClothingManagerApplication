package com.example.appmanager.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appmanager.Interface.ItemClickListener;
import com.example.appmanager.Interface.SaleActionListener;
import com.example.appmanager.Model.Sales;
import com.example.appmanager.R;
import com.example.appmanager.activity.UpdateSaleActivity;
import com.example.appmanager.retrofit.ApiClothing;
import com.example.appmanager.retrofit.RetrofitClient;
import com.example.appmanager.utils.Utils;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.MyViewHolder> {
    Context context;
    List<Sales> list;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiClothing apiClothing;
    SaleActionListener saleActionListener;

    public void setSaleActionListener(SaleActionListener saleActionListener) {
        this.saleActionListener = saleActionListener;
    }

    public SalesAdapter(Context context, List<Sales> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SalesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.sales_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SalesAdapter.MyViewHolder holder, int position) {
        Sales sales = list.get(position);
        if (sales != null) {
            apiClothing = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiClothing.class);
            Glide.with(context).load(sales.getUrl()).into(holder.imageView);
            holder.textView.setText(sales.getInfo());
            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Notice");
                    builder.setMessage("Please choose action!");
                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent sale = new Intent(context, UpdateSaleActivity.class);
                            sale.putExtra("sale", sales);
                            context.startActivity(sale);
                        }
                    });
                    builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            compositeDisposable.add(apiClothing.deleteSale(sales.getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            resultModel -> {
                                                if (resultModel.isSuccess()) {
                                                    list.remove(position);
                                                    notifyItemRemoved(position);
                                                    notifyItemRangeChanged(position, list.size());
                                                    MotionToast.Companion.createToast(
                                                            (Activity) view.getContext(),
                                                            "Notice",
                                                            resultModel.getMessage(),
                                                            MotionToastStyle.DELETE,
                                                            MotionToast.GRAVITY_BOTTOM,
                                                            MotionToast.SHORT_DURATION,
                                                            Typeface.defaultFromStyle(R.font.helvetica_regular)
                                                    );
                                                    if (saleActionListener != null) {
                                                        saleActionListener.onDeleteSale();
                                                    }
                                                }
                                            },
                                            throwable -> {
                                                MotionToast.Companion.createToast(
                                                        (Activity) view.getContext(),
                                                        "Notice",
                                                        Objects.requireNonNull(throwable.getMessage()),
                                                        MotionToastStyle.ERROR,
                                                        MotionToast.GRAVITY_BOTTOM,
                                                        MotionToast.SHORT_DURATION,
                                                        Typeface.defaultFromStyle(R.font.helvetica_regular)
                                                );
                                            }
                                    ));
                        }
                    });
                    builder.create().show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.isEmpty() ? 0 : list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgSales);
            textView = itemView.findViewById(R.id.txtSales);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClick(v, getAdapterPosition());
                }
            });
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
    }
}
