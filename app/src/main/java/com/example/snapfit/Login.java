package com.example.snapfit;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private TextView signUp;
    private EditText email, password;
    private Button loginBtn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (mAuth.getCurrentUser() != null) {
//            startActivity(new Intent(Login.this, MainActivity.class));
//            finish();
//        }
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        signUp = findViewById(R.id.textView6);
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(v -> loginUserAccount());
        signUp.setOnClickListener(v -> signupNewUser());
    }

    private void loginUserAccount() {

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

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void signupNewUser() {
        Intent intent = new Intent(Login.this, SignUp.class);
        startActivity(intent);

    }
}