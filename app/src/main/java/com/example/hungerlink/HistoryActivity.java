//package com.example.hungerlink;
//
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.util.Log;
//import android.widget.ListView;
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
//public class HistoryActivity extends AppCompatActivity {
//
//    private ArrayList<DonationInfo> receiveList;
//    private HistoryAdapter adapter;
//    private DatabaseReference databaseReference;
//    private String currentUserId;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_history);
//
//        ListView listView = findViewById(R.id.listview);
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
//
//}
//



package com.example.hungerlink;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

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

    private ArrayList<DonationInfo> receiveList;
    private HistoryAdapter adapter;
    private DatabaseReference databaseReference;
    private String currentUserId;

    private TextView noDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ListView listView = findViewById(R.id.listview);
        noDataTextView = findViewById(R.id.noDataTextView);
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

        Button btnExportExcel = findViewById(R.id.exportButton);
        btnExportExcel.setOnClickListener(v -> exportToExcel());
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
                if (receiveList.isEmpty()) {
                    Log.d("ReceiveActivity", "No Data Available");
                    noDataTextView.setVisibility(TextView.VISIBLE);

                } else {
                    noDataTextView.setVisibility(TextView.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HistoryActivity.this, "Failed to load donations", Toast.LENGTH_SHORT).show();
                Log.e("HistoryDetailActivity", "Database error: ", error.toException());
            }
        });
    }

    private void exportToExcel() {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Receive History");

            // Create header row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("No."); // Change to "No."
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Food Items");
            header.createCell(3).setCellValue("Phone Number");
            header.createCell(4).setCellValue("Address");
            header.createCell(5).setCellValue("Status");

            // Fill in the data
            int rowCount = 1;
            for (DonationInfo donation : receiveList) {
                Row row = sheet.createRow(rowCount++);
                row.createCell(0).setCellValue(rowCount - 1); // Set sequential number
                row.createCell(1).setCellValue(donation.getName());
                row.createCell(2).setCellValue(donation.getFoodItems());
                row.createCell(3).setCellValue(donation.getPhoneNumber());
                row.createCell(4).setCellValue(donation.getAddress());
                row.createCell(5).setCellValue(donation.getStatus());
            }

            // Save the Excel file in app-specific external storage
            File file = new File(getExternalFilesDir(null), "ReceiveHistory.xlsx");
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                workbook.write(outputStream);
                Toast.makeText(this, "Exported to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

                // Open the Excel file automatically
                openExcelFile(file);

            } catch (IOException e) {
                Toast.makeText(this, "Error exporting file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HistoryActivity", "Export error: ", e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openExcelFile(File file) {
        Uri fileUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        // Check if there is an app to handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No application found to open the file.", Toast.LENGTH_SHORT).show();
        }
    }


}
