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

import java.util.ArrayList;

public class MyDonationAdapter extends ArrayAdapter<DonationInfo> {
    private final Context context;
    private final ArrayList<DonationInfo> donationList;
    private Location userLocation;

    public MyDonationAdapter(Context context, ArrayList<DonationInfo> donationList) {
        super(context, 0, donationList);
        this.context = context;
        this.donationList = donationList;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_my_donation_item, parent, false);
        }

        DonationInfo donation = donationList.get(position);

        TextView nameView = convertView.findViewById(R.id.listName);
        nameView.setText(donation.getName() != null ? donation.getName() : "N/A");

        ImageView imageView = convertView.findViewById(R.id.listImage);
        Glide.with(context)
                .load(donation.getImageUrl())
                .into(imageView);

        TextView statusView = convertView.findViewById(R.id.listStatus);
        statusView.setText(donation.getStatus() != null ? donation.getStatus() : "N/A");

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MyDonationListDetailActivity.class);
            intent.putExtra("donationId", donation.getDonationId());
            context.startActivity(intent);
        });

        return convertView;
    }

}
