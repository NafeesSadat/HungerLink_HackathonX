package com.example.hungerlink;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

public class MainActivity extends AppCompatActivity {

    CardView cardDonate, cardReceive, cardProfile, cardDonationList, cardContact, cardHistory, cardLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        requestNotificationPermission();

        // Initializing card views
        cardDonate = findViewById(R.id.cardDonate);
        cardReceive = findViewById(R.id.cardReceive);
        cardProfile = findViewById(R.id.cardProfile);
        cardDonationList = findViewById(R.id.cardDonationList);
        cardContact = findViewById(R.id.cardContact);
        cardHistory = findViewById(R.id.cardhistory);
        cardLogout = findViewById(R.id.cardLogout);

        // Set click listeners for each card view
        cardDonate.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DonateActivity.class);
            startActivity(intent);
        });

        cardReceive.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReceiveActivity.class);
            startActivity(intent);
        });

        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        cardDonationList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MyDonationListActivity.class);
            startActivity(intent);
        });

        cardContact.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NearbyNGOActivity.class);
            startActivity(intent);
        });

        cardHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        cardLogout.setOnClickListener(v -> {
            // Log out logic or redirect to login screen
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the main activity after logging out
        });
    }

    private void requestNotificationPermission() {
        // Check if the device is running on Android 13 (API level 33) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {    
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission was denied
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
