package com.example.hungerlink;

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

public class ReceiveDetailActivity extends AppCompatActivity {

    private TextView nameTextView, foodItemsTextView, phoneNumberTextView, addressTextView, longitudeTextView, latitudeTextView;
    private ImageView imageUrlImageView;
    private Button receiveButton;
    private String donationId;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_detail);

        // Initialize views
        nameTextView = findViewById(R.id.name);
        foodItemsTextView = findViewById(R.id.foodItems);
        phoneNumberTextView = findViewById(R.id.phoneNumber);
        addressTextView = findViewById(R.id.address);
        longitudeTextView = findViewById(R.id.longitude);
        latitudeTextView = findViewById(R.id.latitude);
        imageUrlImageView = findViewById(R.id.imageUrl);
        receiveButton = findViewById(R.id.buttonReceive);

        // Get the donation ID from the Intent
        Intent intent = getIntent();
        donationId = intent.getStringExtra("donationId");

        // Log the donation ID
        Log.d("ReceiveDetailActivity", "Received Donation ID: " + donationId);

        if (donationId == null) {
            Log.e("ReceiveDetailActivity", "Donation ID is null!");
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

                            Log.d("ReceiveDetailActivity", "Name: " + name + ", FoodItems: " + foodItems + ", Phone: " + phoneNumber + ", Address: " + address);

                            // Set values to views
                            nameTextView.setText(name);
                            foodItemsTextView.setText(foodItems);
                            phoneNumberTextView.setText(phoneNumber);
                            addressTextView.setText(address);
                            longitudeTextView.setText(longitude != null ? String.valueOf(longitude) : "N/A");
                            latitudeTextView.setText(latitude != null ? String.valueOf(latitude) : "N/A");

                            // Load image using Glide
                            Glide.with(ReceiveDetailActivity.this)
                                    .load(imageUrl)
                                    .into(imageUrlImageView);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReceiveDetailActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show();
                Log.e("ReceiveDetailActivity", "Database error: ", error.toException());
            }
        });
    }
}
