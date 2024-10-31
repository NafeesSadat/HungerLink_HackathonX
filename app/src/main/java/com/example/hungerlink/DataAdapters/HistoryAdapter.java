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
//public class HistoryAdapter extends ArrayAdapter<DonationInfo> {
//    private final Context context;
//    private final ArrayList<DonationInfo> donationList;
//
//    public HistoryAdapter(Context context, ArrayList<DonationInfo> donationList) {
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
//            convertView = LayoutInflater.from(context).inflate(R.layout.list_history_item, parent, false);
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
//            Intent intent = new Intent(context, HistoryDetailActivity.class);
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.hungerlink.ActivityClasses.HistoryDetailActivity;
import com.example.hungerlink.HelperClasses.DonationInfo;
import com.example.hungerlink.R;

import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<DonationInfo> {
    private final Context context; // Context of the activity using this adapter
    private final ArrayList<DonationInfo> donationList; // List of DonationInfo objects

    // Constructor to initialize the adapter
    public HistoryAdapter(Context context, ArrayList<DonationInfo> donationList) {
        super(context, 0, donationList); // Call the parent constructor
        this.context = context; // Assign context
        this.donationList = donationList; // Assign donation list
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Inflate the layout if convertView is null
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_history_item, parent, false);
        }

        // Get the donation item for the current position
        DonationInfo donation = donationList.get(position);

        // Find and set the name TextView
        TextView nameView = convertView.findViewById(R.id.listName);
        nameView.setText(donation.getName() != null ? donation.getName() : "N/A"); // Set name or "N/A"

        // Find and load the image using Glide
        ImageView imageView = convertView.findViewById(R.id.listImage);
        // Error handling for image loading
        Glide.with(context)
                .load(donation.getImageUrl())
                .into(imageView);

        // Find and set the status TextView
        TextView statusView = convertView.findViewById(R.id.listStatus);
        statusView.setText(donation.getStatus() != null ? donation.getStatus() : "N/A"); // Set status or "N/A"

        // Set an onClickListener for the item to open HistoryDetailActivity
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HistoryDetailActivity.class); // Create an intent for the detail activity
            intent.putExtra("donationId", donation.getDonationId()); // Pass the donation ID
            context.startActivity(intent); // Start the detail activity
        });

        return convertView; // Return the completed view
    }
}
