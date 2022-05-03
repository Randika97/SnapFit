package com.example.snapfit;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ImageUploading extends AppCompatActivity {
    //Initialize variables
    DrawerLayout drawerLayout;
    String figureFrontPath, figureSidePath, clothPath;
    Button figureFront;
    Button figureSide;
    Button clothFront;
    Button UploadButton;
    public String authEmail, uid;
    Bitmap bitmap;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_uploading);

        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);
        //Get Current Auth user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                uid = profile.getUid();
                authEmail = profile.getEmail();

            }
        }
        //Init UI elements from XML
        figureFront = (Button) findViewById(R.id.uploadFigureFront);
        figureSide = (Button) findViewById(R.id.uploadFigureSide);
        clothFront = (Button) findViewById(R.id.uploadCloth);
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

    public void ClickMenu(View view) {
        //open drawer
        openDrawer(drawerLayout);
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        //open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view) {
        //close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //close drawer layout
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //when drawer is opes close it
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view) {
        //Redirect to landing page
        redirectActivity(this, MainActivity.class);
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity, aClass);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
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
                //display selected image in imageview
                    ImageView myImage = (ImageView) findViewById(R.id.imageViewFigureOne);
                    myImage.setImageBitmap(bitmap);

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
                ImageView myImage = (ImageView) findViewById(R.id.imageViewClothOne);
                myImage.setImageBitmap(bitmap);
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


        //Use base64Encode
/*        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        Service getResponse = new RetrofitConfig().getInstance().getService();
        Call<ServerResponse> call = getResponse.uploadMulFileBaseFormat(image);*/

        // Map is used to multipart the file using okhttp3.RequestBody
        File clothFilePath = new File(clothPath);
        RequestBody reqBody = RequestBody.create(clothFilePath, MediaType.parse("multipart/form-file"));
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("file", clothFilePath.getName(), reqBody);
        Service getResponse = new RetrofitConfig().getInstance().getService();
        Call<ServerResponse> call = getResponse.uploadMulFile(partImage);
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