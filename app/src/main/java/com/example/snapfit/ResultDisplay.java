package com.example.snapfit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
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
    String figure;
    public TextView result;
    public TextView clothText;
    public ImageView recommondOne;
    public ImageView recommondTwo;
    public ImageView recommondThree;
    public ImageView recommondFour;
    public ImageView goBack;
    private static final String TAG = "ResultDisplay";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_display);
        drawerLayout = findViewById(R.id.drawer_layout);
        result = (TextView) findViewById(R.id.textView5);
        clothText = (TextView) findViewById(R.id.clothImage2);
        recommondOne = (ImageView) findViewById(R.id.clothImage1);
        recommondTwo = (ImageView) findViewById(R.id.clothImage3);
        recommondThree = (ImageView) findViewById(R.id.clothImage4);
        recommondFour = (ImageView) findViewById(R.id.clothImage5);
        goBack = (ImageView) findViewById(R.id.imageButton3);
        goBack.setOnClickListener(v -> goBack());
        //checking the passed Country value from previous acitvity and set it to text
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                matchResult = extras.getString("result");
                figure = extras.getString("figure");
                if(matchResult.equals("matching") && figure.equals("pearShape")){
                    result.setText("Matching");
                    recommondOne.setImageResource(R.drawable.questiomark);
                    recommondTwo.setImageResource(R.drawable.questiomark);
                    recommondThree.setImageResource(R.drawable.questiomark);
                    recommondFour.setImageResource(R.drawable.questiomark);
                    clothText.setText(R.string.PearShapeSuggestion);
                }else if(matchResult.equals("notmatching") && figure.equals("pearShape")){
                    result.setText("Not-Matching");
                    clothText.setText(R.string.PearShapeSuggestion);
                    recommondOne.setImageResource(R.drawable.questiomark);
                    recommondTwo.setImageResource(R.drawable.questiomark);
                    recommondThree.setImageResource(R.drawable.questiomark);
                    recommondFour.setImageResource(R.drawable.questiomark);
                }else if(matchResult.equals("matching") && figure.equals("hourglassBody")){
                    result.setText("Matching");
                    clothText.setText(R.string.hourglassSuggestion);
                    recommondOne.setImageResource(R.drawable.questiomark);
                    recommondTwo.setImageResource(R.drawable.questiomark);
                    recommondThree.setImageResource(R.drawable.questiomark);
                    recommondFour.setImageResource(R.drawable.questiomark);
                }else if(matchResult.equals("notmatching") && figure.equals("appleShape")){
                    result.setText("Not-Matching");
                    clothText.setText(R.string.appleShape);
                    recommondOne.setImageResource(R.drawable.recommendtopone);
                    recommondTwo.setImageResource(R.drawable.recommenedtoptwo);
                    recommondThree.setImageResource(R.drawable.recommenedtopthree);
                    recommondFour.setImageResource(R.drawable.recommenedtopfour);
                }else if(matchResult.equals("matching") && figure.equals("appleShape")){
                    result.setText("Matching");
                    clothText.setText(R.string.applematch);
                    recommondOne.setImageResource(R.drawable.questiomark);
                    recommondTwo.setImageResource(R.drawable.questiomark);
                    recommondThree.setImageResource(R.drawable.questiomark);
                    recommondFour.setImageResource(R.drawable.questiomark);
                }else{
                    clothText.setText("No suggestion found");
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
    //Move to SignUp UI
    private void goBack() {
        Intent intent = new Intent(ResultDisplay.this, ImageUploading.class);
        startActivity(intent);
    }

}