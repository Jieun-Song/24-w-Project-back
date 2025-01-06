package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.UserAdapter;
import com.example.myapplication.model.User;
import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        recyclerView = findViewById(R.id.userList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadUsers();
    }

    private void loadUsers() {
        RetrofitService retrofitService = new RetrofitService();
        UserAPI userAPI = retrofitService.getRetrofit().create(UserAPI.class);
        userAPI.getAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                populateListView(response.body());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(UserListActivity.this,"Failed to load users", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateListView(List<User> userList) {
        UserAdapter userAdapter = new UserAdapter(userList);

        recyclerView.setAdapter(userAdapter);
    }
}
