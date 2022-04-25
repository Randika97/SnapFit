package com.example.snapfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView getStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getStart = findViewById(R.id.landingButton);
        getStart.setOnClickListener(v -> Dashboard());

    }

    private void Dashboard() {
        Intent intent = new Intent(MainActivity.this, ImageUploading.class);
        startActivity(intent);

    }



}