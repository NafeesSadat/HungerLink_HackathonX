package com.example.hungerlink;

import com.google.android.gms.maps.model.LatLng;

public class DonationInfo {
    private String donationId;
    private String name;
    private String foodItems;
    private String phoneNumber;
    private String address;
    private LatLng latLng;  // Use LatLng for coordinates
    private String imageUrl;

    // Default constructor required for calls to DataSnapshot.getValue(DonationInfo.class)
    public DonationInfo() {
    }

    // Parameterized constructor
    public DonationInfo(String donationId, String name, String foodItems, String phoneNumber, String address, LatLng latLng, String imageUrl) {
        this.donationId = donationId;
        this.name = name;
        this.foodItems = foodItems;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.latLng = latLng;  // Set LatLng directly
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getDonationId() {
        return donationId;
    }

    public String getName() {
        return name;
    }

    public String getFoodItems() {
        return foodItems;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public LatLng getLatLng() {
        return latLng;  // Return LatLng object
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // New methods to get longitude and latitude
    public double getLongitude() {
        return latLng != null ? latLng.longitude : 0.0; // Return longitude or 0.0 if latLng is null
    }

    public double getLatitude() {
        return latLng != null ? latLng.latitude : 0.0; // Return latitude or 0.0 if latLng is null
    }

    // Setters
    public void setDonationId(String donationId) {
        this.donationId = donationId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFoodItems(String foodItems) {
        this.foodItems = foodItems;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatLng(LatLng latLng) {  // Setter for LatLng
        this.latLng = latLng;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
