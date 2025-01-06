package com.example.myapplication.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class UserHolder extends RecyclerView.ViewHolder {
    TextView id, password, name, email;

    public UserHolder(@NonNull View itemView){
        super(itemView);
        id = itemView.findViewById(R.id.userListItem_ID);
        password = itemView.findViewById(R.id.userListItem_Password);
        name = itemView.findViewById(R.id.userListItem_Name);
        email = itemView.findViewById(R.id.userListItem_Email);
    }
}
