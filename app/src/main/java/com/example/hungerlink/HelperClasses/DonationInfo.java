//package com.example.hungerlink;
//
//import com.google.android.gms.maps.model.LatLng;
//
//public class DonationInfo {
//    private String donationId;
//    private String name;
//    private String foodItems;
//    private String phoneNumber;
//    private String address;
//    private LatLng latLng;  // Use LatLng for coordinates
//    private String imageUrl;
//    private String status;
//    private int streak;
//
//    // Default constructor required for calls to DataSnapshot.getValue(DonationInfo.class)
//    public DonationInfo() {
//    }
//
//    // Parameterized constructor
//    public DonationInfo(String donationId, String name, String foodItems, String phoneNumber, String address, LatLng latLng, String imageUrl, String status) {
//        this.donationId = donationId;
//        this.name = name;
//        this.foodItems = foodItems;
//        this.phoneNumber = phoneNumber;
//        this.address = address;
//        this.latLng = latLng;
//        this.imageUrl = imageUrl;
//        this.status = status;
//    }
//
//    // Getters
//    public String getDonationId() {
//        return donationId;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getFoodItems() {
//        return foodItems;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public LatLng getLatLng() {
//        return latLng;  // Return LatLng object
//    }
//
//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    // New methods to get longitude and latitude
//    public double getLongitude() {
//        return latLng != null ? latLng.longitude : 0.0; // Return longitude or 0.0 if latLng is null
//    }
//
//    public double getLatitude() {
//        return latLng != null ? latLng.latitude : 0.0; // Return latitude or 0.0 if latLng is null
//    }
//
//    // Setters
//    public void setDonationId(String donationId) {
//        this.donationId = donationId;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setFoodItems(String foodItems) {
//        this.foodItems = foodItems;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public void setLatLng(LatLng latLng) {  // Setter for LatLng
//        this.latLng = latLng;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//}


package com.example.hungerlink.HelperClasses;

import com.google.android.gms.maps.model.LatLng;

public class DonationInfo {
    // Fields representing details about the donation
    private String donationId;    // Unique identifier for the donation
    private String name;          // Name of the donation or donor
    private String foodItems;     // Description of food items donated
    private String phoneNumber;   // Contact phone number for the donation
    private String address;       // Address or location details of the donation
    private LatLng latLng;        // Latitude and longitude of the donation location
    private String imageUrl;      // URL for an image of the donation
    private String status;        // Current status of the donation (e.g., available, claimed)
    private int streak;           // Streak value for donor or donation tracking

    // Default constructor required for Firebase calls (e.g., DataSnapshot.getValue(DonationInfo.class))
    public DonationInfo() {
    }

    // Parameterized constructor for initializing a DonationInfo instance with specified values
    public DonationInfo(String donationId, String name, String foodItems, String phoneNumber, String address, LatLng latLng, String imageUrl, String status) {
        this.donationId = donationId;
        this.name = name;
        this.foodItems = foodItems;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.latLng = latLng;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    // Getter for donation ID
    public String getDonationId() {
        return donationId;
    }

    // Getter for donation or donor name
    public String getName() {
        return name;
    }

    // Getter for food items description
    public String getFoodItems() {
        return foodItems;
    }

    // Getter for contact phone number
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Getter for address details
    public String getAddress() {
        return address;
    }

    // Getter for coordinates as LatLng object
    public LatLng getLatLng() {
        return latLng;  // Returns the LatLng object containing latitude and longitude
    }

    // Getter for image URL
    public String getImageUrl() {
        return imageUrl;
    }

    // Getter for donation status
    public String getStatus() {
        return status;
    }

    // Getter for longitude value (handles null case by returning 0.0)
    public double getLongitude() {
        return latLng != null ? latLng.longitude : 0.0; // Return longitude or 0.0 if latLng is null
    }

    // Getter for latitude value (handles null case by returning 0.0)
    public double getLatitude() {
        return latLng != null ? latLng.latitude : 0.0; // Return latitude or 0.0 if latLng is null
    }

    // Setter for donation ID
    public void setDonationId(String donationId) {
        this.donationId = donationId;
    }

    // Setter for donation or donor name
    public void setName(String name) {
        this.name = name;
    }

    // Setter for food items description
    public void setFoodItems(String foodItems) {
        this.foodItems = foodItems;
    }

    // Setter for contact phone number
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Setter for address details
    public void setAddress(String address) {
        this.address = address;
    }

    // Setter for coordinates using LatLng
    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    // Setter for image URL
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Setter for donation status
    public void setStatus(String status) {
        this.status = status;
    }
}
