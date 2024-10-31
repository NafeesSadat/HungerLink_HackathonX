//package com.example.hungerlink;
//
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.util.Log;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//
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
//
//public class MyDonationListActivity extends AppCompatActivity {
//
//    private ArrayList<DonationInfo> donationList;
//    private MyDonationAdapter adapter;
//    private DatabaseReference databaseReference;
//    private String currentUserId;
//
//    private TextView noDataTextView;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_donation_list);
//
//        ListView listView = findViewById(R.id.listview);
//        noDataTextView = findViewById(R.id.noDataTextView);
//        donationList = new ArrayList<>();
//        adapter = new MyDonationAdapter(this, donationList);
//        listView.setAdapter(adapter);
//
//        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
//                .getReference().child("Donation Information");
//        currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
//
//        fetchDonationData();
//
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            DonationInfo selectedDonation = donationList.get(position);
//            Log.d("MyDonationListActivity", "Selected Donation ID: " + selectedDonation.getDonationId());
//            Intent intent1 = new Intent(MyDonationListActivity.this, MyDonationListDetailActivity.class);
//            intent1.putExtra("donationId", selectedDonation.getDonationId());
//            startActivity(intent1);
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
//                        for (DataSnapshot donationSnapshot : userSnapshot.getChildren()) {
//                            String donationId = donationSnapshot.getKey();
//                            String status = donationSnapshot.child("Status").getValue(String.class);
//                            String name = donationSnapshot.child("name").getValue(String.class);
//                            String foodItems = donationSnapshot.child("foodItems").getValue(String.class);
//                            String phoneNumber = donationSnapshot.child("phoneNumber").getValue(String.class);
//                            String address = donationSnapshot.child("address").getValue(String.class);
//                            Double longitude = donationSnapshot.child("longitude").getValue(Double.class);
//                            Double latitude = donationSnapshot.child("latitude").getValue(Double.class);
//                            String imageUrl = donationSnapshot.child("imageUrl").getValue(String.class);
//
//                            LatLng latLng = null;
//                            if (latitude != null && longitude != null) {
//                                latLng = new LatLng(latitude, longitude);
//                            }
//
//                            if (name != null && imageUrl != null) {
//                                DonationInfo donationInfo = new DonationInfo(donationId, name, foodItems, phoneNumber, address, latLng, imageUrl, status);
//                                donationList.add(donationInfo);
//                            }
//                        }
//                    }
//                }
//                adapter.notifyDataSetChanged();
//
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
//                Toast.makeText(MyDonationListActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show();
//                Log.e("MyDonationListActivity", "Database error: ", error.toException());
//            }
//        });
//    }
//
//
//}
//


package com.example.hungerlink.ActivityClasses;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.example.hungerlink.HelperClasses.DonationInfo;
import com.example.hungerlink.DataAdapters.MyDonationAdapter;
import com.example.hungerlink.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MyDonationListActivity extends AppCompatActivity {

    private ArrayList<DonationInfo> donationList; // List to hold donation information
    private MyDonationAdapter adapter; // Adapter for the ListView
    private DatabaseReference databaseReference; // Reference to the Firebase database
    private String currentUserId; // Current user's ID

    private TextView noDataTextView; // TextView to show when there are no donations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donation_list);

        // Initialize ListView and TextView
        ListView listView = findViewById(R.id.listview);
        noDataTextView = findViewById(R.id.noDataTextView);
        donationList = new ArrayList<>(); // Initialize the donation list
        adapter = new MyDonationAdapter(this, donationList); // Create adapter with donation list
        listView.setAdapter(adapter); // Set adapter to the ListView

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Donation Information");

        // Get the current user's ID
        currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        // Fetch donation data from the database
        fetchDonationData();

        // Set up click listener for the ListView items
        listView.setOnItemClickListener((parent, view, position, id) -> {
            DonationInfo selectedDonation = donationList.get(position);
            Log.d("MyDonationListActivity", "Selected Donation ID: " + selectedDonation.getDonationId());
            Intent intent1 = new Intent(MyDonationListActivity.this, MyDonationListDetailActivity.class);
            intent1.putExtra("donationId", selectedDonation.getDonationId()); // Pass donation ID to detail activity
            startActivity(intent1);
        });
    }

    // Method to fetch donation data from the Firebase database
    private void fetchDonationData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donationList.clear(); // Clear the list before fetching new data
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    // Check if the user ID matches the current user
                    if (Objects.equals(userSnapshot.getKey(), currentUserId)) {
                        for (DataSnapshot donationSnapshot : userSnapshot.getChildren()) {
                            // Extract donation details
                            String donationId = donationSnapshot.getKey();
                            String status = donationSnapshot.child("Status").getValue(String.class);
                            String name = donationSnapshot.child("name").getValue(String.class);
                            String foodItems = donationSnapshot.child("foodItems").getValue(String.class);
                            String phoneNumber = donationSnapshot.child("phoneNumber").getValue(String.class);
                            String address = donationSnapshot.child("address").getValue(String.class);
                            Double longitude = donationSnapshot.child("longitude").getValue(Double.class);
                            Double latitude = donationSnapshot.child("latitude").getValue(Double.class);
                            String imageUrl = donationSnapshot.child("imageUrl").getValue(String.class);

                            // Create LatLng object if coordinates are available
                            LatLng latLng = null;
                            if (latitude != null && longitude != null) {
                                latLng = new LatLng(latitude, longitude);
                            }

                            // Check if name and image URL are not null before creating DonationInfo
                            if (name != null && imageUrl != null) {
                                DonationInfo donationInfo = new DonationInfo(donationId, name, foodItems, phoneNumber, address, latLng, imageUrl, status);
                                donationList.add(donationInfo); // Add donation info to the list
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged(); // Notify adapter of data changes

                // Show or hide the no data message based on the donation list
                if (donationList.isEmpty()) {
                    Log.d("MyDonationListActivity", "No Data Available");
                    noDataTextView.setVisibility(TextView.VISIBLE); // Show no data message
                } else {
                    noDataTextView.setVisibility(TextView.GONE); // Hide no data message
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors when fetching data
                Toast.makeText(MyDonationListActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show();
                Log.e("MyDonationListActivity", "Database error: ", error.toException());
            }
        });
    }
}
