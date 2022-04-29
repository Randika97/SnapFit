package com.example.snapfit;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.Toast;

import com.example.snapfit.model.User;
import com.example.snapfit.retrofit.AppConfig;
import com.example.snapfit.retrofit.ServerResponse;
import com.example.snapfit.userservice.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageUploading extends AppCompatActivity {
    UserService userService;
    List<User> list = new ArrayList<User>();
    String figureFrontPath, figureSidePath,clothPath;
    ImageButton figureFront,figureSide,clothFront;
    Button UploadButton;
    public String authEmail,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_uploading);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                 uid = profile.getUid();
                authEmail = profile.getEmail();

            }
        }
        figureFront = (ImageButton)findViewById(R.id.uploadFigureFront);
        figureSide =  (ImageButton)findViewById(R.id.uploadFigureSide);
        clothFront =(ImageButton)findViewById(R.id.uploadCloth);
        UploadButton = (Button) findViewById(R.id.imageUploadPageButton);
        UploadButton.setOnClickListener((View.OnClickListener) v -> uploadMultipleFiles());

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                figureFrontPath = cursor.getString(columnIndex);
                cursor.close();
            }else if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                figureSidePath = cursor.getString(columnIndex);
                cursor.close();
            }
            else if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                clothPath = cursor.getString(columnIndex);
                cursor.close();
            }
            else {
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
        File fileFigureSide = new File(figureSidePath);
        File fileClothFront = new File(clothPath);

        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), fileFigureFrontPath);
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("*/*"), fileFigureSide);
        RequestBody requestBody3 = RequestBody.create(MediaType.parse("*/*"), fileClothFront);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), authEmail);

        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file1", fileFigureFrontPath.getName(), requestBody1);
        MultipartBody.Part fileToUpload2 = MultipartBody.Part.createFormData("file2", fileFigureSide.getName(), requestBody2);
        MultipartBody.Part fileToUpload3 = MultipartBody.Part.createFormData("file3", fileClothFront.getName(), requestBody3);

        UserService getResponse = AppConfig.getRetrofit().create(UserService.class);
        Call<ServerResponse> call = getResponse.uploadMulFile(fileToUpload1, fileToUpload2,fileToUpload3,authEmail);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
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
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
            }
        });
    }
}