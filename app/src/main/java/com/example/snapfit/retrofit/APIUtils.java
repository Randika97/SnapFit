package com.example.snapfit.retrofit;

import com.example.snapfit.userservice.UserService;

public class APIUtils {

    private APIUtils(){
    };

    public static final String API_URL = "http://120.0.0.1:8080";

    public static UserService getUserService(){
        return RetrofitClient.getClient(API_URL).create(UserService.class);
    }

}