//package com.example.hungerlink;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
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
//public class ReceiveDetailActivity extends AppCompatActivity {
//
//    private TextView nameTextView, foodItemsTextView, phoneNumberTextView, addressTextView, longitudeTextView, latitudeTextView, statusTextView, showOnMapsTextVew;
//    private ImageView imageUrlImageView;
//    private Button selectButton;
//    private String donationId;
//
//    private DatabaseReference databaseReference;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_receive_detail);
//
//        // Initialize views
//        nameTextView = findViewById(R.id.name);
//        foodItemsTextView = findViewById(R.id.foodItems);
//        phoneNumberTextView = findViewById(R.id.phoneNumber);
//        addressTextView = findViewById(R.id.address);
//        longitudeTextView = findViewById(R.id.longitude);
//        latitudeTextView = findViewById(R.id.latitude);
//        imageUrlImageView = findViewById(R.id.imageUrl);
//        selectButton = findViewById(R.id.buttonSelect);
//        statusTextView = findViewById(R.id.status);
//        showOnMapsTextVew = findViewById(R.id.showOnMapsTextView); // Initialize the show on maps TextView
//
//        // Get the donation ID from the Intent
//        Intent intent = getIntent();
//        donationId = intent.getStringExtra("donationId");
//
//        // Log the donation ID
//        Log.d("ReceiveDetailActivity", "Received Donation ID: " + donationId);
//
//        if (donationId == null) {
//            Log.e("ReceiveDetailActivity", "Donation ID is null!");
//            Toast.makeText(this, "No donation data available.", Toast.LENGTH_SHORT).show();
//            finish(); // Close the activity
//            return; // Exit onCreate
//        }
//
//        // Initialize Firebase Database reference
//        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
//                .getReference("Donation Information");
//
//        // Fetch donation info from the database
//        fetchDonationInfo();
//
//        // Set OnClickListener for the select button
//        selectButton.setOnClickListener(v -> updateDonationStatus("Pending"));
//
//        // Set OnClickListener for the show on maps TextView
//        showOnMapsTextVew.setOnClickListener(v -> showDirections());
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
//                            Log.d("ReceiveDetailActivity", "Name: " + name + ", FoodItems: " + foodItems + ", Phone: " + phoneNumber + ", Address: " + address);
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
//                            Glide.with(ReceiveDetailActivity.this)
//                                    .load(imageUrl)
//                                    .into(imageUrlImageView);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(ReceiveDetailActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show();
//                Log.e("ReceiveDetailActivity", "Database error: ", error.toException());
//            }
//        });
//    }
//
//    private void updateDonationStatus(String newStatus) {
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
//                                            selectButton.setVisibility(Button.GONE);
//                                            Toast.makeText(ReceiveDetailActivity.this, "Status updated to Pending", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            Toast.makeText(ReceiveDetailActivity.this, "Failed to update status", Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(ReceiveDetailActivity.this, "Donation not found", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(ReceiveDetailActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show();
//                Log.e("ReceiveDetailActivity", "Database error: ", error.toException());
//            }
//        });
//    }
//
//    private void showDirections() {
//        // Get the latitude and longitude from the respective TextViews
//        String latitude = latitudeTextView.getText().toString().replace("Latitude: ", "").trim();
//        String longitude = longitudeTextView.getText().toString().replace("Longitude: ", "").trim();
//
//        // Check if latitude and longitude are valid
//        if (!latitude.equals("N/A") && !longitude.equals("N/A")) {
//            // Create the URI for the Maps intent
//            String uri = "google.navigation:q=" + latitude + "," + longitude;
//            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//            mapIntent.setPackage("com.google.android.apps.maps"); // Optional: set package for Google Maps
//            startActivity(mapIntent);
//        } else {
//            Toast.makeText(this, "Invalid location data.", Toast.LENGTH_SHORT).show();
//        }
//    }
//}



package com.example.hungerlink;

