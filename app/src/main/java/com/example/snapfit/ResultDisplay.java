package com.example.snapfit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.snapfit.model.User;
import com.example.snapfit.retrofit.AppConfig;
import com.example.snapfit.userservice.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultDisplay extends AppCompatActivity {
    private UserService userService;
    private static final String TAG = "ResultDisplay";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_display);
        userService = AppConfig.getRetrofit().create(UserService.class);
        callReadAPI();
    }
    public void callReadAPI() {
        // pass query parameter as user to get all the users
        userService.getResults().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                String message = response.message();
                List<User> usersList = response.body();
                int usersSize = usersList.size();
                Log.i(TAG,"usersSize : "+ usersSize);
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
            }
        });
    }
}