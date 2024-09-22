package com.example.appmanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appmanager.Model.Product;
import com.example.appmanager.Interface.ItemClickListener;
import com.example.appmanager.R;
import com.example.appmanager.utils.Utils;

import java.text.DecimalFormat;
import java.util.List;

import io.paperdb.Paper;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    Context context;
    List<Product> list;

    public ProductAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
        Paper.init(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_child_adapter, parent, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = list.get(position);
        if (product != null) {
            Glide.with(context).load(product.getUrl_img()).into(holder.childImage);
            holder.childTitle.setText(product.getName());
            holder.childInfo.setText(product.getInfo());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            holder.childPrice.setText("đ" + decimalFormat.format(Double.parseDouble(product.getPrice())));
        }
    }
    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView childImage;
        TextView childTitle, childInfo, childPrice;
        ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            childImage = itemView.findViewById(R.id.childImage);
            childInfo = itemView.findViewById(R.id.childInfo);
            childTitle = itemView.findViewById(R.id.childTitle);
            childPrice = itemView.findViewById(R.id.childPrice);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition());
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
    }
}