import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReceiveDetailActivity extends AppCompatActivity {

    private TextView nameTextView, foodItemsTextView, phoneNumberTextView, addressTextView, longitudeTextView, latitudeTextView, statusTextView, showOnMapsTextView;
    private ImageView imageUrlImageView;
    private Button selectButton;
    private String donationId;

    private DatabaseReference databaseReference;
    private String receiveImageUrl;

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
        selectButton = findViewById(R.id.buttonSelect);
        statusTextView = findViewById(R.id.status);
        showOnMapsTextView = findViewById(R.id.showOnMapsTextView);

        // Get the donation ID from the Intent
        Intent intent = getIntent();
        donationId = intent.getStringExtra("donationId");

        // Log the donation ID
        Log.d("ReceiveDetailActivity", "Received Donation ID: " + donationId);

        if (donationId == null) {
            Log.e("ReceiveDetailActivity", "Donation ID is null!");
            Toast.makeText(this, "No donation data available.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Donation Information");

        // Fetch donation info from the database
        fetchDonationInfo();

        // Set OnClickListener for the select button
        selectButton.setOnClickListener(v -> {
            updateDonationStatus("Pending", databaseReference);
            Log.e("ReceiveDetailActivity", "Status updated to Pending");
            saveToReceiveInformation();
            Log.e("ReceiveDetailActivity", "Information saved to Receive Information");
        });

        // Set OnClickListener for the show on maps TextView
        showOnMapsTextView.setOnClickListener(v -> showDirections());
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
                            receiveImageUrl = imageUrl;
                            String status = donationSnapshot.child("Status").getValue(String.class);

                            // Set values to views
                            nameTextView.setText(name);
                            foodItemsTextView.setText(foodItems);
                            phoneNumberTextView.setText(phoneNumber);
                            addressTextView.setText(address);
                            longitudeTextView.setText(longitude != null ? "Longitude: " + longitude : "N/A");
                            latitudeTextView.setText(latitude != null ? "Latitude: " + latitude : "N/A");
                            statusTextView.setText(String.format("Status: %s", status));

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

    private void updateDonationStatus(String newStatus, DatabaseReference databaseReference) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean donationFound = false;

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot donationSnapshot : userSnapshot.getChildren()) {
                        if (Objects.equals(donationSnapshot.getKey(), donationId)) {
                            donationSnapshot.getRef().child("Status").setValue(newStatus)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            statusTextView.setText(String.format("Status: %s", newStatus));
                                            selectButton.setVisibility(Button.GONE);
                                            Toast.makeText(ReceiveDetailActivity.this, "Status updated to Pending", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ReceiveDetailActivity.this, "Failed to update status", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            donationFound = true;
                            break;
                        }
                    }
                    if (donationFound) {
                        break;
                    }
                }

                if (!donationFound) {
                    Toast.makeText(ReceiveDetailActivity.this, "Donation not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReceiveDetailActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show();
                Log.e("ReceiveDetailActivity", "Database error: ", error.toException());
            }
        });
    }

    private void saveToReceiveInformation() {
        // Get the current user ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = currentUser.getUid();

        // Reference to the "Receive Information" path under the current user ID
        DatabaseReference receiveInfoRef = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Receive Information")
                .child(userId)
                .child(donationId); // Save under donationId for unique identification

        // Create a map to hold the donation data
        Map<String, Object> receiveData = new HashMap<>();
        receiveData.put("name", nameTextView.getText().toString());
        receiveData.put("foodItems", foodItemsTextView.getText().toString());
        receiveData.put("phoneNumber", phoneNumberTextView.getText().toString());
        receiveData.put("address", addressTextView.getText().toString());
        receiveData.put("longitude", Double.parseDouble(longitudeTextView.getText().toString().replace("Longitude: ", "")));
        receiveData.put("latitude", Double.parseDouble(latitudeTextView.getText().toString().replace("Latitude: ", "")));
        receiveData.put("imageUrl", receiveImageUrl);
        receiveData.put("Status", "Pending");

        // Save to Firebase under the current user's ID and donation ID
        receiveInfoRef.setValue(receiveData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ReceiveDetailActivity.this, "Information saved to Receive Information", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ReceiveDetailActivity.this, "Failed to save information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDirections() {
        String latitude = latitudeTextView.getText().toString().replace("Latitude: ", "").trim();
        String longitude = longitudeTextView.getText().toString().replace("Longitude: ", "").trim();

        if (!latitude.equals("N/A") && !longitude.equals("N/A")) {
            String uri = "google.navigation:q=" + latitude + "," + longitude;
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Invalid location data.", Toast.LENGTH_SHORT).show();
        }
    }
}
