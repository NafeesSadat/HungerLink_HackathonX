//package com.example.hungerlink;
//
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.bumptech.glide.Glide;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.Objects;
//
//public class MyDonationListDetailActivity extends AppCompatActivity {
//
//
//    private TextView nameTextView, foodItemsTextView, phoneNumberTextView, addressTextView, longitudeTextView, latitudeTextView, statusTextView;
//    private ImageView imageUrlImageView;
//    private Button cancelButton, completedButton;
//    private String donationId;
//
//    private DatabaseReference databaseReference;
//
//    private DatabaseReference databaseReferenceReceive;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_donation_list_detail);
//
//        // Initialize views
//        nameTextView = findViewById(R.id.name);
//        foodItemsTextView = findViewById(R.id.foodItems);
//        phoneNumberTextView = findViewById(R.id.phoneNumber);
//        addressTextView = findViewById(R.id.address);
//        longitudeTextView = findViewById(R.id.longitude);
//        latitudeTextView = findViewById(R.id.latitude);
//        imageUrlImageView = findViewById(R.id.imageUrl);
//        cancelButton = findViewById(R.id.buttonCancel);
//        completedButton = findViewById(R.id.buttonCompleted);
//        statusTextView = findViewById(R.id.status);
//
//        // Get the donation ID from the Intent
//        Intent intent = getIntent();
//        donationId = intent.getStringExtra("donationId");
//
//        // Log the donation ID
//        Log.d("MyDonationListDetailActivity", "DonationList Donation ID: " + donationId);
//
//        if (donationId == null) {
//            Log.e("MyDonationListDetailActivity", "MyDonationList ID is null!");
//            // Optionally, you can finish the activity or show a message to the user
//            Toast.makeText(this, "No donation data available.", Toast.LENGTH_SHORT).show();
//            finish(); // Close the activity
//            return; // Exit onCreate
//        }
//
//        // Initialize Firebase Database reference
//        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
//                .getReference("Donation Information");
//
//        // Initialize Firebase Database reference
//        databaseReferenceReceive = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
//                .getReference("Receive Information");
//
//        // Fetch donation info from the database
//        fetchDonationInfo();
//
//        // Set OnClickListener for the select button
//        completedButton.setOnClickListener(v -> {
//            updateDonationStatus("Completed", databaseReference);
//            updateDonationStatus("Completed", databaseReferenceReceive);
//        });
//
//        cancelButton.setOnClickListener(v -> {
//            updateDonationStatus("Canceled", databaseReference);
//            updateDonationStatus("Canceled", databaseReferenceReceive);
//        });
//    }
//
//    private void fetchDonationInfo() {
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
//                    for (DataSnapshot donationSnapshot : userSnapshot.getChildren()) {
//                        if (Objects.equals(donationSnapshot.getKey(), donationId)) {
//                            String name = donationSnapshot.child("name").getValue(String.class);
//                            String foodItems = donationSnapshot.child("foodItems").getValue(String.class);
//                            String phoneNumber = donationSnapshot.child("phoneNumber").getValue(String.class);
//                            String address = donationSnapshot.child("address").getValue(String.class);
//                            Double longitude = donationSnapshot.child("longitude").getValue(Double.class);
//                            Double latitude = donationSnapshot.child("latitude").getValue(Double.class);
//                            String imageUrl = donationSnapshot.child("imageUrl").getValue(String.class);
//                            String status = donationSnapshot.child("Status").getValue(String.class);
//
//                            Log.d("MyDonationListDetailActivity", "Name: " + name + ", FoodItems: " + foodItems + ", Phone: " + phoneNumber + ", Address: " + address);
//
//                            // Set values to views
//                            nameTextView.setText(name);
//                            foodItemsTextView.setText(foodItems);
//                            phoneNumberTextView.setText(phoneNumber);
//                            addressTextView.setText(address);
//                            longitudeTextView.setText(longitude != null ? "Longitude: " + longitude : "N/A");
//                            latitudeTextView.setText(latitude != null ? "Latitude: " + latitude : "N/A");
//                            statusTextView.setText(String.format("Status: %s", status));
//
//                            // Load image using Glide
//                            Glide.with(MyDonationListDetailActivity.this)
//                                    .load(imageUrl)
//                                    .into(imageUrlImageView);
//
//                            // Hide the completedButton if the status is "Completed"
//                            if ("Completed".equalsIgnoreCase(status)) {
//                                completedButton.setVisibility(Button.GONE);
//                                cancelButton.setVisibility(Button.GONE);
//                            } else if ("Canceled".equalsIgnoreCase(status)) {
//                                completedButton.setVisibility(Button.GONE);
//                                cancelButton.setVisibility(Button.GONE);
//                            }
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(MyDonationListDetailActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show();
//                Log.e("MyDonationListDetailActivity", "Database error: ", error.toException());
//            }
//        });
//    }
//
//    private void updateDonationStatus(String newStatus, DatabaseReference databaseReference) {
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                boolean donationFound = false; // Flag to check if donation is found
//
//                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
//                    for (DataSnapshot donationSnapshot : userSnapshot.getChildren()) {
//                        if (Objects.equals(donationSnapshot.getKey(), donationId)) {
//                            // Donation found, update the status
//                            donationSnapshot.getRef().child("Status").setValue(newStatus)
//                                    .addOnCompleteListener(task -> {
//                                        if (task.isSuccessful()) {
//                                            // Update the status TextView
//                                            statusTextView.setText(String.format("Status: %s", newStatus));
//                                            // Hide the select button
//                                            completedButton.setVisibility(Button.GONE);
//                                            cancelButton.setVisibility(Button.GONE);
//
//                                            // Show notification
//                                            NotificationActivity.showNotification(
//                                                    MyDonationListDetailActivity.this, // Context
//                                                    "Donation Status Updated",
//                                                    "The donation status has been changed to " + newStatus
//                                            );
//
//                                            Toast.makeText(MyDonationListDetailActivity.this, "Status updated to Pending", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            Toast.makeText(MyDonationListDetailActivity.this, "Failed to update status", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                            donationFound = true; // Mark that the donation was found
//                            break; // Exit the inner loop
//                        }
//                    }
//                    if (donationFound) {
//                        break; // Exit the outer loop if donation was found
//                    }
//                }
//
//                if (!donationFound) {
//                    Toast.makeText(MyDonationListDetailActivity.this, "Donation not found", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(MyDonationListDetailActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show();
//                Log.e("MyDonationListDetailActivity", "Database error: ", error.toException());
//            }
//        });
//    }
//
//}
//


package com.example.hungerlink.ActivityClasses;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.example.hungerlink.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;

public class MyDonationListDetailActivity extends AppCompatActivity {

    // Define UI elements
    private TextView nameTextView, foodItemsTextView, phoneNumberTextView, addressTextView, longitudeTextView, latitudeTextView, statusTextView;
    private ImageView imageUrlImageView;
    private Button cancelButton, completedButton;
    private String donationId; // To store the donation ID

    private DatabaseReference databaseReference; // Reference for Donation Information
    private DatabaseReference databaseReferenceReceive; // Reference for Receive Information

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donation_list_detail);

        // Initialize views
        nameTextView = findViewById(R.id.name);
        foodItemsTextView = findViewById(R.id.foodItems);
        phoneNumberTextView = findViewById(R.id.phoneNumber);
        addressTextView = findViewById(R.id.address);
        longitudeTextView = findViewById(R.id.longitude);
        latitudeTextView = findViewById(R.id.latitude);
        imageUrlImageView = findViewById(R.id.imageUrl);
        cancelButton = findViewById(R.id.buttonCancel);
        completedButton = findViewById(R.id.buttonCompleted);
        statusTextView = findViewById(R.id.status);

        // Get the donation ID from the Intent
        Intent intent = getIntent();
        donationId = intent.getStringExtra("donationId");

        // Log the donation ID for debugging
        Log.d("MyDonationListDetailActivity", "DonationList Donation ID: " + donationId);

        // Check if donationId is null
        if (donationId == null) {
            Log.e("MyDonationListDetailActivity", "MyDonationList ID is null!");
            Toast.makeText(this, "No donation data available.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if no data is found
            return; // Exit onCreate
        }

        // Initialize Firebase Database references
        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Donation Information");
        databaseReferenceReceive = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Receive Information");

        // Fetch donation info from the database
        fetchDonationInfo();

        // Set OnClickListener for the completed button
        completedButton.setOnClickListener(v -> {
            // Update the donation status to "Completed"
            updateDonationStatus("Completed", databaseReference);
            updateDonationStatus("Completed", databaseReferenceReceive);
        });

        // Set OnClickListener for the cancel button
        cancelButton.setOnClickListener(v -> {
            // Update the donation status to "Canceled"
            updateDonationStatus("Canceled", databaseReference);
            updateDonationStatus("Canceled", databaseReferenceReceive);
        });
    }

    // Fetch donation information from Firebase
    private void fetchDonationInfo() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Iterate through the snapshot to find the matching donation
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot donationSnapshot : userSnapshot.getChildren()) {
                        if (Objects.equals(donationSnapshot.getKey(), donationId)) {
                            // Extract donation details from the snapshot
                            String name = donationSnapshot.child("name").getValue(String.class);
                            String foodItems = donationSnapshot.child("foodItems").getValue(String.class);
                            String phoneNumber = donationSnapshot.child("phoneNumber").getValue(String.class);
                            String address = donationSnapshot.child("address").getValue(String.class);
                            Double longitude = donationSnapshot.child("longitude").getValue(Double.class);
                            Double latitude = donationSnapshot.child("latitude").getValue(Double.class);
                            String imageUrl = donationSnapshot.child("imageUrl").getValue(String.class);
                            String status = donationSnapshot.child("Status").getValue(String.class);

                            // Log extracted values for debugging
                            Log.d("MyDonationListDetailActivity", "Name: " + name + ", FoodItems: " + foodItems + ", Phone: " + phoneNumber + ", Address: " + address);

                            // Set values to the respective TextViews
                            nameTextView.setText(name);
                            foodItemsTextView.setText(foodItems);
                            phoneNumberTextView.setText(phoneNumber);
                            addressTextView.setText(address);
                            longitudeTextView.setText(longitude != null ? "Longitude: " + longitude : "N/A");
                            latitudeTextView.setText(latitude != null ? "Latitude: " + latitude : "N/A");
                            statusTextView.setText(String.format("Status: %s", status));

                            // Load the donation image using Glide
                            Glide.with(MyDonationListDetailActivity.this)
                                    .load(imageUrl)
                                    .into(imageUrlImageView);

                            // Hide the buttons if the donation is completed or canceled
                            if ("Completed".equalsIgnoreCase(status) || "Canceled".equalsIgnoreCase(status)) {
                                completedButton.setVisibility(Button.GONE);
                                cancelButton.setVisibility(Button.GONE);
                            }
                        }
                    }
                }
            }

            // Handle database errors
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyDonationListDetailActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show();
                Log.e("MyDonationListDetailActivity", "Database error: ", error.toException());
            }
        });
    }

    // Update the donation status in Firebase
    private void updateDonationStatus(String newStatus, DatabaseReference databaseReference) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean donationFound = false; // Flag to check if donation is found

                // Iterate through the snapshot to find the matching donation
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot donationSnapshot : userSnapshot.getChildren()) {
                        if (Objects.equals(donationSnapshot.getKey(), donationId)) {
                            // Donation found, update the status
                            donationSnapshot.getRef().child("Status").setValue(newStatus)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            // Update the status TextView
                                            statusTextView.setText(String.format("Status: %s", newStatus));
                                            // Hide the buttons
                                            completedButton.setVisibility(Button.GONE);
                                            cancelButton.setVisibility(Button.GONE);

                                            // Show notification of the status update
                                            NotificationActivity.showNotification(
                                                    MyDonationListDetailActivity.this, // Context
                                                    "Donation Status Updated",
                                                    "The donation status has been changed to " + newStatus
                                            );

                                            Toast.makeText(MyDonationListDetailActivity.this, "Status updated to " + newStatus, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MyDonationListDetailActivity.this, "Failed to update status", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            donationFound = true; // Mark that the donation was found
                            break; // Exit the inner loop
                        }
                    }
                    if (donationFound) {
                        break; // Exit the outer loop if donation was found
                    }
                }

                // If the donation was not found, show a message
                if (!donationFound) {
                    Toast.makeText(MyDonationListDetailActivity.this, "Donation not found", Toast.LENGTH_SHORT).show();
                }
            }

            // Handle database errors
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyDonationListDetailActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show();
                Log.e("MyDonationListDetailActivity", "Database error: ", error.toException());
            }
        });
    }
}
