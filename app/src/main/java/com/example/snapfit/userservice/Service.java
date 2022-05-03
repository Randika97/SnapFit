package com.example.snapfit.userservice;

import com.example.snapfit.model.Result;
import com.example.snapfit.retrofit.ServerResponse;

import java.util.List;

import okhttp3.MultipartBody;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;



public interface Service {

    @GET("result/")
    Call<List<ServerResponse>> getResults();

    @FormUrlEncoded
    @Multipart
    @POST("/")
    Call<ServerResponse> uploadMulFile(@Part("file") MultipartBody.Part filepart);


    @FormUrlEncoded
    @POST("/")
    Call<ServerResponse> uploadMulFileBaseFormat(@Field("file") String image);
}