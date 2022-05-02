package com.example.snapfit;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.snapfit.retrofit.RetrofitConfig;
import com.example.snapfit.retrofit.ServerResponse;
import com.example.snapfit.userservice.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
//import retrofit2.Response;

public class ImageUploading extends AppCompatActivity  {
    //Initialize variables
    DrawerLayout drawerLayout;
    String figureFrontPath, figureSidePath,clothPath;
    Button figureFront;
    Button figureSide;
    Button clothFront;
    Button UploadButton;
    public String authEmail,uid;
    Bitmap bitmap;
    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_uploading);

        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                 uid = profile.getUid();
                authEmail = profile.getEmail();

            }
        }
        //Init UI elements from XML
        figureFront = (Button)findViewById(R.id.uploadFigureFront);
        figureSide =  (Button)findViewById(R.id.uploadFigureSide);
        clothFront =(Button)findViewById(R.id.uploadCloth);
        UploadButton = (Button) findViewById(R.id.imageUploadPageButton);
        //Upload phots to API
        UploadButton.setOnClickListener(view -> {
            uploadMultipleFiles();
        });
        figureFront.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 0);
        });
        figureSide.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 1);
        });
        clothFront.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 2);
        });
    }
    public void ClickMenu(View view){
        //open drawer
        openDrawer(drawerLayout);
    }
    private static void openDrawer(DrawerLayout drawerLayout) {
        //open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void ClickLogo(View view){
        //close drawer
        closeDrawer(drawerLayout);
    }
    public static void closeDrawer(DrawerLayout drawerLayout) {
        //close drawer layout
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            //when drawer is opes close it
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public void ClickHome(View view){
        //Redirect to landing page
        redirectActivity(this,MainActivity.class);
    }
    public static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity,aClass);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
    @Override
    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When front Image is select
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                figureFrontPath = cursor.getString(columnIndex);
                cursor.close();
            } else if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
                // When Side Image is select
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                figureSidePath = cursor.getString(columnIndex);
                cursor.close();
            } else if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
                // Cloth Image is send
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                clothPath = cursor.getString(columnIndex);
                cursor.close();
            } else {
                Toast.makeText(this, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }

    // Uploading Images to server
    private void uploadMultipleFiles() {
        // Map is used to multipart the file using okhttp3.RequestBody
        File fileFigureFrontPath = new File(figureFrontPath);
        //File fileFigureSide = new File(figureSidePath);
        //File fileClothFront = new File(clothPath);
        //method One
        // Parsing any Media type file
        /*RequestBody requestBody1 = RequestBody.create(fileFigureFrontPath, MediaType.parse("multipart/form-data"));
        RequestBody requestBody2 = RequestBody.create(MediaType.parse(""), fileFigureSide);
        RequestBody requestBody3 = RequestBody.create(MediaType.parse(""), fileClothFront);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), authEmail);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("bodyfront", fileFigureFrontPath.getName(), requestBody1);
        MultipartBody.Part fileToUpload2 = MultipartBody.Part.createFormData("file2", fileFigureSide.getName(), requestBody2);
        MultipartBody.Part fileToUpload3 = MultipartBody.Part.createFormData("file3", fileClothFront.getName(), requestBody3);
        Call<ServerResponse> call = new RetrofitConfig().getService().uploadMulFile(filePart);
        Service getResponse = RetrofitConfig.getRetrofit().create(Service.class);
        Call<ServerResponse> call = getResponse.uploadMulFile(fileToUpload1);*/
        //Multipart with with responseBody
        //MultipartBody.Part filePart = MultipartBody.Part.createFormData("filePart", fileFigureFrontPath.getName(), RequestBody.create(fileFigureFrontPath,MediaType.parse("image/*")));

        //method two
        RequestBody reqBody = RequestBody.create(fileFigureFrontPath, MediaType.parse("multipart/form-file"));
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("file", fileFigureFrontPath.getName(), reqBody);
        Service getResponse = new RetrofitConfig().getInstance().getService();
        Call<ServerResponse> call = getResponse.uploadMulFile(partImage);
      //Call<ServerResponse> call = new RetrofitConfig().getService().uploadMulFile(partImage);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Response<ServerResponse> response, Retrofit retrofit) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ImageUploading.this, ResultDisplay.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.v("Response", serverResponse.toString());
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "failed uploading", Toast.LENGTH_SHORT).show();
            }
        });
    }
}