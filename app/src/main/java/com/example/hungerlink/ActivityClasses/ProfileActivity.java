//package com.example.hungerlink;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class ProfileActivity extends AppCompatActivity {
//
//    private TextView profileName;
//
//    private TextView profilename;
//    private TextView profileEmail;
//    private TextView profilePhone;
//    private TextView profileAddress;
//
//    private FirebaseAuth auth;
//    private DatabaseReference databaseReference;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//
//        // Initialize UI components
//        profileName = findViewById(R.id.profileNameText);
//        profilename = findViewById(R.id.profilename);
//        profileEmail = findViewById(R.id.profileEmailText);
//        profilePhone = findViewById(R.id.profilePhoneText);
//        profileAddress = findViewById(R.id.profileAddressText);
//
//        // Initialize Firebase Auth
//        auth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = auth.getCurrentUser();
//
//        if (currentUser != null) {
//            String userID = currentUser.getUid();
//
//            // Initialize Firebase Database reference
//            databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
//                    .getReference("Users")
//                    .child(userID);
//
//            // Fetch user data
//            fetchUserData();
//        } else {
//            // If user is not logged in, show a message
//            Toast.makeText(ProfileActivity.this, "User not logged in!", Toast.LENGTH_SHORT).show();
//            finish(); // Close the activity
//        }
//    }
//
//    // Method to fetch user data from Firebase Realtime Database
//    private void fetchUserData() {
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    // Extract user information from the snapshot
//                    UserInfoClass userInfo = snapshot.getValue(UserInfoClass.class);
//
//                    if (userInfo != null) {
//                        // Set the fetched user information to the TextViews
//                        profileName.setText(userInfo.getName());
//                        profilename.setText(userInfo.getName());
//                        profileEmail.setText(userInfo.getEmail());
//                        profilePhone.setText(userInfo.getPhoneNo());
//                        profileAddress.setText(userInfo.getAddress());
//                    } else {
//                        Toast.makeText(ProfileActivity.this, "User information is missing!", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(ProfileActivity.this, "No user data found!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Log error and show a message to the user
//                Log.e("ProfileActivity", "Failed to read user data", error.toException());
//                Toast.makeText(ProfileActivity.this, "Failed to load user data. Please try again!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}



package com.example.hungerlink.ActivityClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hungerlink.R;
import com.example.hungerlink.HelperClasses.UserInfoClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    // UI components
    private TextView profileName;
    private TextView profilename;
    private TextView profileEmail;
    private TextView profilePhone;
    private TextView profileAddress;

    // Firebase components
    private FirebaseAuth auth; // Firebase Auth instance
    private DatabaseReference databaseReference; // Reference to the Firebase Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // Set the layout for the activity

        // Initialize UI components
        profileName = findViewById(R.id.profileNameText); // Profile name TextView
        profilename = findViewById(R.id.profilename); // Profile name TextView (duplicate?)
        profileEmail = findViewById(R.id.profileEmailText); // Profile email TextView
        profilePhone = findViewById(R.id.profilePhoneText); // Profile phone TextView
        profileAddress = findViewById(R.id.profileAddressText); // Profile address TextView

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance(); // Get the Firebase Auth instance
        FirebaseUser currentUser = auth.getCurrentUser(); // Get the current user

        if (currentUser != null) {
            // User is logged in
            String userID = currentUser.getUid(); // Get the user ID

            // Initialize Firebase Database reference
            databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("Users") // Reference to the "Users" node
                    .child(userID); // Reference to the current user's node

            // Fetch user data
            fetchUserData(); // Call method to fetch user data
        } else {
            // User is not logged in, show a message and close the activity
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
                    // Snapshot exists, extract user information
                    UserInfoClass userInfo = snapshot.getValue(UserInfoClass.class); // Deserialize snapshot to UserInfoClass

                    if (userInfo != null) {
                        // Set the fetched user information to the TextViews
                        profileName.setText(userInfo.getName()); // Set profile name
                        profilename.setText(userInfo.getName()); // Set profile name again (duplicate?)
                        profileEmail.setText(userInfo.getEmail()); // Set profile email
                        profilePhone.setText(userInfo.getPhoneNo()); // Set profile phone number
                        profileAddress.setText(userInfo.getAddress()); // Set profile address
                    } else {
                        // User information is missing, show an error message
                        Toast.makeText(ProfileActivity.this, "User information is missing!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // No user data found, show an error message
                    Toast.makeText(ProfileActivity.this, "No user data found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log error and show a message to the user
                Log.e("ProfileActivity", "Failed to read user data", error.toException()); // Log the error
                Toast.makeText(ProfileActivity.this, "Failed to load user data. Please try again!", Toast.LENGTH_SHORT).show(); // Show error message
            }
        });
    }
}

