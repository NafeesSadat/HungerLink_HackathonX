//package com.example.hungerlink;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.Objects;
//
//public class ReceiveActivity extends AppCompatActivity {
//    private ArrayList<DonationInfo> donationList;
//    private DonationAdapter adapter;
//    private DatabaseReference databaseReference;
//    private FusedLocationProviderClient fusedLocationProviderClient;
//    private Location userLocation;
//
//    private String currentUserId;
//
//    private TextView noDataTextView;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_receive);
//        noDataTextView = findViewById(R.id.noDataTextView);
//
//        ListView listView = findViewById(R.id.listview);
//        donationList = new ArrayList<>();
//        adapter = new DonationAdapter(this, donationList);
//        listView.setAdapter(adapter);
//
//        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
//                .getReference().child("Donation Information");
//
//        currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        fetchUserLocation();  // Fetch user’s location
//
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            DonationInfo selectedDonation = donationList.get(position);
//            Log.d("ReceiveActivity", "Selected Donation ID: " + selectedDonation.getDonationId());
//            Intent intent1 = new Intent(ReceiveActivity.this, ReceiveDetailActivity.class);
//            intent1.putExtra("donationId", selectedDonation.getDonationId());
//            startActivity(intent1);
//        });
//    }
//
//    private void fetchUserLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//            return;
//        }
//        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
//            if (location != null) {
//                userLocation = location;
//                fetchDonationData();  // Fetch donations once we have user location
//            }
//        });
//    }
//
//    private void fetchDonationData() {
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                donationList.clear();
//                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
//                    if (Objects.equals(userSnapshot.getKey(), currentUserId)) {
//                        continue;
//                    }
//                    for (DataSnapshot donationSnapshot : userSnapshot.getChildren()) {
//                        String donationId = donationSnapshot.getKey();
//                        String status = donationSnapshot.child("Status").getValue(String.class);
//
//                        // Skip donations with "Pending" status
//                        if ("Pending".equalsIgnoreCase(status) || "Completed".equalsIgnoreCase(status) || "Canceled".equalsIgnoreCase(status)) {
//                            continue;
//                        }
//                        String name = donationSnapshot.child("name").getValue(String.class);
//                        String foodItems = donationSnapshot.child("foodItems").getValue(String.class);
//                        String phoneNumber = donationSnapshot.child("phoneNumber").getValue(String.class);
//                        String address = donationSnapshot.child("address").getValue(String.class);
//
//                        Double longitude = donationSnapshot.child("longitude").getValue(Double.class);
//                        Double latitude = donationSnapshot.child("latitude").getValue(Double.class);
//                        String imageUrl = donationSnapshot.child("imageUrl").getValue(String.class);
//
//                        LatLng latLng = null;
//                        if (latitude != null && longitude != null) {
//                            latLng = new LatLng(latitude, longitude);
//                        }
//
//                        if (name != null && imageUrl != null) {
//                            DonationInfo donationInfo = new DonationInfo(donationId, name, foodItems, phoneNumber, address, latLng, imageUrl, status);
//                            donationList.add(donationInfo);
//                        }
//                    }
//                }
//                adapter.setUserLocation(userLocation);  // Set user location in the adapter
//                adapter.notifyDataSetChanged();
//
//
//
//                Log.d("ReceiveActivity", "Donation List Size: " + donationList.size());
//
//                // Toggle the visibility of the no data text view
//                if (donationList.isEmpty()) {
//                    Log.d("ReceiveActivity", "No Data Available");
//                    noDataTextView.setVisibility(TextView.VISIBLE);
//
//                } else {
//                    noDataTextView.setVisibility(TextView.GONE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(ReceiveActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show();
//                Log.e("ReceiveActivity", "Database error: ", error.toException());
//            }
//        });
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);  // Call to superclass
//
//        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            fetchUserLocation();
//        }
//    }
//
//}
//

