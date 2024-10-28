package com.example.hungerlink;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class HistoryActivity extends AppCompatActivity {

    private ArrayList<DonationInfo> receiveList;
    private HistoryAdapter adapter;
    private DatabaseReference databaseReference;
    private String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ListView listView = findViewById(R.id.listview);
        receiveList = new ArrayList<>();
        adapter = new HistoryAdapter(this, receiveList);
        listView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Receive Information");
        currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        fetchDonationData();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            DonationInfo selectedDonation = receiveList.get(position);
            Log.d("HistoryDetailActivity", "Selected Donation ID: " + selectedDonation.getDonationId());
            Intent intent1 = new Intent(HistoryActivity.this, HistoryDetailActivity.class);
            intent1.putExtra("donationId", selectedDonation.getDonationId());
            startActivity(intent1);
        });
    }

    private void fetchDonationData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                receiveList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    if (Objects.equals(userSnapshot.getKey(), currentUserId)) {
                        for (DataSnapshot donationSnapshot : userSnapshot.getChildren()) {
                            String donationId = donationSnapshot.getKey();
                            String status = donationSnapshot.child("Status").getValue(String.class);
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
                                receiveList.add(donationInfo);
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HistoryActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show();
                Log.e("HistoryDetailActivity", "Database error: ", error.toException());
            }
        });
    }


}

