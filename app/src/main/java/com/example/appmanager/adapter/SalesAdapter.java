package com.example.appmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appmanager.Model.Sales;
import com.example.appmanager.R;

import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.MyViewHolder> {
    Context context;
    List<Sales> list;

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
            Glide.with(context).load(sales.getUrl()).into(holder.imageView);
            holder.textView.setText(sales.getInfo());
        }
    }

    @Override
    public int getItemCount() {
        return list.isEmpty() ? 0 : list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgSales);
            textView = itemView.findViewById(R.id.txtSales);
        }
    }
}
