//package com.example.hungerlink;
//
//import android.content.Context;
//import android.content.Intent;
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
//
//import java.util.ArrayList;
//
//public class DonationAdapter extends ArrayAdapter<DonationInfo> {
//    private final Context context;
//    private final ArrayList<DonationInfo> donationList;
//
//    public DonationAdapter(Context context, ArrayList<DonationInfo> donationList) {
//        super(context, 0, donationList);
//        this.context = context;
//        this.donationList = donationList;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//        // Inflate the view if it hasn't been created
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
//        }
//
//        // Get the donation item at the current position
//        DonationInfo donation = donationList.get(position);
//
//        // Set the donation name
//        TextView nameView = convertView.findViewById(R.id.listName);
//        nameView.setText(donation.getName() != null ? donation.getName() : "N/A");
//
//        // Load the donation image using Glide
//        ImageView imageView = convertView.findViewById(R.id.listImage);
//        Glide.with(context)
//                .load(donation.getImageUrl())
//                .into(imageView);
//
//        TextView distanceView = convertView.findViewById(R.id.listDistance);
//
//        // Set an OnClickListener on the entire item view to open ReceiveDetailActivity
//        convertView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, ReceiveDetailActivity.class);
//            intent.putExtra("donationId", donation.getDonationId()); // Pass the donation ID
//            context.startActivity(intent);
//        });
//
//        return convertView;
//    }
//}



package com.example.hungerlink;

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

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class DonationAdapter extends ArrayAdapter<DonationInfo> {
    private final Context context;
    private final ArrayList<DonationInfo> donationList;
    private Location userLocation;

    public DonationAdapter(Context context, ArrayList<DonationInfo> donationList) {
        super(context, 0, donationList);
        this.context = context;
        this.donationList = donationList;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        DonationInfo donation = donationList.get(position);

        TextView nameView = convertView.findViewById(R.id.listName);
        nameView.setText(donation.getName() != null ? donation.getName() : "N/A");

        ImageView imageView = convertView.findViewById(R.id.listImage);
        Glide.with(context)
                .load(donation.getImageUrl())
                .into(imageView);

        TextView distanceView = convertView.findViewById(R.id.listDistance);
        if (userLocation != null && donation.getLatLng() != null) {
            double distance = calculateDistance(userLocation, donation.getLatLng());
            distanceView.setText(String.format("%.2f km", distance));
        } else {
            distanceView.setText("N/A");
        }

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReceiveDetailActivity.class);
            intent.putExtra("donationId", donation.getDonationId());
            context.startActivity(intent);
        });

        return convertView;
    }

    private double calculateDistance(Location userLocation, LatLng donationLatLng) {
        float[] results = new float[1];
        Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),
                donationLatLng.latitude, donationLatLng.longitude, results);
        return results[0] / 1000;  // Convert meters to kilometers
    }
}
