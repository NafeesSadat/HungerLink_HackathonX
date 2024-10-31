package com.example.hungerlink.ActivityClasses;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hungerlink.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DonateActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 100;

    private EditText editTextName, editTextFoodItems, editTextPhoneNumber;
    private AutoCompleteTextView editTextAddress;
    private ImageView imagePreview;
    private Uri imageUri;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String userId;

    private ListView addressSuggestions;
    private ArrayAdapter<String> suggestionsAdapter;
    private List<String> addressList;
    private PlacesClient placesClient;
    private String selectedPlaceId; // Store the selected place ID for fetching details

    // Declare the ActivityResultLauncher
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private LatLng latLng;

    private List<String> placeIdList = new ArrayList<>(); // Add this to your class fields

    private String status = "Available";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        // Initialize Views
        editTextName = findViewById(R.id.editTextName_d);
        editTextFoodItems = findViewById(R.id.editTextFoodItems);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber_d);
        editTextAddress = findViewById(R.id.editTextAddress);
        imagePreview = findViewById(R.id.imagePreview);
        Button buttonDonate = findViewById(R.id.buttonDonate);
        Button buttonUploadImage = findViewById(R.id.buttonUploadImage);
        addressSuggestions = findViewById(R.id.addressSuggestions);

        // Firebase references
        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Donation Information").child(userId);
        storageReference = FirebaseStorage.getInstance().getReference("donation_images");

        // Initialize Places SDK
        Places.initialize(getApplicationContext(), "AIzaSyDRB6n2ZMOZApWSGUnUr91QTvdXbIIFn1I"); // Replace with your API key
        placesClient = Places.createClient(this);

        // Initialize ActivityResultLauncher for image picking
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                imageUri = result.getData().getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imagePreview.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Address suggestions
        addressList = new ArrayList<>();
        suggestionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, addressList);
        addressSuggestions.setAdapter(suggestionsAdapter);
        addressSuggestions.setVisibility(View.GONE);

        editTextAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fetchAddressSuggestions(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        addressSuggestions.setOnItemClickListener((adapterView, view, i, l) -> {
            String selectedAddress = suggestionsAdapter.getItem(i);
            editTextAddress.setText(selectedAddress);
            addressSuggestions.setVisibility(View.GONE);

            // Get the corresponding placeId from the list based on index
            String selectedPlaceId = placeIdList.get(i);
            fetchPlaceDetails(selectedPlaceId); // Fetch place details using the selected place ID
        });

        buttonUploadImage.setOnClickListener(v -> openImageChooser());
        buttonDonate.setOnClickListener(v -> submitDonation());
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent); // Use the launcher to start the intent
    }

    private void fetchAddressSuggestions(String query) {
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setSessionToken(token)
                .build();

        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener(response -> {
                    addressList.clear();
                    placeIdList.clear(); // Clear old place IDs
                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                        addressList.add(prediction.getFullText(null).toString());
                        placeIdList.add(prediction.getPlaceId()); // Save the place ID to a list
                    }
                    suggestionsAdapter.notifyDataSetChanged();
                    addressSuggestions.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(exception -> Log.e("PlacesAPI", "Error fetching address suggestions", exception));
    }

    private void fetchPlaceDetails(String placeId) {
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, List.of(Place.Field.LAT_LNG, Place.Field.ADDRESS));
        placesClient.fetchPlace(request)
                .addOnSuccessListener(response -> {
                    selectedPlaceId = placeId; // Store selected place ID
                    Log.d("PlacesAPI", "Selected place ID: " + selectedPlaceId); // Log the selected place ID

                    // Directly get the LatLng from the response
                    latLng = response.getPlace().getLatLng(); // Save it to the class variable

                    if (latLng != null) {
                        Log.d("PlacesAPI", "Latitude: " + latLng.latitude + ", Longitude: " + latLng.longitude);
                    }
                })
                .addOnFailureListener(exception -> {
                    Log.e("PlacesAPI", "Error fetching place details", exception);
                });
    }


    private void submitDonation() {
        final String name = editTextName.getText().toString().trim();
        final String foodItems = editTextFoodItems.getText().toString().trim();
        final String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        final String address = editTextAddress.getText().toString().trim();

        if (name.isEmpty() || foodItems.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if selectedPlaceId is set and use the latLng already fetched in fetchPlaceDetails()
        if (selectedPlaceId != null) {
            // If you already have latLng saved in an instance variable from fetchPlaceDetails()
            if (latLng != null) {
                // Save donation data with the stored latLng
                saveDonationData(name, foodItems, phoneNumber, address, latLng.latitude, latLng.longitude);
            } else {
                Toast.makeText(DonateActivity.this, "Failed to retrieve location data. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select an address", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDonationData(String name, String foodItems, String phoneNumber, String address, double latitude, double longitude) {
        final StorageReference fileReference = storageReference.child(userId + "/" + System.currentTimeMillis() + ".jpg");
        fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    Map<String, Object> donationData = new HashMap<>();
                    donationData.put("name", name);
                    donationData.put("foodItems", foodItems);
                    donationData.put("phoneNumber", phoneNumber);
                    donationData.put("address", address);
                    donationData.put("imageUrl", uri.toString());
                    donationData.put("latitude", latitude);
                    donationData.put("longitude", longitude);
                    donationData.put("Status", status);

                    databaseReference.push().setValue(donationData).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(DonateActivity.this, "Donation submitted successfully", Toast.LENGTH_SHORT).show();
                            clearFields();
                        } else {
                            Toast.makeText(DonateActivity.this, "Failed to submit donation", Toast.LENGTH_SHORT).show();
                        }
                    });
                })).addOnFailureListener(e -> Toast.makeText(DonateActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show());
    }

    private void clearFields() {
        editTextName.setText("");
        editTextFoodItems.setText("");
        editTextPhoneNumber.setText("");
        editTextAddress.setText("");
        imagePreview.setImageResource(0); // Clear the image preview
        imageUri = null; // Reset image URI
        addressSuggestions.setVisibility(View.GONE); // Hide address suggestions
    }
}



