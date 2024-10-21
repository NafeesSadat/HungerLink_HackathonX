package com.example.hungerlink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class DonorSignUpActivity extends AppCompatActivity {
    private Button signUp;
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private EditText phoneNo;
    private EditText blood;
    private RadioButton male;
    private RadioButton female;
    private FirebaseAuth auth;

    String txt_email, txt_password, txt_confirmPassword, txt_name, txt_phoneNo, txt_blood, txt_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        phoneNo = findViewById(R.id.phone);
        blood = findViewById(R.id.blood);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        auth = FirebaseAuth.getInstance();
        signUp = findViewById(R.id.signUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_email = email.getText().toString().trim();
                txt_password = password.getText().toString().trim();
                txt_confirmPassword = confirmPassword.getText().toString().trim();
                txt_name = name.getText().toString().trim();
                txt_phoneNo = phoneNo.getText().toString().trim();
                txt_blood = blood.getText().toString().trim();

                int count = 0;

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
                if (TextUtils.isEmpty(txt_blood)) {
                    blood.setError("Enter your Blood Group");
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
                    }
                    else if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()) {
                        Toast.makeText(DonorSignUpActivity.this, "Please provide a valid Email Address", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        signUpUser(txt_email, txt_password);
                    }
                }
            }

        });
    }

    //Method to Sign up new User-->>
    private void signUpUser(String txtEmail, String txt_password) {

        auth.createUserWithEmailAndPassword(txtEmail, txt_password).addOnCompleteListener(DonorSignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                try {
                    txt_email = email.getText().toString().trim();
                    txt_confirmPassword = confirmPassword.getText().toString().trim();
                    txt_name = name.getText().toString().trim();
                    txt_phoneNo = phoneNo.getText().toString().trim();
                    txt_blood = blood.getText().toString().trim();

                    if (male.isChecked())
                        txt_gender = "Male";
                    else if (female.isChecked())
                        txt_gender = "Female";
                    else
                        txt_gender = "Not Available";
                } catch (Exception e) {
                    Toast.makeText(DonorSignUpActivity.this, "Enter the the values properly!", Toast.LENGTH_SHORT).show();
                }

                if (task.isSuccessful()) {
                    UserInfoClass user = new UserInfoClass(txt_name, txtEmail, txt_phoneNo, txt_blood, txt_gender);
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    FirebaseDatabase.getInstance().getReference("Users").child(userID)
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Contacts contacts = new Contacts("", "", "", "");
                                        FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Contacts").setValue(contacts).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(DonorSignUpActivity.this, "User has been registered successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    Toast.makeText(DonorSignUpActivity.this, "Registration failed!", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                    else {
                                        Toast.makeText(DonorSignUpActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DonorSignUpActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
                                }
                            });

                    startActivity(new Intent(DonorSignUpActivity.this, LoginActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(DonorSignUpActivity.this, "The Email Address is already registered for an existing account!\nTry again with a different email!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DonorSignUpActivity.this, LoginActivity.class));
    }
}