package com.example.hungerlink;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    CardView cardDonate, cardReceive, cardNearbyNGO, cardAboutUs, cardContact, cardShare, cardLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        // Initializing card views
        cardDonate = findViewById(R.id.cardDonate);
        cardReceive = findViewById(R.id.cardReceive);
        cardNearbyNGO = findViewById(R.id.cardnearbyngo);
        cardAboutUs = findViewById(R.id.cardAboutus);
        cardContact = findViewById(R.id.cardContact);
        cardShare = findViewById(R.id.cardshare);
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

        cardNearbyNGO.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NearbyNGOActivity.class);
            startActivity(intent);
        });

        cardAboutUs.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(intent);
        });

        cardContact.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
            startActivity(intent);
        });

        cardShare.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShareActivity.class);
            startActivity(intent);
        });

        cardLogout.setOnClickListener(v -> {
            // Log out logic or redirect to login screen
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the main activity after logging out
        });
    }
}