//package com.example.hungerlink.ActivityClasses;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.widget.*;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.hungerlink.R;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.libraries.places.api.Places;
//import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
//import com.google.android.libraries.places.api.model.AutocompletePrediction;
//import com.google.android.libraries.places.api.model.Place;
//import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
//import com.google.android.libraries.places.api.net.PlacesClient;
//import com.google.android.libraries.places.api.net.FetchPlaceRequest;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
//public class DonateActivity extends AppCompatActivity {
//    private static final int REQUEST_LOCATION_PERMISSION = 100;
//
//    // Declare UI elements
//    private EditText editTextName, editTextFoodItems, editTextPhoneNumber;
//    private AutoCompleteTextView editTextAddress;
//    private ImageView imagePreview;
//    private Uri imageUri; // Holds the URI of the selected image
//    private DatabaseReference databaseReference; // Reference to Firebase Database
//    private StorageReference storageReference; // Reference to Firebase Storage
//    private String userId; // User ID of the currently logged-in user
//
//    private ListView addressSuggestions;
//    private ArrayAdapter<String> suggestionsAdapter;
//    private List<String> addressList;
//    private PlacesClient placesClient; // Google Places client
//    private String selectedPlaceId; // Holds the ID of the selected place for detailed info
//
//    // Declare the ActivityResultLauncher for image picking
//    private ActivityResultLauncher<Intent> imagePickerLauncher;
//    private LatLng latLng; // Holds latitude and longitude data of the selected address
//
//    private List<String> placeIdList = new ArrayList<>(); // List of place IDs for address suggestions
//
//    private String status = "Available"; // Default donation status
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_donate);
//
//        // Initialize views by finding them in the layout
//        editTextName = findViewById(R.id.editTextName_d);
//        editTextFoodItems = findViewById(R.id.editTextFoodItems);
//        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber_d);
//        editTextAddress = findViewById(R.id.editTextAddress);
//        imagePreview = findViewById(R.id.imagePreview);
//        Button buttonDonate = findViewById(R.id.buttonDonate);
//        Button buttonUploadImage = findViewById(R.id.buttonUploadImage);
//        addressSuggestions = findViewById(R.id.addressSuggestions);
//
//        // Firebase initialization
//        try {
//            userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
//            databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
//                    .getReference().child("Donation Information").child(userId);
//            storageReference = FirebaseStorage.getInstance().getReference("donation_images");
//        } catch (Exception e) {
//            Toast.makeText(this, "Error initializing Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Initialize Places SDK for address suggestions
//        Places.initialize(getApplicationContext(), "AIzaSyDRB6n2ZMOZApWSGUnUr91QTvdXbIIFn1I"); // API key
//        placesClient = Places.createClient(this);
//
//        // Set up image picker launcher to handle image selection
//        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null && result.getData().getData() != null) {
//                imageUri = result.getData().getData();
//                try {
//                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    imagePreview.setImageBitmap(bitmap); // Display the selected image
//                } catch (Exception e) {
//                    Toast.makeText(this, "Error displaying image", Toast.LENGTH_SHORT).show();
//                    Log.e("ImagePicker", "Error displaying image", e);
//                }
//            }
//        });
//
//        // Setup for address suggestions using Google Places API
//        addressList = new ArrayList<>();
//        suggestionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, addressList);
//        addressSuggestions.setAdapter(suggestionsAdapter);
//        addressSuggestions.setVisibility(View.GONE); // Initially hide suggestions
//
//        // Listener to fetch address suggestions as user types
//        editTextAddress.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                fetchAddressSuggestions(charSequence.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) { }
//        });
//
//        // Handle address selection from suggestion list
//        addressSuggestions.setOnItemClickListener((adapterView, view, i, l) -> {
//            String selectedAddress = suggestionsAdapter.getItem(i);
//            editTextAddress.setText(selectedAddress);
//            addressSuggestions.setVisibility(View.GONE);
//
//            // Fetch detailed location data using place ID
//            String selectedPlaceId = placeIdList.get(i);
//            fetchPlaceDetails(selectedPlaceId);
//        });
//
//        // Set onClick listener for image upload button
//        buttonUploadImage.setOnClickListener(v -> openImageChooser());
//
//        // Set onClick listener for donation submission
//        buttonDonate.setOnClickListener(v -> submitDonation());
//    }
//
//    // Launches an intent to select an image from gallery
//    private void openImageChooser() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        imagePickerLauncher.launch(intent);
//    }
//
//    // Fetches address suggestions based on user input using Google Places API
//    private void fetchAddressSuggestions(String query) {
//        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
//        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
//                .setQuery(query)
//                .setSessionToken(token)
//                .build();
//
//        placesClient.findAutocompletePredictions(request)
//                .addOnSuccessListener(response -> {
//                    addressList.clear();
//                    placeIdList.clear(); // Clear old place IDs
//                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
//                        addressList.add(prediction.getFullText(null).toString());
//                        placeIdList.add(prediction.getPlaceId()); // Store the place ID
//                    }
//                    suggestionsAdapter.notifyDataSetChanged();
//                    addressSuggestions.setVisibility(View.VISIBLE); // Show suggestions
//                })
//                .addOnFailureListener(exception -> {
//                    Toast.makeText(this, "Failed to fetch address suggestions", Toast.LENGTH_SHORT).show();
//                    Log.e("PlacesAPI", "Error fetching address suggestions", exception);
//                });
//    }
//
//    // Fetches details (latitude, longitude) of a selected place ID
//    private void fetchPlaceDetails(String placeId) {
//        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, List.of(Place.Field.LAT_LNG, Place.Field.ADDRESS));
//        placesClient.fetchPlace(request)
//                .addOnSuccessListener(response -> {
//                    selectedPlaceId = placeId; // Store the selected place ID
//                    latLng = response.getPlace().getLatLng(); // Store latitude and longitude
//
//                    if (latLng != null) {
//                        Log.d("PlacesAPI", "Latitude: " + latLng.latitude + ", Longitude: " + latLng.longitude);
//                    }
//                })
//                .addOnFailureListener(exception -> {
//                    Toast.makeText(this, "Failed to fetch place details", Toast.LENGTH_SHORT).show();
//                    Log.e("PlacesAPI", "Error fetching place details", exception);
//                });
//    }
//
//    // Validates user input and initiates donation submission
//    private void submitDonation() {
//        final String name = editTextName.getText().toString().trim();
//        final String foodItems = editTextFoodItems.getText().toString().trim();
//        final String phoneNumber = editTextPhoneNumber.getText().toString().trim();
//        final String address = editTextAddress.getText().toString().trim();
//
//        // Check that all required fields are filled and an image is selected
//        if (name.isEmpty() || foodItems.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || imageUri == null) {
//            Toast.makeText(this, "Please fill all fields and upload an image", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Verify if a location was selected and latLng data is available
//        if (selectedPlaceId != null) {
//            if (latLng != null) {
//                saveDonationData(name, foodItems, phoneNumber, address, latLng.latitude, latLng.longitude);
//            } else {
//                Toast.makeText(this, "Location data is not available for the selected address", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(this, "Please select a valid address from suggestions", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    // Saves the donation data to Firebase Database and uploads the image to Firebase Storage
//    private void saveDonationData(String name, String foodItems, String phoneNumber, String address, double latitude, double longitude) {
//        StorageReference imageRef = storageReference.child(System.currentTimeMillis() + ".jpg");
//
//        // Upload image to Firebase Storage
//        imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
//                        .addOnSuccessListener(uri -> {
//                            // Prepare donation data map to upload to Firebase Database
//                            Map<String, Object> donationData = new HashMap<>();
//                            donationData.put("name", name);
//                            donationData.put("foodItems", foodItems);
//                            donationData.put("phoneNumber", phoneNumber);
//                            donationData.put("address", address);
//                            donationData.put("latitude", latitude);
//                            donationData.put("longitude", longitude);
//                            donationData.put("imageUri", uri.toString());
//                            donationData.put("status", status);
//
//                            databaseReference.push().setValue(donationData)
//                                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Donation submitted successfully", Toast.LENGTH_SHORT).show())
//                                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to submit donation", Toast.LENGTH_SHORT).show());
//                        })
//                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to get image URL", Toast.LENGTH_SHORT).show()))
//                .addOnFailureListener(e -> Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show());
//    }
//}
