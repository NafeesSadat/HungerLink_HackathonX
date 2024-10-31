//package com.example.hungerlink;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.FileProvider;
//
//import com.google.android.gms.maps.model.LatLng;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Objects;
//
//public class HistoryActivity extends AppCompatActivity {
//
//    private ArrayList<DonationInfo> receiveList;
//    private HistoryAdapter adapter;
//    private DatabaseReference databaseReference;
//    private String currentUserId;
//
//    private TextView noDataTextView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_history);
//
//        ListView listView = findViewById(R.id.listview);
//        noDataTextView = findViewById(R.id.noDataTextView);
//        receiveList = new ArrayList<>();
//        adapter = new HistoryAdapter(this, receiveList);
//        listView.setAdapter(adapter);
//
//        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
//                .getReference().child("Receive Information");
//        currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
//
//        fetchDonationData();
//
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            DonationInfo selectedDonation = receiveList.get(position);
//            Log.d("HistoryDetailActivity", "Selected Donation ID: " + selectedDonation.getDonationId());
//            Intent intent1 = new Intent(HistoryActivity.this, HistoryDetailActivity.class);
//            intent1.putExtra("donationId", selectedDonation.getDonationId());
//            startActivity(intent1);
//        });
//
//        Button btnExportExcel = findViewById(R.id.exportButton);
//        btnExportExcel.setOnClickListener(v -> exportToExcel());
//    }
//
//    private void fetchDonationData() {
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                receiveList.clear();
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
//                                receiveList.add(donationInfo);
//                            }
//                        }
//                    }
//                }
//                adapter.notifyDataSetChanged();
//                if (receiveList.isEmpty()) {
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
//                Toast.makeText(HistoryActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show();
//                Log.e("HistoryDetailActivity", "Database error: ", error.toException());
//            }
//        });
//    }
//
//    private void exportToExcel() {
//        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
//            Sheet sheet = workbook.createSheet("Receive History");
//
//            // Create header row
//            Row header = sheet.createRow(0);
//            header.createCell(0).setCellValue("No."); // Change to "No."
//            header.createCell(1).setCellValue("Name");
//            header.createCell(2).setCellValue("Food Items");
//            header.createCell(3).setCellValue("Phone Number");
//            header.createCell(4).setCellValue("Address");
//            header.createCell(5).setCellValue("Status");
//
//            // Fill in the data
//            int rowCount = 1;
//            for (DonationInfo donation : receiveList) {
//                Row row = sheet.createRow(rowCount++);
//                row.createCell(0).setCellValue(rowCount - 1); // Set sequential number
//                row.createCell(1).setCellValue(donation.getName());
//                row.createCell(2).setCellValue(donation.getFoodItems());
//                row.createCell(3).setCellValue(donation.getPhoneNumber());
//                row.createCell(4).setCellValue(donation.getAddress());
//                row.createCell(5).setCellValue(donation.getStatus());
//            }
//
//            // Save the Excel file in app-specific external storage
//            File file = new File(getExternalFilesDir(null), "ReceiveHistory.xlsx");
//            try (FileOutputStream outputStream = new FileOutputStream(file)) {
//                workbook.write(outputStream);
//                Toast.makeText(this, "Exported to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//
//                // Open the Excel file automatically
//                openExcelFile(file);
//
//            } catch (IOException e) {
//                Toast.makeText(this, "Error exporting file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("HistoryActivity", "Export error: ", e);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private void openExcelFile(File file) {
//        Uri fileUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(fileUri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//
//        // Check if there is an app to handle the intent
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "No application found to open the file.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//}


