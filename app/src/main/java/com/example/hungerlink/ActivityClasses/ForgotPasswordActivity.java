package com.example.hungerlink.ActivityClasses;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hungerlink.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_dialog);

        emailBox = findViewById(R.id.emailBox);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnReset = findViewById(R.id.btnReset);

        btnCancel.setOnClickListener(view -> finish());

        btnReset.setOnClickListener(view -> {
            String email = emailBox.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                // Proceed with Firebase password reset
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPasswordActivity.this, "Reset link sent to your email", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ForgotPasswordActivity.this, "Error sending reset email", Toast.LENGTH_SHORT).show();
                            }
                            try {
                                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                                finish();
                            } catch (Exception e) {
                                Toast.makeText(ForgotPasswordActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}

