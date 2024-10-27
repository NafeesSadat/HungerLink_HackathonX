package com.example.hungerlink;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyDonationListActivity extends AppCompatActivity {

    private ArrayList<DonationInfo> donationList;
    private DonationAdapter adapter;
    private DatabaseReference databaseReference;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donation_list);

        ListView listView = findViewById(R.id.listview);
        donationList = new ArrayList<>();
        adapter = new DonationAdapter(this, donationList);
        listView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Donation Information");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchUserLocation();  // Fetch user’s location

        listView.setOnItemClickListener((parent, view, position, id) -> {
            DonationInfo selectedDonation = donationList.get(position);
            Log.d("MyDonationListActivity", "Selected Donation ID: " + selectedDonation.getDonationId());
            Intent intent1 = new Intent(MyDonationListActivity.this, MyDonationListDetailActivity.class);
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
                    for (DataSnapshot donationSnapshot : userSnapshot.getChildren()) {
                        String donationId = donationSnapshot.getKey();
                        String status = donationSnapshot.child("Status").getValue(String.class);

                        // Skip donations with "Pending" status
                        if ("Pending".equalsIgnoreCase(status)) {
                            continue;
                        }
                        String name = donationSnapshot.child("name").getValue(String.class);
                        String foodItems = donationSnapshot.child("foodItems").getValue(String.class);
                        String phoneNumber = donationSnapshot.child("phoneNumber").getValue(String.class);
                        String address = donationSnapshot.child("address").getValue(String.class);
//                        String status = donationSnapshot.child("Status").getValue(String.class);

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyDonationListActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show();
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

