//package com.example.hungerlink;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.auth.FirebaseAuth;
//
//public class LoginActivity extends AppCompatActivity {
//
//    private Button login;
//    private TextView signUp;
//    private EditText email;
//    private EditText password;
//    private TextView forgetPassword;
//    private FirebaseAuth auth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        try {
//            login = findViewById(R.id.login);
//            email = findViewById(R.id.email);
//            password = findViewById(R.id.password);
//            signUp = findViewById(R.id.signupText);
////            forgetPassword = findViewById(R.id.forgotPassword);
//            auth = FirebaseAuth.getInstance();
//        } catch (Exception e) {
//            Toast.makeText(LoginActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
//        }
//
//        email.requestFocus();
//
//        login.setOnClickListener(view -> {
//            try {
//                String txt_email = email.getText().toString();
//                String txt_password = password.getText().toString();
//                loginUser(txt_email, txt_password);
//            } catch (Exception e) {
//                Toast.makeText(LoginActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        signUp.setOnClickListener(view -> {
//            startActivity(new Intent(LoginActivity.this, DonorSignUpActivity.class));
//            finish();
//        });
//
////        forgetPassword.setOnClickListener(view -> {
////            try {
////                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
////                finish();
////            } catch (Exception e) {
////                Toast.makeText(LoginActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
////            }
////        });
//    }
//
//    //Method for the USER Login-->>
//    private void loginUser(String email, String password) {
//        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
//            Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//        });
//        auth.signInWithEmailAndPassword(email, password).addOnFailureListener(e -> {
//            e.printStackTrace();
//            Toast.makeText(LoginActivity.this, "Invalid Email or Password!", Toast.LENGTH_SHORT).show();
//        });
//    }
//
//}


package com.example.hungerlink.ActivityClasses;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hungerlink.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button login; // Button for login
    private TextView signUp; // TextView for navigating to signup activity
    private EditText email; // EditText for user email input
    private EditText password; // EditText for user password input
    private TextView forgetPassword; // TextView for password recovery
    private FirebaseAuth auth; // Firebase authentication instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components and Firebase Auth
        try {
            login = findViewById(R.id.login);
            email = findViewById(R.id.email);
            password = findViewById(R.id.password);
            signUp = findViewById(R.id.signupText);
            forgetPassword = findViewById(R.id.forgotPassword);
            auth = FirebaseAuth.getInstance();
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
        }

        email.requestFocus(); // Set focus on the email input field

        // Set onClickListener for the login button
        login.setOnClickListener(view -> {
            try {
                String txt_email = email.getText().toString(); // Get email input
                String txt_password = password.getText().toString(); // Get password input
                loginUser(txt_email, txt_password); // Call login method
            } catch (Exception e) {
                Toast.makeText(LoginActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
            }
        });

        // Set onClickListener for the sign-up TextView
        signUp.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, DonorSignUpActivity.class)); // Start sign-up activity
            finish(); // Close the current activity
        });

        // password recovery functionality
        forgetPassword.setOnClickListener(view -> {
            try {
                 startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                 finish();
            } catch (Exception e) {
                 Toast.makeText(LoginActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method for user login
    private void loginUser(String email, String password) {
        // Attempt to sign in with email and password
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    // If successful, show success message and start MainActivity
                    Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish(); // Close the LoginActivity
                })
                .addOnFailureListener(e -> {
                    // If login fails, show error message
                    Toast.makeText(LoginActivity.this, "Invalid Email or Password!", Toast.LENGTH_SHORT).show();
                });
    }
}
