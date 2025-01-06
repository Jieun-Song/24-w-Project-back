package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.User;
import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserAPI;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
    }

    private void initializeComponents(){
        TextInputEditText inputEditName=findViewById(R.id.form_textFieldName);
        TextInputEditText inputEditPhonenum=findViewById(R.id.form_textFieldPhonenum);
        TextInputEditText inputEditGender=findViewById(R.id.form_textFieldGender);
        TextInputEditText inputEditEmail=findViewById(R.id.form_textFieldEmail);
        TextInputEditText inputEditUsername=findViewById(R.id.form_textFieldUsername);
        TextInputEditText inputEditPassword=findViewById(R.id.form_textFieldPassword);
        TextInputEditText inputEditBirthdate=findViewById(R.id.form_textFieldBirthdate);


        MaterialButton buttonSave=findViewById(R.id.form_buttonSave);

        RetrofitService retrofitService=new RetrofitService();
        UserAPI  userAPI=retrofitService.getRetrofit().create(UserAPI.class);

        buttonSave.setOnClickListener(view -> {
            String name = String.valueOf(inputEditName.getText());
            String phonenum = String.valueOf(inputEditPhonenum.getText());
            int gender = Integer.parseInt(String.valueOf(inputEditGender.getText()));
            String email = String.valueOf(inputEditEmail.getText());
            String username = String.valueOf(inputEditUsername.getText());
            String password = String.valueOf(inputEditPassword.getText());
            String birthdate = String.valueOf(inputEditBirthdate.getText());

            User user=new User();
            user.setName(name);
            user.setPhonenum(phonenum);
            user.setGender(gender);
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password);
            user.setBirthdate(birthdate);

            userAPI.save(user)
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) { // 저장이 되었다면
                            Toast.makeText(MainActivity.this, "Save Success", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) { // 저장이 실패했다면
                            Toast.makeText(MainActivity.this, "Save failed", Toast.LENGTH_LONG).show();
                            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, "Error occurred", t);
                        }
                    });


        });
    }
}