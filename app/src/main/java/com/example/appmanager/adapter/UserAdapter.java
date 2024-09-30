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
import com.example.appmanager.Model.User;
import com.example.appmanager.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    Context context;
    List<User> list;

    public UserAdapter(Context context, List<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.user_adapter_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {
        User user = list.get(position);
        if (user != null) {
            holder.name.setText(user.getFirstname() + user.getLastname());
            holder.gmail.setText(user.getEmail());
            holder.phone.setText(user.getPhonenumber());
            holder.address.setText(user.getAddress());
        }
    }

    @Override
    public int getItemCount() {
        return list.isEmpty() ? 0 : list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, gmail, address;
        public MyViewHolder(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.txtUsername);
            gmail = v.findViewById(R.id.txtUserGmail);
            phone = v.findViewById(R.id.txtUserPhone);
            address = v.findViewById(R.id.txtUserAddress);
        }
    }
}
