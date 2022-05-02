package com.example.snapfit.userservice;

import com.example.snapfit.model.User;
import com.example.snapfit.retrofit.ServerResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface UserService {

    @GET("result/")
    Call<List<User>> getResults();

    @Multipart
    @POST("checkMatch/")
    Call<ServerResponse> uploadMulFile(@Part MultipartBody.Part bodyFront);

}