package com.example.hungerlink.ActivityClasses;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.hungerlink.HelperClasses.DonationInfo;
import com.example.hungerlink.DataAdapters.HistoryAdapter;
import com.example.hungerlink.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<DonationInfo> receiveList; // List to store donation information
    private HistoryAdapter adapter; // Adapter for the ListView
    private DatabaseReference databaseReference; // Reference to the Firebase Database
    private String currentUserId; // Current user's ID

    private TextView noDataTextView; // TextView to show if there's no data available

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history); // Set the layout for this activity

        ListView listView = findViewById(R.id.listview); // Initialize the ListView
        noDataTextView = findViewById(R.id.noDataTextView); // Initialize the TextView for no data
        receiveList = new ArrayList<>(); // Initialize the donation list
        adapter = new HistoryAdapter(this, receiveList); // Initialize the adapter
        listView.setAdapter(adapter); // Set the adapter to the ListView

        // Get reference to the Firebase database and fetch donation data
        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Receive Information");
        currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(); // Get current user's ID

        fetchDonationData(); // Fetch donation data from the database

        // Set item click listener for the ListView
        listView.setOnItemClickListener((parent, view, position, id) -> {
            DonationInfo selectedDonation = receiveList.get(position); // Get selected donation info
            Log.d("HistoryDetailActivity", "Selected Donation ID: " + selectedDonation.getDonationId()); // Log the selected donation ID
            Intent intent1 = new Intent(HistoryActivity.this, HistoryDetailActivity.class); // Create intent for HistoryDetailActivity
            intent1.putExtra("donationId", selectedDonation.getDonationId()); // Pass the donation ID
            startActivity(intent1); // Start the HistoryDetailActivity
        });

        Button btnExportExcel = findViewById(R.id.exportButton); // Initialize the export button
        btnExportExcel.setOnClickListener(v -> exportToExcel()); // Set click listener to export data to Excel
    }

    private void fetchDonationData() {
        // Fetch donation data from the Firebase database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                receiveList.clear(); // Clear the previous data
                // Iterate through each user snapshot
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    // Check if the user ID matches the current user's ID
                    if (Objects.equals(userSnapshot.getKey(), currentUserId)) {
                        // Iterate through each donation snapshot for the current user
                        for (DataSnapshot donationSnapshot : userSnapshot.getChildren()) {
                            // Retrieve donation information from the database
                            String donationId = donationSnapshot.getKey(); // Get donation ID
                            String status = donationSnapshot.child("Status").getValue(String.class); // Get donation status
                            String name = donationSnapshot.child("name").getValue(String.class); // Get donor name
                            String foodItems = donationSnapshot.child("foodItems").getValue(String.class); // Get food items
                            String phoneNumber = donationSnapshot.child("phoneNumber").getValue(String.class); // Get phone number
                            String address = donationSnapshot.child("address").getValue(String.class); // Get address
                            Double longitude = donationSnapshot.child("longitude").getValue(Double.class); // Get longitude
                            Double latitude = donationSnapshot.child("latitude").getValue(Double.class); // Get latitude
                            String imageUrl = donationSnapshot.child("imageUrl").getValue(String.class); // Get image URL

                            LatLng latLng = null; // Initialize LatLng object
                            // Create LatLng object if latitude and longitude are available
                            if (latitude != null && longitude != null) {
                                latLng = new LatLng(latitude, longitude);
                            }

                            // Create DonationInfo object and add it to the list if name and imageUrl are not null
                            if (name != null && imageUrl != null) {
                                DonationInfo donationInfo = new DonationInfo(donationId, name, foodItems, phoneNumber, address, latLng, imageUrl, status);
                                receiveList.add(donationInfo); // Add the donation info to the list
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged(); // Notify the adapter of data changes
                // Check if the receive list is empty
                if (receiveList.isEmpty()) {
                    Log.d("ReceiveActivity", "No Data Available"); // Log no data available
                    noDataTextView.setVisibility(TextView.VISIBLE); // Show no data TextView
                } else {
                    noDataTextView.setVisibility(TextView.GONE); // Hide no data TextView
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database read error
                Toast.makeText(HistoryActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show(); // Show error message
                Log.e("HistoryDetailActivity", "Database error: ", error.toException()); // Log error
            }
        });
    }

    private void exportToExcel() {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) { // Create a new workbook
            Sheet sheet = workbook.createSheet("Receive History"); // Create a new sheet

            // Create header row for the Excel sheet
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("No."); // Change to "No."
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Food Items");
            header.createCell(3).setCellValue("Phone Number");
            header.createCell(4).setCellValue("Address");
            header.createCell(5).setCellValue("Status");

            // Fill in the data for each donation in the list
            int rowCount = 1; // Initialize row count
            for (DonationInfo donation : receiveList) {
                Row row = sheet.createRow(rowCount++); // Create a new row for each donation
                row.createCell(0).setCellValue(rowCount - 1); // Set sequential number
                row.createCell(1).setCellValue(donation.getName()); // Set donor name
                row.createCell(2).setCellValue(donation.getFoodItems()); // Set food items
                row.createCell(3).setCellValue(donation.getPhoneNumber()); // Set phone number
                row.createCell(4).setCellValue(donation.getAddress()); // Set address
                row.createCell(5).setCellValue(donation.getStatus()); // Set status
            }

            // Save the Excel file in app-specific external storage
            File file = new File(getExternalFilesDir(null), "ReceiveHistory.xlsx"); // Create file for Excel
            try (FileOutputStream outputStream = new FileOutputStream(file)) { // Create output stream for file
                workbook.write(outputStream); // Write workbook to output stream
                Toast.makeText(this, "Exported to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show(); // Show success message

                // Open the Excel file automatically
                openExcelFile(file); // Call method to open the Excel file

            } catch (IOException e) {
                // Handle file export error
                Toast.makeText(this, "Error exporting file: " + e.getMessage(), Toast.LENGTH_SHORT).show(); // Show error message
                Log.e("HistoryActivity", "Export error: ", e); // Log export error
            }
        } catch (IOException e) {
            // Handle workbook creation error
            Toast.makeText(this, "Error creating workbook: " + e.getMessage(), Toast.LENGTH_SHORT).show(); // Show error message
            Log.e("HistoryActivity", "Workbook creation error: ", e); // Log workbook creation error
        }
    }

    private void openExcelFile(File file) {
        Uri fileUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file); // Get URI for the file
        Intent intent = new Intent(Intent.ACTION_VIEW); // Create intent to view the file
        intent.setDataAndType(fileUri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // Set MIME type
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant read permission
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // Prevent history entry for this intent
        try {
            startActivity(intent); // Start activity to open Excel file
        } catch (Exception e) {
            // Handle error when opening file
            Toast.makeText(this, "No application available to open Excel file", Toast.LENGTH_SHORT).show(); // Show error message
            Log.e("HistoryActivity", "Error opening Excel file: ", e); // Log error
        }
    }
}
