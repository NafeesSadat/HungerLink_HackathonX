package com.example.hungerlink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileName;

    private TextView profilename;
    private TextView profileEmail;
    private TextView profilePhone;
    private TextView profileAddress;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI components
        profileName = findViewById(R.id.profileNameText);
        profilename = findViewById(R.id.profilename);
        profileEmail = findViewById(R.id.profileEmailText);
        profilePhone = findViewById(R.id.profilePhoneText);
        profileAddress = findViewById(R.id.profileAddressText);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userID = currentUser.getUid();

            // Initialize Firebase Database reference
            databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("Users")
                    .child(userID);

            // Fetch user data
            fetchUserData();
        } else {
            // If user is not logged in, show a message
            Toast.makeText(ProfileActivity.this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        }
    }

    // Method to fetch user data from Firebase Realtime Database
    private void fetchUserData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Extract user information from the snapshot
                    UserInfoClass userInfo = snapshot.getValue(UserInfoClass.class);

                    if (userInfo != null) {
                        // Set the fetched user information to the TextViews
                        profileName.setText(userInfo.getName());
                        profilename.setText(userInfo.getName());
                        profileEmail.setText(userInfo.getEmail());
                        profilePhone.setText(userInfo.getPhoneNo());
                        profileAddress.setText(userInfo.getAddress());
                    } else {
                        Toast.makeText(ProfileActivity.this, "User information is missing!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "No user data found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log error and show a message to the user
                Log.e("ProfileActivity", "Failed to read user data", error.toException());
                Toast.makeText(ProfileActivity.this, "Failed to load user data. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
