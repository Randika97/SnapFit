package com.example.snapfit.retrofit;


import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import com.example.snapfit.userservice.Service;
import com.squareup.okhttp.OkHttpClient;

public class RetrofitConfig {
    private static String BASE_URL = "http://10.0.2.2:5000/";
    private static RetrofitConfig mInstance;
    private final Retrofit retrofit;

    public  RetrofitConfig(){

        OkHttpClient okHttpClient = new OkHttpClient();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }
    public static synchronized RetrofitConfig getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitConfig();
        }
        return mInstance;
    }

    public Service getService(){
        return this.retrofit.create(Service.class);
    }

}