package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserHolder> {
    private List<User> userList;

    public UserAdapter(List<User> userList){
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_user_item, parent, false);
        return new UserHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position){
        User user = userList.get(position);
        holder.id.setText(user.getId());
        holder.password.setText(user.getPassword());
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
