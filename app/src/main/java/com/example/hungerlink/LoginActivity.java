package com.example.hungerlink;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private TextView signUp;
    private EditText email;
    private EditText password;
    private TextView forgetPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            login = findViewById(R.id.login);
            email = findViewById(R.id.email);
            password = findViewById(R.id.password);
//            signUp = findViewById(R.id.signUp);
//            forgetPassword = findViewById(R.id.forgotPassword);
            auth = FirebaseAuth.getInstance();
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
        }

        email.requestFocus();

        login.setOnClickListener(view -> {
            try {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                loginUser(txt_email, txt_password);
            } catch (Exception e) {
                Toast.makeText(LoginActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
            }
        });

//        signUp.setOnClickListener(view -> {
//            startActivity(new Intent(LoginActivity.this, DonorSignUpActivity.class));
//            finish();
//        });

//        forgetPassword.setOnClickListener(view -> {
//            try {
//                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
//                finish();
//            } catch (Exception e) {
//                Toast.makeText(LoginActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    //Method for the USER Login-->>
    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });
        auth.signInWithEmailAndPassword(email, password).addOnFailureListener(e -> {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, "Invalid Email or Password!", Toast.LENGTH_SHORT).show();
        });
    }

}