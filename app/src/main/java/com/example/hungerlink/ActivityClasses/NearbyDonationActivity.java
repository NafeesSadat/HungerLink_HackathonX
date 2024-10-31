//package com.example.hungerlink.ActivityClasses;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.ActivityCompat;
//import androidx.fragment.app.FragmentActivity;
//
//import com.example.hungerlink.HelperClasses.FetchURL;
//import com.example.hungerlink.R;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.model.Polyline;
//import com.google.android.gms.maps.model.PolylineOptions;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class NearbyDonationActivity extends FragmentActivity implements OnMapReadyCallback {
//
//    private GoogleMap mMap;
//    private FusedLocationProviderClient fusedLocationProviderClient;
//    private Location userLocation;
//    private DatabaseReference databaseReference;
//    private final String TAG = "NearbyDonationActivity";
//    private List<Polyline> polylines = new ArrayList<>(); // Track drawn polylines
//    private LatLng lastClickedMarkerPosition; // Store the position of the last clicked marker
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_nearby_donation); // Ensure this is correct
//
//        // Initialize fused location provider and database reference
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
//                .getReference().child("Donation Information");
//
//        // Request location permissions
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        } else {
//            fetchUserLocation();
//        }
//
//        // Initialize the TextView for opening Google Maps
//        TextView showOnMapsTextView = findViewById(R.id.showOnMapsTextView);
//        showOnMapsTextView.setOnClickListener(v -> {
//            if (userLocation != null && lastClickedMarkerPosition != null) {
//                openGoogleMaps(userLocation, lastClickedMarkerPosition);
//            } else {
//                Toast.makeText(this, "User location or destination not found", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void fetchUserLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
//            if (location != null) {
//                userLocation = location;
//                Log.d(TAG, "User location: " + userLocation.getLatitude() + ", " + userLocation.getLongitude());
//                initMap();
//            } else {
//                Log.e(TAG, "User location is null");
//                Toast.makeText(this, "Unable to fetch location", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void initMap() {
//        com.google.android.gms.maps.SupportMapFragment mapFragment =
//                (com.google.android.gms.maps.SupportMapFragment) getSupportFragmentManager()
//                        .findFragmentById(R.id.map);
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(this);
//        } else {
//            Log.e(TAG, "Map Fragment is null");
//        }
//    }
//
//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Check if the Google Map is null
//        if (mMap == null) {
//            Log.e(TAG, "Map initialization failed. Check your API key and enable Maps SDK in Google Cloud Console.");
//            Toast.makeText(this, "Map initialization failed. Check API key.", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        // Check for permissions
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//
//        // Enable user location on the map
//        mMap.setMyLocationEnabled(true);
//
//        // Fetch donation locations and set up marker click listener
//        fetchDonationLocations();
//        mMap.setOnMarkerClickListener(this::onMarkerClick);
//    }
//
//    private void fetchDonationLocations() {
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (!snapshot.exists()) {
//                    Log.e(TAG, "No donation locations found in the database.");
//                    return;
//                }
//                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
//                    for (DataSnapshot donationSnapshot : userSnapshot.getChildren()) {
//                        String name = donationSnapshot.child("name").getValue(String.class);
//                        Double lat = donationSnapshot.child("latitude").getValue(Double.class);
//                        Double lng = donationSnapshot.child("longitude").getValue(Double.class);
//
//                        Log.d(TAG, "Donation - Name: " + name + ", Latitude: " + lat + ", Longitude: " + lng);
//
//                        if (name != null && lat != null && lng != null) {
//                            LatLng donationLocation = new LatLng(lat, lng);
//                            mMap.addMarker(new MarkerOptions().position(donationLocation).title(name));
//                        } else {
//                            Log.e(TAG, "Missing data for a donation location.");
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e(TAG, "Database error: ", error.toException());
//            }
//        });
//    }
//
//    private boolean onMarkerClick(Marker marker) {
//        lastClickedMarkerPosition = marker.getPosition(); // Store the clicked marker position
//        if (userLocation != null) {
//            showDirections(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()), lastClickedMarkerPosition);
//        } else {
//            Toast.makeText(this, "User location not found", Toast.LENGTH_SHORT).show();
//        }
//        return true; // Return true to indicate that the click was handled
//    }
//
//    private void showDirections(LatLng origin, LatLng destination) {
//        // Clear previous polylines before drawing a new one
//        clearPreviousPolylines();
//
//        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
//                + origin.latitude + "," + origin.longitude
//                + "&destination=" + destination.latitude + "," + destination.longitude
//                + "&key=AIzaSyA6yE8ghQUdlgS74NTIGxNwuXRTY4nAL6k"; // Replace with your API key
//
//        new FetchURL(this::drawPolyline).execute(url);
//    }
//
//    private void clearPreviousPolylines() {
//        // Remove all previously drawn polylines from the map
//        for (Polyline polyline : polylines) {
//            polyline.remove();
//        }
//        polylines.clear(); // Clear the list of polylines
//    }
//
//    private void drawPolyline(String response) {
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            JSONArray routes = jsonObject.getJSONArray("routes");
//            JSONObject route = routes.getJSONObject(0);
//            JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
//            String points = overviewPolyline.getString("points");
//
//            List<LatLng> latLngList = decodePolyline(points);
//            PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngList).color(0xFF2196F3).width(8);
//
//            // Draw the polyline and add it to the list
//            Polyline polyline = mMap.addPolyline(polylineOptions);
//            polylines.add(polyline); // Keep track of the drawn polyline
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private List<LatLng> decodePolyline(String encoded) {
//        List<LatLng> poly = new ArrayList<>();
//        int index = 0, len = encoded.length();
//        int lat = 0, lng = 0;
//
//        while (index < len) {
//            int b, shift = 0, result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlat = ((result >> 1) ^ -(result & 1));
//            lat += dlat;
//
//            shift = 0;
//            result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlng = ((result >> 1) ^ -(result & 1));
//            lng += dlng;
//
//            LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
//            poly.add(p);
//        }
//        return poly;
//    }
//
//    private void openGoogleMaps(Location userLocation, LatLng destination) {
//        String uri = "http://maps.google.com/maps?saddr=" + userLocation.getLatitude() + "," + userLocation.getLongitude() +
//                "&daddr=" + destination.latitude + "," + destination.longitude;
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//        startActivity(intent);
//    }
//}



package com.example.hungerlink.ActivityClasses;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.hungerlink.HelperClasses.FetchURL;
import com.example.hungerlink.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NearbyDonationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap; // Google Map object for displaying the map
    private FusedLocationProviderClient fusedLocationProviderClient; // Client for accessing location services
    private Location userLocation; // User's current location
    private DatabaseReference databaseReference; // Reference to the Firebase database
    private final String TAG = "NearbyDonationActivity"; // Tag for logging
    private List<Polyline> polylines = new ArrayList<>(); // List to track drawn polylines on the map
    private LatLng lastClickedMarkerPosition; // Store the position of the last clicked marker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_donation); // Set the layout for this activity

        // Initialize fused location provider and database reference
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Donation Information");

        // Request location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If permission not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // If permission granted, fetch user location
            fetchUserLocation();
        }

        // Initialize the TextView for opening Google Maps
        Button showOnMapsButton = findViewById(R.id.showOnMapsButton);
        showOnMapsButton.setOnClickListener(v -> {
            // When the TextView is clicked, open Google Maps if user location and last marker position are available
            if (userLocation != null && lastClickedMarkerPosition != null) {
                openGoogleMaps(userLocation, lastClickedMarkerPosition);
            } else {
                Toast.makeText(this, "User location or destination not found", Toast.LENGTH_SHORT).show(); // Show a toast if data is missing
            }
        });
    }

    private void fetchUserLocation() {
        // Check for location permission before accessing location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; // Exit if permission is not granted
        }
        // Fetch the last known location
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                userLocation = location; // Store the user's location
                Log.d(TAG, "User location: " + userLocation.getLatitude() + ", " + userLocation.getLongitude());
                initMap(); // Initialize the map after obtaining the location
            } else {
                Log.e(TAG, "User location is null"); // Log error if location is null
                Toast.makeText(this, "Unable to fetch location", Toast.LENGTH_SHORT).show(); // Show toast for error
            }
        });
    }

    private void initMap() {
        // Initialize the map fragment
        com.google.android.gms.maps.SupportMapFragment mapFragment =
                (com.google.android.gms.maps.SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this); // Asynchronously get the map
        } else {
            Log.e(TAG, "Map Fragment is null"); // Log error if the map fragment is null
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap; // Set the GoogleMap object

        // Check if the Google Map is null
        if (mMap == null) {
            Log.e(TAG, "Map initialization failed. Check your API key and enable Maps SDK in Google Cloud Console."); // Log error if map initialization fails
            Toast.makeText(this, "Map initialization failed. Check API key.", Toast.LENGTH_LONG).show(); // Show toast for error
            return; // Exit if map is not initialized
        }

        // Check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; // Exit if permissions are not granted
        }

        // Enable user location on the map
        mMap.setMyLocationEnabled(true);

        // Fetch donation locations from the database
        fetchDonationLocations();
        mMap.setOnMarkerClickListener(this::onMarkerClick); // Set listener for marker clicks
    }

    private void fetchDonationLocations() {
        // Fetch donation locations from Firebase database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if any donation locations exist
                if (!snapshot.exists()) {
                    Log.e(TAG, "No donation locations found in the database."); // Log error if no locations found
                    return; // Exit if no locations exist
                }
                // Iterate through the database snapshot to retrieve donation locations
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot donationSnapshot : userSnapshot.getChildren()) {
                        // Get donation details from the snapshot
                        String name = donationSnapshot.child("name").getValue(String.class);
                        Double lat = donationSnapshot.child("latitude").getValue(Double.class);
                        Double lng = donationSnapshot.child("longitude").getValue(Double.class);

                        Log.d(TAG, "Donation - Name: " + name + ", Latitude: " + lat + ", Longitude: " + lng);

                        // Check if the name, latitude, and longitude are not null before adding a marker
                        if (name != null && lat != null && lng != null) {
                            LatLng donationLocation = new LatLng(lat, lng); // Create LatLng object for donation location
                            mMap.addMarker(new MarkerOptions().position(donationLocation).title(name)); // Add marker to the map
                        } else {
                            Log.e(TAG, "Missing data for a donation location."); // Log error if data is missing
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: ", error.toException()); // Log database error
            }
        });
    }

    private boolean onMarkerClick(Marker marker) {
        lastClickedMarkerPosition = marker.getPosition(); // Store the clicked marker position
        if (userLocation != null) {
            showDirections(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()), lastClickedMarkerPosition); // Show directions if user location is available
        } else {
            Toast.makeText(this, "User location not found", Toast.LENGTH_SHORT).show(); // Show toast if user location is missing
        }
        return true; // Return true to indicate that the click was handled
    }

    private void showDirections(LatLng origin, LatLng destination) {
        // Clear previous polylines before drawing a new one
        clearPreviousPolylines();

        // Build the Google Maps Directions API URL
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
                + origin.latitude + "," + origin.longitude
                + "&destination=" + destination.latitude + "," + destination.longitude
                + "&key=AIzaSyA6yE8ghQUdlgS74NTIGxNwuXRTY4nAL6k"; // Replace with your API key

        // Execute the FetchURL task to retrieve directions
        new FetchURL(this::drawPolyline).execute(url);
    }

    private void clearPreviousPolylines() {
        // Remove all previously drawn polylines from the map
        for (Polyline polyline : polylines) {
            polyline.remove(); // Remove polyline from the map
        }
        polylines.clear(); // Clear the list of polylines
    }

    private void drawPolyline(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response); // Parse the JSON response
            JSONArray routes = jsonObject.getJSONArray("routes"); // Get routes array
            JSONObject route = routes.getJSONObject(0); // Get the first route
            JSONObject overviewPolyline = route.getJSONObject("overview_polyline"); // Get overview polyline
            String points = overviewPolyline.getString("points"); // Extract encoded points

            // Create a PolylineOptions object to define the polyline's properties
            PolylineOptions options = new PolylineOptions()
                    .width(8) // Set width of the polyline
                    .color(getResources().getColor(R.color.purple_700)) // Set color of the polyline
                    .geodesic(true); // Make polyline geodesic

            // Decode the encoded points into LatLng objects and add them to the polyline options
            for (LatLng point : decodePoly(points)) {
                options.add(point); // Add point to polyline options
            }

            // Draw the polyline on the map and add it to the list of polylines
            Polyline polyline = mMap.addPolyline(options);
            polylines.add(polyline); // Store the polyline in the list
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response: ", e); // Log JSON parsing error
        }
    }

    // Helper method to decode encoded polyline points
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>(); // List to hold decoded points
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        // Loop through the encoded string to decode it
        while (index < len) {
            int b, shift = 0, result = 0;
            // Decode latitude
            do {
                b = encoded.charAt(index++) - 63; // Get the next character and convert it to a number
                result |= (b & 0x1f) << shift; // Shift and combine the result
                shift += 5; // Increment shift for next value
            } while (b >= 0x20);
            int dlat = ((result >> 1) ^ -(result & 1)); // Get the latitude
            lat += dlat; // Update latitude

            // Reset for longitude decoding
            shift = 0;
            result = 0;
            // Decode longitude
            do {
                b = encoded.charAt(index++) - 63; // Get the next character and convert it to a number
                result |= (b & 0x1f) << shift; // Shift and combine the result
                shift += 5; // Increment shift for next value
            } while (b >= 0x20);
            int dlng = ((result >> 1) ^ -(result & 1)); // Get the longitude
            lng += dlng; // Update longitude

            // Create a new LatLng object and add it to the list
            LatLng p = new LatLng(((double) lat / 1E5), ((double) lng / 1E5));
            poly.add(p); // Add the point to the polyline list
        }
        return poly; // Return the list of decoded points
    }

    private void openGoogleMaps(Location userLocation, LatLng destination) {
        // Create a URI to launch Google Maps with the user's location and destination
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destination.latitude + "," + destination.longitude + "&mode=d");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri); // Create an intent to view the map
        mapIntent.setPackage("com.google.android.apps.maps"); // Set the intent to open Google Maps
        startActivity(mapIntent); // Start the intent
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Check if location permission was granted
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchUserLocation(); // Fetch user location if permission granted
            } else {
                Toast.makeText(this, "Permission denied. Unable to access location.", Toast.LENGTH_SHORT).show(); // Show toast for denied permission
            }
        }
    }
}