package com.example.hungerlink.ActivityClasses;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.hungerlink.DataAdapters.DonationAdapter;
import com.example.hungerlink.HelperClasses.DonationInfo;
import com.example.hungerlink.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ReceiveActivity extends AppCompatActivity {
    // List to hold donation information
    private ArrayList<DonationInfo> donationList;
    private DonationAdapter adapter; // Adapter for displaying donations
    private DatabaseReference databaseReference; // Reference to Firebase Database
    private FusedLocationProviderClient fusedLocationProviderClient; // Location client to get user's location
    private Location userLocation; // User's current location

    private String currentUserId; // Current user's ID
    private TextView noDataTextView; // TextView to show when there are no donations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive); // Set the layout for the activity

        // Initialize the no data TextView
        noDataTextView = findViewById(R.id.noDataTextView);

        // Initialize the ListView and its adapter
        ListView listView = findViewById(R.id.listview);
        donationList = new ArrayList<>();
        adapter = new DonationAdapter(this, donationList);
        listView.setAdapter(adapter); // Set the adapter to the ListView

        // Initialize Firebase Database reference for donations
        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Donation Information");

        // Get current user ID
        currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        // Initialize FusedLocationProviderClient to access location services
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchUserLocation();  // Fetch user’s location

        // Set up a click listener for the ListView items
        listView.setOnItemClickListener((parent, view, position, id) -> {
            DonationInfo selectedDonation = donationList.get(position); // Get selected donation
            Log.d("ReceiveActivity", "Selected Donation ID: " + selectedDonation.getDonationId());
            // Start ReceiveDetailActivity and pass the donation ID
            Intent intent1 = new Intent(ReceiveActivity.this, ReceiveDetailActivity.class);
            intent1.putExtra("donationId", selectedDonation.getDonationId());
            startActivity(intent1); // Launch the detail activity
        });
    }

    // Method to fetch the user's current location
    private void fetchUserLocation() {
        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return; // Exit the method
        }
        // Get the last known location
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                userLocation = location; // Store the user's location
                fetchDonationData();  // Fetch donation data once location is available
            }
        });
    }

    // Method to fetch donation data from the Firebase Database
    private void fetchDonationData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donationList.clear(); // Clear the existing donation list
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    // Skip donations from the current user
                    if (Objects.equals(userSnapshot.getKey(), currentUserId)) {
                        continue;
                    }
                    // Loop through each donation for the user
                    for (DataSnapshot donationSnapshot : userSnapshot.getChildren()) {
                        String donationId = donationSnapshot.getKey(); // Get donation ID
                        String status = donationSnapshot.child("Status").getValue(String.class); // Get donation status

                        // Skip donations with "Pending", "Completed", or "Canceled" status
                        if ("Pending".equalsIgnoreCase(status) || "Completed".equalsIgnoreCase(status) || "Canceled".equalsIgnoreCase(status)) {
                            continue;
                        }
                        // Extract donation details
                        String name = donationSnapshot.child("name").getValue(String.class);
                        String foodItems = donationSnapshot.child("foodItems").getValue(String.class);
                        String phoneNumber = donationSnapshot.child("phoneNumber").getValue(String.class);
                        String address = donationSnapshot.child("address").getValue(String.class);

                        // Get latitude and longitude
                        Double longitude = donationSnapshot.child("longitude").getValue(Double.class);
                        Double latitude = donationSnapshot.child("latitude").getValue(Double.class);
                        String imageUrl = donationSnapshot.child("imageUrl").getValue(String.class);

                        // Create a LatLng object if latitude and longitude are available
                        LatLng latLng = null;
                        if (latitude != null && longitude != null) {
                            latLng = new LatLng(latitude, longitude);
                        }

                        // Check if name and image URL are available before creating DonationInfo
                        if (name != null && imageUrl != null) {
                            DonationInfo donationInfo = new DonationInfo(donationId, name, foodItems, phoneNumber, address, latLng, imageUrl, status);
                            donationList.add(donationInfo); // Add donation info to the list
                        }
                    }
                }
                // Update the adapter with the user's location
                adapter.setUserLocation(userLocation);
                adapter.notifyDataSetChanged(); // Notify the adapter to refresh the ListView

                Log.d("ReceiveActivity", "Donation List Size: " + donationList.size());

                // Toggle the visibility of the no data text view
                if (donationList.isEmpty()) {
                    Log.d("ReceiveActivity", "No Data Available");
                    noDataTextView.setVisibility(TextView.VISIBLE); // Show no data message
                } else {
                    noDataTextView.setVisibility(TextView.GONE); // Hide no data message
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Show error message if data loading fails
                Toast.makeText(ReceiveActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show();
                Log.e("ReceiveActivity", "Database error: ", error.toException());
            }
        });
    }

    // Handle location permission request results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);  // Call to superclass

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchUserLocation(); // Fetch location if permission granted
        } else {
            // Show a message if permission was denied
            Toast.makeText(this, "Location permission denied. Unable to fetch location.", Toast.LENGTH_SHORT).show();
        }
    }
}

