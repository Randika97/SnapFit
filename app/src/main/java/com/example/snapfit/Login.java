package com.example.snapfit;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snapfit.firebase.Database;
import com.example.snapfit.firebase.OnGetDataListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private TextView signUp;
    private EditText email, password;
    private Button loginBtn;
    String BodyStatus;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create instance for session management
        SessionManagement sessionManagement = new SessionManagement(Login.this);
        String userID = sessionManagement.getSession(); //Get session management ID
        if(userID != null){
            //If userId not null get the userID and to get the Image status
            mCheckInforInServer(userID.replaceAll("@(.*).(.*)", ""));
        }
        else{
            //if the user ID is null view the Login UI
            setContentView(R.layout.activity_login);
            mAuth = FirebaseAuth.getInstance();
            signUp = findViewById(R.id.textView6);
            email = findViewById(R.id.loginEmail);
            password = findViewById(R.id.loginPassword);
            loginBtn = findViewById(R.id.loginBtn);
            loginBtn.setOnClickListener(v -> loginUserAccount());
            signUp.setOnClickListener(v -> signupNewUser());
        }
    }
    //Get the users body Image status
    public void mCheckInforInServer(String child) {
        new Database().mReadDataOnce(child, new OnGetDataListener() {
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(DataSnapshot data) {
                BodyStatus = data.getValue(String.class);
                Intent intent = new Intent(getApplicationContext(), ImageUploading.class);
                intent.putExtra("bodyStatus", BodyStatus);
                startActivity(intent);
            }
            @Override
            public void onFailed(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Fetch data unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Login to App using Firebase Auth
    private void loginUserAccount() {
        //Init variables
        String firebaseEmail, firebasePassword;
        firebaseEmail = email.getText().toString();
        firebasePassword = password.getText().toString();

        if (TextUtils.isEmpty(firebaseEmail)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(firebasePassword)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(firebaseEmail, firebasePassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                        //Save the email fro session management
                        SessionManagement sessionManagement = new SessionManagement(Login.this);
                        sessionManagement.saveSession(firebaseEmail);
                        Intent intent = new Intent(Login.this, ImageUploading.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                    }
                });
    }

    //Move to SignUp UI
    private void signupNewUser() {
        Intent intent = new Intent(Login.this, SignUp.class);
        startActivity(intent);
    }
}