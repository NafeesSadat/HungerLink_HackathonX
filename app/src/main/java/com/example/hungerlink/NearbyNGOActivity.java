package com.example.hungerlink;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

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
import java.util.Objects;

public class NearbyNGOActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location userLocation;
    private DatabaseReference databaseReference;
    private final String TAG = "NearbyNGOActivity";
    private List<Polyline> polylines = new ArrayList<>(); // Track drawn polylines

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_ngoactivity); // Ensure this is correct

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Donation Information");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            fetchUserLocation();
        }
    }

    private void fetchUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                userLocation = location;
                Log.d(TAG, "User location: " + userLocation.getLatitude() + ", " + userLocation.getLongitude());
                initMap();
            } else {
                Log.e(TAG, "User location is null");
                Toast.makeText(this, "Unable to fetch location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initMap() {
        com.google.android.gms.maps.SupportMapFragment mapFragment =
                (com.google.android.gms.maps.SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e(TAG, "Map Fragment is null");
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Check if the Google Map is null
        if (mMap == null) {
            Log.e(TAG, "Map initialization failed. Check your API key and enable Maps SDK in Google Cloud Console.");
            Toast.makeText(this, "Map initialization failed. Check API key.", Toast.LENGTH_LONG).show();
            return;
        }

        // Check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Enable user location on the map
        mMap.setMyLocationEnabled(true);

        // Fetch donation locations and set up marker click listener
        fetchDonationLocations();
        mMap.setOnMarkerClickListener(this::onMarkerClick);
    }

    private void fetchDonationLocations() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.e(TAG, "No donation locations found in the database.");
                    return;
                }
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot donationSnapshot : userSnapshot.getChildren()) {
                        String name = donationSnapshot.child("name").getValue(String.class);
                        Double lat = donationSnapshot.child("latitude").getValue(Double.class);
                        Double lng = donationSnapshot.child("longitude").getValue(Double.class);

                        Log.d(TAG, "Donation - Name: " + name + ", Latitude: " + lat + ", Longitude: " + lng);

                        if (name != null && lat != null && lng != null) {
                            LatLng donationLocation = new LatLng(lat, lng);
                            mMap.addMarker(new MarkerOptions().position(donationLocation).title(name));
                        } else {
                            Log.e(TAG, "Missing data for a donation location.");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: ", error.toException());
            }
        });
    }

    private boolean onMarkerClick(Marker marker) {
        if (userLocation != null) {
            LatLng destination = marker.getPosition();
            showDirections(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()), destination);
        } else {
            Toast.makeText(this, "User location not found", Toast.LENGTH_SHORT).show();
        }
        return true; // Return true to indicate that the click was handled
    }

    private void showDirections(LatLng origin, LatLng destination) {
        // Clear previous polylines before drawing a new one
        clearPreviousPolylines();

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
                + origin.latitude + "," + origin.longitude
                + "&destination=" + destination.latitude + "," + destination.longitude
                + "&key=AIzaSyA6yE8ghQUdlgS74NTIGxNwuXRTY4nAL6k"; // Replace with your API key

        new FetchURL(this::drawPolyline).execute(url);
    }

    private void clearPreviousPolylines() {
        // Remove all previously drawn polylines from the map
        for (Polyline polyline : polylines) {
            polyline.remove();
        }
        polylines.clear(); // Clear the list of polylines
    }

    private void drawPolyline(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray routes = jsonObject.getJSONArray("routes");
            JSONObject route = routes.getJSONObject(0);
            JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
            String points = overviewPolyline.getString("points");

            List<LatLng> latLngList = decodePolyline(points);
            PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngList).color(0xFF2196F3).width(8);

            // Draw the polyline and add it to the list
            Polyline polyline = mMap.addPolyline(polylineOptions);
            polylines.add(polyline); // Keep track of the drawn polyline
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result >> 1) ^ -(result & 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result >> 1) ^ -(result & 1));
            lng += dlng;

            LatLng p = new LatLng(((double) lat / 1E5), ((double) lng / 1E5));
            poly.add(p);
        }
        return poly;
    }
}
