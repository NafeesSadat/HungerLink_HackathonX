//package com.example.hungerlink;
//
//import androidx.activity.OnBackPressedCallback;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Patterns;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.Objects;
//
//public class DonorSignUpActivity extends AppCompatActivity {
//    private Button signUp;
//    private EditText name;
//    private EditText email;
//    private EditText password;
//    private EditText confirmPassword;
//    private EditText phoneNo;
//    private EditText address;
//    private FirebaseAuth auth;
//
//    private TextView loginRedirectText;
//
//    String txt_email, txt_password, txt_confirmPassword, txt_name, txt_phoneNo, txt_address;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup);
//
//        // Initialize UI components
//        name = findViewById(R.id.name);
//        email = findViewById(R.id.signup_email);
//        password = findViewById(R.id.signup_password);
//        confirmPassword = findViewById(R.id.confirmPassword);
//        phoneNo = findViewById(R.id.phone);
//        address = findViewById(R.id.address);
//        auth = FirebaseAuth.getInstance();
//        signUp = findViewById(R.id.signup_button);
//        loginRedirectText = findViewById(R.id.loginRedirectText);
//
//        // Set the sign-up button listener
//        signUp.setOnClickListener(view -> {
//            txt_email = email.getText().toString().trim();
//            txt_password = password.getText().toString().trim();
//            txt_confirmPassword = confirmPassword.getText().toString().trim();
//            txt_name = name.getText().toString().trim();
//            txt_phoneNo = phoneNo.getText().toString().trim();
//            txt_address = address.getText().toString().trim();
//
//            int count = 0;
//
//            // Validate input fields
//            if (TextUtils.isEmpty(txt_email)) {
//                email.setError("Enter your Email Address");
//                count++;
//            }
//            if (TextUtils.isEmpty(txt_name)) {
//                name.setError("Enter your Name");
//                count++;
//            }
//            if (TextUtils.isEmpty(txt_password)) {
//                password.setError("Enter your Password");
//                count++;
//            }
//            if (TextUtils.isEmpty(txt_confirmPassword)) {
//                confirmPassword.setError("Confirm your Password");
//                count++;
//            }
//            if (TextUtils.isEmpty(txt_phoneNo)) {
//                phoneNo.setError("Enter your Phone Number");
//                count++;
//            }
//            if (TextUtils.isEmpty(txt_address)) {
//                address.setError("Enter your Address");
//                count++;
//            }
//            if (!Objects.equals(txt_password, txt_confirmPassword)) {
//                password.setError("Passwords do not match");
//                confirmPassword.setError("Passwords do not match");
//                count++;
//            }
//
//            if (count == 0) {
//                if (txt_password.length() < 6) {
//                    password.setError("Password too short\nMinimum length 6 characters!");
//                    confirmPassword.setError("Password too short\nMinimum length 6 characters!");
//                } else if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()) {
//                    Toast.makeText(DonorSignUpActivity.this, "Please provide a valid Email Address", Toast.LENGTH_SHORT).show();
//                } else {
//                    signUpUser(txt_email, txt_password);
//                }
//            }
//        });
//
//        loginRedirectText.setOnClickListener(view -> {
//            startActivity(new Intent(DonorSignUpActivity.this, LoginActivity.class));
//            finish(); // Optionally close the current activity
//        });
//    }
//
//    // Method to sign up new user
//    private void signUpUser(String txtEmail, String txt_password) {
//        auth.createUserWithEmailAndPassword(txtEmail, txt_password).addOnCompleteListener(DonorSignUpActivity.this, task -> {
//
//            try {
//                // Get the input values after successful sign-up
//                txt_email = email.getText().toString().trim();
//                txt_confirmPassword = confirmPassword.getText().toString().trim();
//                txt_name = name.getText().toString().trim();
//                txt_phoneNo = phoneNo.getText().toString().trim();
//                txt_address = address.getText().toString().trim();
//
//            } catch (Exception e) {
//                Toast.makeText(DonorSignUpActivity.this, "Enter the values properly!", Toast.LENGTH_SHORT).show();
//            }
//
//            if (task.isSuccessful()) {
//                // Create the UserInfoClass object to store user data
//                UserInfoClass user = new UserInfoClass(txt_name, txtEmail, txt_phoneNo, txt_address);
//                String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
//
//                // Create or update user data in Firebase Realtime Database
//                FirebaseDatabase database = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app");
//                DatabaseReference userRef = database.getReference("Users").child(userID);
//
//                userRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(DonorSignUpActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(DonorSignUpActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(DonorSignUpActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                // Redirect to the login activity
//                startActivity(new Intent(DonorSignUpActivity.this, LoginActivity.class));
//                finish();
//            } else {
//                Toast.makeText(DonorSignUpActivity.this, "The Email Address is already registered for an existing account!\nTry again with a different email!", Toast.LENGTH_LONG).show();
//            }
//        });
//        // Handle back press using OnBackPressedDispatcher
//        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                // Perform your custom action on back press, for example:
//                startActivity(new Intent(DonorSignUpActivity.this, LoginActivity.class));
//                finish(); // Close the current activity
//            }
//        });
//    }
//
//}


