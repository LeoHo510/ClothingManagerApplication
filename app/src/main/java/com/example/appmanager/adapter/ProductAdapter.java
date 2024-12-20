package com.example.appmanager.adapter;

import android.annotation.SuppressLint;
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
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appmanager.Interface.ProductActionListener;
import com.example.appmanager.Model.Product;
import com.example.appmanager.Interface.ItemClickListener;
import com.example.appmanager.R;
import com.example.appmanager.activity.UpdateProductActivity;
import com.example.appmanager.retrofit.ApiClothing;
import com.example.appmanager.retrofit.RetrofitClient;
import com.example.appmanager.utils.Utils;

import java.text.DecimalFormat;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    Context context;
    List<Product> list;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiClothing apiClothing;
    ProductActionListener productActionListener;

    public void setProductActionListener(ProductActionListener productActionListener) {
        this.productActionListener = productActionListener;
    }

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
            apiClothing = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiClothing.class);
            Glide.with(context).load(product.getUrl_img()).into(holder.childImage);
            holder.childTitle.setText(product.getName());
            holder.childInfo.setText(product.getInfo());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            holder.childPrice.setText("đ" + decimalFormat.format(Double.parseDouble(product.getPrice())));
            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Notice");
                    builder.setMessage("Please choose your action!");
                    builder.setNegativeButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent update = new Intent(view.getContext(), UpdateProductActivity.class);
                            update.putExtra("product", product);
                            context.startActivity(update);
                        }
                    });
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            compositeDisposable.add(apiClothing.deleteProduct(product.getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            resultModel -> {
                                                if (resultModel.isSuccess()) {
                                                    list.remove(position);
                                                    notifyItemRemoved(position);
                                                    notifyItemRangeChanged(position, list.size());
                                                    if (productActionListener != null) {
                                                        productActionListener.onProductDeleted();
                                                    }
//                                                    Intent intent = new Intent("update_product_list");
//                                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                                    MotionToast.Companion.createToast(
                                                            (Activity) view.getContext(),
                                                            "Notice",
                                                            resultModel.getMessage(),
                                                            MotionToastStyle.DELETE,
                                                            MotionToast.GRAVITY_BOTTOM,
                                                            MotionToast.SHORT_DURATION,
                                                            Typeface.defaultFromStyle(R.font.helvetica_regular)
                                                    );
                                                } else {
                                                    MotionToast.Companion.createToast(
                                                            (Activity) view.getContext(),
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
                                                MotionToast.Companion.createToast(
                                                        (Activity) view.getContext(),
                                                        "Error",
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
                    builder.create().show();
                }
            });
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