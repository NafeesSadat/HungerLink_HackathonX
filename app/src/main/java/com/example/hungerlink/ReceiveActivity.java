package com.example.hungerlink;

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
    private ArrayList<DonationInfo> donationList;
    private DonationAdapter adapter;
    private DatabaseReference databaseReference;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location userLocation;

    private String currentUserId;

    private TextView noDataTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        noDataTextView = findViewById(R.id.noDataTextView);

        ListView listView = findViewById(R.id.listview);
        donationList = new ArrayList<>();
        adapter = new DonationAdapter(this, donationList);
        listView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Donation Information");

        currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchUserLocation();  // Fetch user’s location

        listView.setOnItemClickListener((parent, view, position, id) -> {
            DonationInfo selectedDonation = donationList.get(position);
            Log.d("ReceiveActivity", "Selected Donation ID: " + selectedDonation.getDonationId());
            Intent intent1 = new Intent(ReceiveActivity.this, ReceiveDetailActivity.class);
            intent1.putExtra("donationId", selectedDonation.getDonationId());
            startActivity(intent1);
        });
    }

    private void fetchUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                userLocation = location;
                fetchDonationData();  // Fetch donations once we have user location
            }
        });
    }

    private void fetchDonationData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donationList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    if (Objects.equals(userSnapshot.getKey(), currentUserId)) {
                        continue;
                    }
                    for (DataSnapshot donationSnapshot : userSnapshot.getChildren()) {
                        String donationId = donationSnapshot.getKey();
                        String status = donationSnapshot.child("Status").getValue(String.class);

                        // Skip donations with "Pending" status
                        if ("Pending".equalsIgnoreCase(status) || "Completed".equalsIgnoreCase(status) || "Canceled".equalsIgnoreCase(status)) {
                            continue;
                        }
                        String name = donationSnapshot.child("name").getValue(String.class);
                        String foodItems = donationSnapshot.child("foodItems").getValue(String.class);
                        String phoneNumber = donationSnapshot.child("phoneNumber").getValue(String.class);
                        String address = donationSnapshot.child("address").getValue(String.class);

                        Double longitude = donationSnapshot.child("longitude").getValue(Double.class);
                        Double latitude = donationSnapshot.child("latitude").getValue(Double.class);
                        String imageUrl = donationSnapshot.child("imageUrl").getValue(String.class);

                        LatLng latLng = null;
                        if (latitude != null && longitude != null) {
                            latLng = new LatLng(latitude, longitude);
                        }

                        if (name != null && imageUrl != null) {
                            DonationInfo donationInfo = new DonationInfo(donationId, name, foodItems, phoneNumber, address, latLng, imageUrl, status);
                            donationList.add(donationInfo);
                        }
                    }
                }
                adapter.setUserLocation(userLocation);  // Set user location in the adapter
                adapter.notifyDataSetChanged();



                Log.d("ReceiveActivity", "Donation List Size: " + donationList.size());

                // Toggle the visibility of the no data text view
                if (donationList.isEmpty()) {
                    Log.d("ReceiveActivity", "No Data Available");
                    noDataTextView.setVisibility(TextView.VISIBLE);

                } else {
                    noDataTextView.setVisibility(TextView.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReceiveActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show();
                Log.e("ReceiveActivity", "Database error: ", error.toException());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);  // Call to superclass

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchUserLocation();
        }
    }

}