package com.example.hungerlink.ActivityClasses;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hungerlink.R;
import com.example.hungerlink.HelperClasses.UserInfoClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class DonorSignUpActivity extends AppCompatActivity {
    // UI components
    private Button signUp;
    private EditText name, email, password, confirmPassword, phoneNo, address;
    private FirebaseAuth auth;
    private TextView loginRedirectText;

    // Temporary storage for field values
    String txt_email, txt_password, txt_confirmPassword, txt_name, txt_phoneNo, txt_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI components
        name = findViewById(R.id.name);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.confirmPassword);
        phoneNo = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        auth = FirebaseAuth.getInstance();
        signUp = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        // Set the sign-up button listener
        signUp.setOnClickListener(view -> {
            // Retrieve and validate field values
            txt_email = email.getText().toString().trim();
            txt_password = password.getText().toString().trim();
            txt_confirmPassword = confirmPassword.getText().toString().trim();
            txt_name = name.getText().toString().trim();
            txt_phoneNo = phoneNo.getText().toString().trim();
            txt_address = address.getText().toString().trim();

            int count = 0;

            // Field validation checks
            if (TextUtils.isEmpty(txt_email)) {
                email.setError("Enter your Email Address");
                count++;
            }
            if (TextUtils.isEmpty(txt_name)) {
                name.setError("Enter your Name");
                count++;
            }
            if (TextUtils.isEmpty(txt_password)) {
                password.setError("Enter your Password");
                count++;
            }
            if (TextUtils.isEmpty(txt_confirmPassword)) {
                confirmPassword.setError("Confirm your Password");
                count++;
            }
            if (TextUtils.isEmpty(txt_phoneNo)) {
                phoneNo.setError("Enter your Phone Number");
                count++;
            }
            if (TextUtils.isEmpty(txt_address)) {
                address.setError("Enter your Address");
                count++;
            }
            if (!Objects.equals(txt_password, txt_confirmPassword)) {
                password.setError("Passwords do not match");
                confirmPassword.setError("Passwords do not match");
                count++;
            }

            if (count == 0) {
                if (txt_password.length() < 6) {
                    password.setError("Password too short\nMinimum length 6 characters!");
                    confirmPassword.setError("Password too short\nMinimum length 6 characters!");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()) {
                    Toast.makeText(DonorSignUpActivity.this, "Please provide a valid Email Address", Toast.LENGTH_SHORT).show();
                } else {
                    signUpUser(txt_email, txt_password);
                }
            }
        });

        // Redirect to login if already registered
        loginRedirectText.setOnClickListener(view -> {
            startActivity(new Intent(DonorSignUpActivity.this, LoginActivity.class));
            finish();
        });
    }

    // Method to sign up new user
    private void signUpUser(String txtEmail, String txt_password) {
        auth.createUserWithEmailAndPassword(txtEmail, txt_password).addOnCompleteListener(DonorSignUpActivity.this, task -> {
            if (task.isSuccessful()) {
                // Store user data in Firebase
                storeUserData();
                // Redirect to login activity
                startActivity(new Intent(DonorSignUpActivity.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(DonorSignUpActivity.this, "The Email Address is already registered for an existing account!\nTry again with a different email!", Toast.LENGTH_LONG).show();
            }
        });

        // Handle back press using OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(DonorSignUpActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    // Helper method to store user data in Firebase Realtime Database
    private void storeUserData() {
        try {
            txt_email = email.getText().toString().trim();
            txt_name = name.getText().toString().trim();
            txt_phoneNo = phoneNo.getText().toString().trim();
            txt_address = address.getText().toString().trim();

            // User information object
            UserInfoClass user = new UserInfoClass(txt_name, txt_email, txt_phoneNo, txt_address);
            String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

            // Save to Firebase Database
            DatabaseReference userRef = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("Users")
                    .child(userID);

            userRef.setValue(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(DonorSignUpActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DonorSignUpActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> Toast.makeText(DonorSignUpActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show());

        } catch (Exception e) {
            Toast.makeText(DonorSignUpActivity.this, "Enter the values properly!", Toast.LENGTH_SHORT).show();
        }
    }
}
