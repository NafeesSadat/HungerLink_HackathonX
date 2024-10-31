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
//
//import java.util.ArrayList;
//
//public class MyDonationAdapter extends ArrayAdapter<DonationInfo> {
//    private final Context context;
//    private final ArrayList<DonationInfo> donationList;
//    private Location userLocation;
//
//    public MyDonationAdapter(Context context, ArrayList<DonationInfo> donationList) {
//        super(context, 0, donationList);
//        this.context = context;
//        this.donationList = donationList;
//    }
//
//    @SuppressLint("DefaultLocale")
//    @NonNull
//    @Override
//    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.list_my_donation_item, parent, false);
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
//        TextView statusView = convertView.findViewById(R.id.listStatus);
//        statusView.setText(donation.getStatus() != null ? donation.getStatus() : "N/A");
//
//        convertView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, MyDonationListDetailActivity.class);
//            intent.putExtra("donationId", donation.getDonationId());
//            context.startActivity(intent);
//        });
//
//        return convertView;
//    }
//
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
import com.example.hungerlink.ActivityClasses.MyDonationListDetailActivity;
import com.example.hungerlink.HelperClasses.DonationInfo;
import com.example.hungerlink.R;

import java.util.ArrayList;

public class MyDonationAdapter extends ArrayAdapter<DonationInfo> {
    private final Context context; // Context for the adapter
    private final ArrayList<DonationInfo> donationList; // List of donations
    private Location userLocation; // To store user's location if needed

    // Constructor for the adapter
    public MyDonationAdapter(Context context, ArrayList<DonationInfo> donationList) {
        super(context, 0, donationList); // Call superclass constructor
        this.context = context;
        this.donationList = donationList;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Inflate the view if it has not been recycled
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_my_donation_item, parent, false);
        }

        // Get the current donation item
        DonationInfo donation = donationList.get(position);

        // Set up the name TextView
        TextView nameView = convertView.findViewById(R.id.listName);
        nameView.setText(donation.getName() != null ? donation.getName() : "N/A"); // Fallback if name is null

        // Set up the image using Glide
        ImageView imageView = convertView.findViewById(R.id.listImage);
        Glide.with(context)
                .load(donation.getImageUrl())
                .into(imageView); // Load image into ImageView

        // Set up the status TextView
        TextView statusView = convertView.findViewById(R.id.listStatus);
        statusView.setText(donation.getStatus() != null ? donation.getStatus() : "N/A"); // Fallback if status is null

        // Set an OnClickListener for the donation item
        convertView.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(context, MyDonationListDetailActivity.class);
                intent.putExtra("donationId", donation.getDonationId()); // Pass donation ID to detail activity
                context.startActivity(intent);
            } catch (Exception e) {
                // Handle any potential exceptions when starting the activity
                Toast.makeText(context, "Error opening donation details. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView; // Return the completed view
    }
}
