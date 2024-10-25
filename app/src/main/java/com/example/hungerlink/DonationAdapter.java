package com.example.hungerlink;

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

import java.util.ArrayList;

public class DonationAdapter extends ArrayAdapter<DonationInfo> {
    private final Context context;
    private final ArrayList<DonationInfo> donationList;

    public DonationAdapter(Context context, ArrayList<DonationInfo> donationList) {
        super(context, 0, donationList);
        this.context = context;
        this.donationList = donationList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Inflate the view if it hasn't been created
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        // Get the donation item at the current position
        DonationInfo donation = donationList.get(position);

        // Set the donation name
        TextView nameView = convertView.findViewById(R.id.listName);
        nameView.setText(donation.getName() != null ? donation.getName() : "N/A");

        // Load the donation image using Glide
        ImageView imageView = convertView.findViewById(R.id.listImage);
        Glide.with(context)
                .load(donation.getImageUrl())
                .into(imageView);

        // Set an OnClickListener on the entire item view to open ReceiveDetailActivity
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReceiveDetailActivity.class);
            intent.putExtra("donationId", donation.getDonationId()); // Pass the donation ID
            context.startActivity(intent);
        });

        return convertView;
    }
}
