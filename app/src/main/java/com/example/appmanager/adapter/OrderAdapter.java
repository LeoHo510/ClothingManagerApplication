package com.example.appmanager.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanager.Model.User;
import com.example.appmanager.Model.UserModel;
import com.example.appmanager.R;
import com.example.appmanager.Model.Order;
import com.example.appmanager.retrofit.ApiClothing;
import com.example.appmanager.retrofit.ApiPushNoti;
import com.example.appmanager.retrofit.RetrofitClient;
import com.example.appmanager.retrofit.RetrofitClientNoti;
import com.example.appmanager.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private Context context;
    private List<Order> orderList;
    private OrderDetailsAdapter adapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiClothing apiClothing;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
        this.apiClothing = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiClothing.class);
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.order_adapter_layout, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (!orderList.isEmpty()) {
            Order order = orderList.get(position);
            if (order != null) {
                holder.id.setText("Order #" + order.getId());
                holder.status.setText("Status: " + order.getStatus());
                holder.date.setText(order.getDate());

                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                holder.total_price.setText("Total Price: đ" + decimalFormat.format(Double.parseDouble(order.getTotalprice())));

                holder.name.setText("Name: " + order.getFirstname() + " " + order.getLastname());
                holder.email.setText("Email: " + order.getEmail());
                holder.address.setText("Address: " + order.getAddress());
                holder.phone_number.setText("Phone Number: " + order.getPhonenumber());

                LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                holder.recyclerView.setLayoutManager(manager);
                holder.recyclerView.setHasFixedSize(true);
                adapter = new OrderDetailsAdapter(context, order.getList());
                holder.recyclerView.setAdapter(adapter);

                holder.itemView.setOnLongClickListener(v -> {
                    showStatusDialog(order, position);
                    return true;
                });
            }
        }
    }

    private void showStatusDialog(Order order, int position) {
        String[] statuses = {"Pending", "Processing", "Shipped", "Delivered", "Canceled", "Refunded", "Failed"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn trạng thái mới")
                .setItems(statuses, (dialog, which) -> {
                    String newStatus = statuses[which];

                    // Cập nhật trạng thái mới trong danh sách
                    updateOrderStatus(order, newStatus, position);
                });

        builder.create().show();
    }

    private void updateOrderStatus(Order order, String newStatus, int position) {
        compositeDisposable.add(apiClothing.updateStatusOrder(order.getId(), newStatus)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if (messageModel.isSuccess()) {
                                // Cập nhật trạng thái mới nếu API thành công
                                orderList.get(position).setStatus(newStatus);
                                notifyItemChanged(position);

                                // Gửi thông báo thay đổi trạng thái đơn hàng cho user
                                pushNotiToUser(order, newStatus);

                                // Gửi email thay đổi trạng thái đơn hàng cho user
                                pushEmailToCustomer(order.getEmail(), "Notice for Order #" + order.getId(), buildEmailMessage(order, newStatus));

                                MotionToast.Companion.createToast(
                                        (Activity) context,
                                        "Notice",
                                        messageModel.getMessage(),
                                        MotionToastStyle.SUCCESS,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(context, R.font.helvetica_regular)
                                );
                            } else {
                                MotionToast.Companion.createToast(
                                        (Activity) context,
                                        "Error",
                                        "Failed to update order status!",
                                        MotionToastStyle.ERROR,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(context, R.font.helvetica_regular)
                                );
                            }
                        },
                        throwable -> {
                            // Xử lý lỗi API
                            MotionToast.Companion.createToast(
                                    (Activity) context,
                                    "Error",
                                    "Failed to update order status!",
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(context, R.font.helvetica_regular)
                            );
                        }
                ));
    }
    public void pushNotiToUser(Order order, String newStatus) {
        compositeDisposable.add(apiClothing.getToken(0, order.getIduser())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess() && userModel.getResult() != null) {
                                for (int i = 0; i < userModel.getResult().size(); i++) {
                                    final HashMap<String, Object> requestBody = getStringObjectHashMap(newStatus, userModel, i);

                                    ApiPushNoti apiPushNoti = RetrofitClientNoti.getInstance().create(ApiPushNoti.class);
                                    compositeDisposable.add(apiPushNoti.sendNotification(requestBody)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(
                                                    notiResponse -> {
                                                        Log.d("pushNotiToUser", "Notification sent successfully: " + new Gson().toJson(notiResponse));
                                                    },
                                                    throwable -> {
                                                        Log.e("pushNotiToUser", "Error sending notification: " + throwable.getMessage(), throwable);
                                                    }
                                            ));
                                }
                            }
                        },
                        throwable -> {
                            Log.d("pushNotiToUser", throwable.getMessage());
                        }
                ));
    }

    @NonNull
    private static HashMap<String, Object> getStringObjectHashMap(String newStatus, UserModel userModel, int i) {
        Map<String, String> notification = new HashMap<>();
        notification.put("title", "Thông báo");
        notification.put("body", "Đơn hàng của bạn đã thay đổi trạng thái: " + newStatus);

        Map<String, Object> message = new HashMap<>();
        message.put("token", userModel.getResult().get(i).getToken());
        message.put("notification", notification);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("message", message);
        return requestBody;
    }

    @Override
    public int getItemCount() {
        return orderList == null ? 0 : orderList.size();
    }

    public void dispose() {
        compositeDisposable.dispose();
    }

    private void pushEmailToCustomer(String email, String subject, String message) {
        compositeDisposable.add(apiClothing.send_email_order_status(email, subject, message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> Log.d("Email", "Email sent successfully!"),
                        throwable -> Log.e("Email", "Error sending email: " + throwable.getMessage())
                ));
    }
    private String buildEmailMessage(Order order, String newStatus) {
        StringBuilder message = new StringBuilder();

        message.append("<html><body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>");
        message.append("<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>");

        // Header
        message.append("<img src='").append("https://static.topcv.vn/company_logos/UVtPJzXHNpW4xuYDTIOFzTGee91V77wq_1652668489____7c7b6d92ab5b882b561d96cdff2e9a75.png").append("' alt='Product Image' style='width: 80px; height: 80px; border-radius: 5px;'>");
        message.append("<h2 style='text-align: center; color: #4CAF50;'>Your order's status has been changed!</h2>");
        message.append("<p>Dear ").append(order.getFirstname()).append(",</p>");
        message.append("<p>Here are the details of your order:</p>");

        // Product List
        if (!order.getList().isEmpty()) {
            message.append("<ul style='list-style: none; padding: 0;'>");
            for (int i = 0; i < order.getList().size(); i++) {
                String imageUrl = order.getList().get(i).getUrl_img();
                String productName = order.getList().get(i).getName();
                productName = productName.length() > 30 ? productName.substring(0, 27) + "..." : productName;
                String size = String.valueOf(order.getList().get(i).getSize());
                int quantity = order.getList().get(i).getQuantity();
                String priceStr = order.getList().get(i).getPrice();
                double price = (priceStr != null) ? Double.parseDouble(priceStr) * quantity : 0.0;

                message.append("<li style='margin-bottom: 20px; border-bottom: 1px solid #eee; padding-bottom: 10px;'>");
                message.append("<div style='display: flex; align-items: center;'>");
                message.append("<img src='").append(imageUrl).append("' alt='Product Image' style='width: 80px; height: 80px; border-radius: 5px; margin-right: 15px;'>");
                message.append("<div>");
                message.append("<p style='font-size: 16px; font-weight: bold; margin: 0;'>").append(productName).append("</p>");
                message.append("<p style='margin: 5px 0; font-size: 14px;'>Size: ").append(size).append("</p>");
                message.append("<p style='margin: 5px 0; font-size: 14px;'>Quantity: ").append(quantity).append("</p>");
                message.append("<p style='margin: 5px 0; font-size: 14px; color: #4CAF50; font-weight: bold;'>").append(String.format(Locale.US, "%,.0f VND", price)).append("</p>");
                message.append("</div>");
                message.append("</div>");
                message.append("</li>");
            }
            message.append("</ul>");
        } else {
            message.append("<p style='text-align: center; color: #888;'>No products found in your order.</p>");
        }

        // Total Price
        double totalPrice = (order.getTotalprice() != null) ? Double.parseDouble(order.getTotalprice()) : 0.0;
        message.append("<p style='text-align: right; font-size: 18px; font-weight: bold; margin-top: 20px;'>Total: ").append(String.format(Locale.US, "%,.0f VND", totalPrice)).append("</p>");

        // Order Status
        message.append("<p style='font-size: 16px; margin-top: 20px;'><strong>Status:</strong>").append(" ").append(newStatus).append("</p>");
        message.append("<p>We will notify you once your order is change status!</p>");

        // Footer
        message.append("<p style='text-align: center; margin-top: 30px; color: #888;'>Best regards,<br>Merchize</p>");
        message.append("</div></body></html>");

        return message.toString();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, status, date, total_price, name, email, address, phone_number;
        RecyclerView recyclerView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.idOrder);
            status = itemView.findViewById(R.id.statusOrder);
            date = itemView.findViewById(R.id.dateOrder);
            total_price = itemView.findViewById(R.id.totalPriceOrder);
            name = itemView.findViewById(R.id.nameOrder);
            email = itemView.findViewById(R.id.emailOrder);
            address = itemView.findViewById(R.id.addressOrder);
            phone_number = itemView.findViewById(R.id.phonenumberOrder);
            recyclerView = itemView.findViewById(R.id.recycleOrder);
        }
    }
}


