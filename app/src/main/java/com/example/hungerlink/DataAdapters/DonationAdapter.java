//package com.example.hungerlink;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.Intent;
//import android.location.Location;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//
//import com.bumptech.glide.Glide;
//import com.google.android.gms.maps.model.LatLng;
//
//import java.util.ArrayList;
//
//public class DonationAdapter extends ArrayAdapter<DonationInfo> {
//    private final Context context;
//    private final ArrayList<DonationInfo> donationList;
//    private Location userLocation;
//
//    public DonationAdapter(Context context, ArrayList<DonationInfo> donationList) {
//        super(context, 0, donationList);
//        this.context = context;
//        this.donationList = donationList;
//    }
//
//    public void setUserLocation(Location userLocation) {
//        this.userLocation = userLocation;
//    }
//
//    @SuppressLint("DefaultLocale")
//    @NonNull
//    @Override
//    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
//        }
//
//        DonationInfo donation = donationList.get(position);
//
//        TextView nameView = convertView.findViewById(R.id.listName);
//        nameView.setText(donation.getName() != null ? donation.getName() : "N/A");
//
//        ImageView imageView = convertView.findViewById(R.id.listImage);
//        Glide.with(context)
//                .load(donation.getImageUrl())
//                .into(imageView);
//
//        TextView distanceView = convertView.findViewById(R.id.listDistance);
//        if (userLocation != null && donation.getLatLng() != null) {
//            double distance = calculateDistance(userLocation, donation.getLatLng());
//            distanceView.setText(String.format("%.2f km", distance));
//        } else {
//            distanceView.setText("N/A");
//        }
//
//        convertView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, ReceiveDetailActivity.class);
//            intent.putExtra("donationId", donation.getDonationId());
//            context.startActivity(intent);
//        });
//
//        return convertView;
//    }
//
//    private double calculateDistance(Location userLocation, LatLng donationLatLng) {
//        float[] results = new float[1];
//        Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),
//                donationLatLng.latitude, donationLatLng.longitude, results);
//        return results[0] / 1000;  // Convert meters to kilometers
//    }
//}


package com.example.hungerlink.DataAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.hungerlink.ActivityClasses.ReceiveDetailActivity;
import com.example.hungerlink.HelperClasses.DonationInfo;
import com.example.hungerlink.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class DonationAdapter extends ArrayAdapter<DonationInfo> {
    private final Context context;  // Context from the activity that instantiated the adapter
    private final ArrayList<DonationInfo> donationList;  // List of donations to display
    private Location userLocation;  // Current location of the user

    // Constructor for DonationAdapter
    public DonationAdapter(Context context, ArrayList<DonationInfo> donationList) {
        super(context, 0, donationList);
        this.context = context;
        this.donationList = donationList;
    }

    // Sets the user location to calculate distances to donations
    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    // Creates or updates each row view in the ListView for each donation item
    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Inflate the layout for the item if not already created
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        // Get the current donation item from the list based on position
        DonationInfo donation = donationList.get(position);

        // Set the name of the donation, checking if it exists to avoid NullPointerExceptions
        TextView nameView = convertView.findViewById(R.id.listName);
        nameView.setText(donation.getName() != null ? donation.getName() : "N/A");

        // Load and display the donation image using Glide library
        ImageView imageView = convertView.findViewById(R.id.listImage);
        Glide.with(context)
                .load(donation.getImageUrl())
                .into(imageView);

        // Calculate and display distance if user's location and donation location are available
        TextView distanceView = convertView.findViewById(R.id.listDistance);
        if (userLocation != null && donation.getLatLng() != null) {
            double distance = calculateDistance(userLocation, donation.getLatLng());
            distanceView.setText(String.format("%.2f km", distance));
        } else {
            distanceView.setText("N/A");  // Display "N/A" if location information is incomplete
            Toast.makeText(context, "Unable to calculate distance: location data is missing.", Toast.LENGTH_SHORT).show();
        }

        // Set up a click listener on each item in the list to open the detail page
        convertView.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(context, ReceiveDetailActivity.class);
                intent.putExtra("donationId", donation.getDonationId());  // Pass donation ID to detail activity
                context.startActivity(intent);  // Start the activity to view details of the donation
            } catch (Exception e) {
                Toast.makeText(context, "Error opening donation details. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;  // Return the view for the current list item
    }

    // Calculates the distance in kilometers between user's location and donation location
    private double calculateDistance(Location userLocation, LatLng donationLatLng) {
        float[] results = new float[1];  // Array to store the distance result
        // Calculate the distance between two geographical points
        Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),
                donationLatLng.latitude, donationLatLng.longitude, results);
        return results[0] / 1000;  // Convert meters to kilometers
    }
}

