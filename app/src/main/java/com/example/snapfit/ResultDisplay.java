package com.example.snapfit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import com.example.snapfit.model.Result;
import com.example.snapfit.userservice.Service;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultDisplay extends AppCompatActivity {
    DrawerLayout drawerLayout;
    String matchResult;
    private TextView result;
    private static final String TAG = "ResultDisplay";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_display);
        drawerLayout = findViewById(R.id.drawer_layout);
        result = (TextView) findViewById(R.id.textView5);
        //checking the passed Country value from previous acitvity and set it to text
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                matchResult = extras.getString("result");
                if(matchResult.equals("matching")){
                    result.setText("Matching");
                }else{
                    result.setText("Not-Matching");
                }
            } else {
                result.setText("Error matchning");
            }
        }
    }



    //Drawer functions
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
        redirectActivity(this,ImageUploading.class);
    }
    public void ClickLogout(View view){
        //Redirect to landing page
        SessionManagement sessionManagement = new SessionManagement(ResultDisplay.this);
        sessionManagement.removeSession();
        redirectActivity(this,MainActivity.class);
    }
    public static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity,aClass);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

}