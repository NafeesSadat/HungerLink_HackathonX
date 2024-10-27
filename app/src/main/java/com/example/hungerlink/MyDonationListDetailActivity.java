package com.example.hungerlink;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MyDonationListDetailActivity extends AppCompatActivity {


    private TextView nameTextView, foodItemsTextView, phoneNumberTextView, addressTextView, longitudeTextView, latitudeTextView, statusTextView;
    private ImageView imageUrlImageView;
    private Button cancelButton, completedButton;
    private String donationId;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_docation_list_detail);

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

        // Log the donation ID
        Log.d("MyDonationListDetailActivity", "DonationList Donation ID: " + donationId);

        if (donationId == null) {
            Log.e("MyDonationListDetailActivity", "MyDonationList ID is null!");
            // Optionally, you can finish the activity or show a message to the user
            Toast.makeText(this, "No donation data available.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
            return; // Exit onCreate
        }

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Donation Information");

        // Fetch donation info from the database
        fetchDonationInfo();

        // Set OnClickListener for the select button
        completedButton.setOnClickListener(v -> updateDonationStatus("Pending"));
    }

    private void fetchDonationInfo() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot donationSnapshot : userSnapshot.getChildren()) {
                        if (Objects.equals(donationSnapshot.getKey(), donationId)) {
                            String name = donationSnapshot.child("name").getValue(String.class);
                            String foodItems = donationSnapshot.child("foodItems").getValue(String.class);
                            String phoneNumber = donationSnapshot.child("phoneNumber").getValue(String.class);
                            String address = donationSnapshot.child("address").getValue(String.class);
                            Double longitude = donationSnapshot.child("longitude").getValue(Double.class);
                            Double latitude = donationSnapshot.child("latitude").getValue(Double.class);
                            String imageUrl = donationSnapshot.child("imageUrl").getValue(String.class);
                            String status = donationSnapshot.child("Status").getValue(String.class);

                            Log.d("MyDonationListDetailActivity", "Name: " + name + ", FoodItems: " + foodItems + ", Phone: " + phoneNumber + ", Address: " + address);

                            // Set values to views
                            nameTextView.setText(name);
                            foodItemsTextView.setText(foodItems);
                            phoneNumberTextView.setText(phoneNumber);
                            addressTextView.setText(address);
                            longitudeTextView.setText(longitude != null ? "Longitude: " + longitude : "N/A");
                            latitudeTextView.setText(latitude != null ? "Latitude: " + latitude : "N/A");
                            statusTextView.setText(String.format("Status: %s", status));

                            // Load image using Glide
                            Glide.with(MyDonationListDetailActivity.this)
                                    .load(imageUrl)
                                    .into(imageUrlImageView);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyDonationListDetailActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show();
                Log.e("MyDonationListDetailActivity", "Database error: ", error.toException());
            }
        });
    }

    private void updateDonationStatus(String newStatus) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean donationFound = false; // Flag to check if donation is found

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot donationSnapshot : userSnapshot.getChildren()) {
                        if (Objects.equals(donationSnapshot.getKey(), donationId)) {
                            // Donation found, update the status
                            donationSnapshot.getRef().child("Status").setValue(newStatus)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            // Update the status TextView
                                            statusTextView.setText(String.format("Status: %s", newStatus));
                                            // Hide the select button
                                            completedButton.setVisibility(Button.GONE);
                                            Toast.makeText(MyDonationListDetailActivity.this, "Status updated to Pending", Toast.LENGTH_SHORT).show();
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

                if (!donationFound) {
                    Toast.makeText(MyDonationListDetailActivity.this, "Donation not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyDonationListDetailActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show();
                Log.e("MyDonationListDetailActivity", "Database error: ", error.toException());
            }
        });
    }

}

