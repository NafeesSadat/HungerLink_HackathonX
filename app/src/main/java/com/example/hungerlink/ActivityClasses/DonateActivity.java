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
    private static final int REQUEST_LOCATION_PERMISSION = 100; // Request code for location permission

    // UI components
    private EditText editTextName, editTextFoodItems, editTextPhoneNumber;
    private AutoCompleteTextView editTextAddress;
    private ImageView imagePreview;
    private Uri imageUri; // URI to store the selected image
    private ListView addressSuggestions;

    // Firebase references
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String userId;

    // Address suggestions list and adapter
    private ArrayAdapter<String> suggestionsAdapter;
    private List<String> addressList;
    private PlacesClient placesClient; // Google Places client for fetching location data
    private String selectedPlaceId; // ID of the selected place for further details

    // Image picker launcher
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    // Store the coordinates of the selected location
    private LatLng latLng;
    private List<String> placeIdList = new ArrayList<>(); // List to store Place IDs for suggestions
    private String status = "Available"; // Default status for donations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        // Initialize UI components
        editTextName = findViewById(R.id.editTextName_d);
        editTextFoodItems = findViewById(R.id.editTextFoodItems);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber_d);
        editTextAddress = findViewById(R.id.editTextAddress);
        imagePreview = findViewById(R.id.imagePreview);
        Button buttonDonate = findViewById(R.id.buttonDonate);
        Button buttonUploadImage = findViewById(R.id.buttonUploadImage);
        addressSuggestions = findViewById(R.id.addressSuggestions);

        // Setup Firebase references
        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Donation Information").child(userId);
        storageReference = FirebaseStorage.getInstance().getReference("donation_images");

        // Initialize Places API with the application's context
        Places.initialize(getApplicationContext(), "AIzaSyDRB6n2ZMOZApWSGUnUr91QTvdXbIIFn1I"); // Replace with actual API key
        placesClient = Places.createClient(this);

        // Register activity result launcher for image selection
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                imageUri = result.getData().getData(); // Store image URI
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imagePreview.setImageBitmap(bitmap); // Display selected image
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Initialize address suggestions list and adapter
        addressList = new ArrayList<>();
        suggestionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, addressList);
        addressSuggestions.setAdapter(suggestionsAdapter);
        addressSuggestions.setVisibility(View.GONE); // Initially hide the suggestions list

        // Set up listener for address input to fetch suggestions
        editTextAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fetchAddressSuggestions(charSequence.toString()); // Fetch suggestions as user types
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Handle address suggestion click
        addressSuggestions.setOnItemClickListener((adapterView, view, i, l) -> {
            String selectedAddress = suggestionsAdapter.getItem(i); // Get selected suggestion
            editTextAddress.setText(selectedAddress);
            addressSuggestions.setVisibility(View.GONE); // Hide suggestions list

            // Fetch place details for the selected address
            String selectedPlaceId = placeIdList.get(i);
            fetchPlaceDetails(selectedPlaceId);
        });

        // Set up button listeners
        buttonUploadImage.setOnClickListener(v -> openImageChooser()); // Opens image chooser
        buttonDonate.setOnClickListener(v -> submitDonation()); // Submits donation
    }

    // Opens the image picker to select an image
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent); // Start image picker intent
    }

    // Fetch address suggestions based on user input
    private void fetchAddressSuggestions(String query) {
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setSessionToken(token)
                .build();

        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener(response -> {
                    addressList.clear(); // Clear previous suggestions
                    placeIdList.clear(); // Clear previous Place IDs
                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                        addressList.add(prediction.getFullText(null).toString()); // Add suggestion text
                        placeIdList.add(prediction.getPlaceId()); // Add corresponding Place ID
                    }
                    suggestionsAdapter.notifyDataSetChanged();
                    addressSuggestions.setVisibility(View.VISIBLE); // Show suggestions list
                })
                .addOnFailureListener(exception -> Log.e("PlacesAPI", "Error fetching address suggestions", exception));
    }

    // Fetch details for a specific place based on its ID
    private void fetchPlaceDetails(String placeId) {
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, List.of(Place.Field.LAT_LNG, Place.Field.ADDRESS));
        placesClient.fetchPlace(request)
                .addOnSuccessListener(response -> {
                    selectedPlaceId = placeId; // Store selected place ID
                    latLng = response.getPlace().getLatLng(); // Retrieve LatLng coordinates

                    if (latLng != null) {
                        Log.d("PlacesAPI", "Latitude: " + latLng.latitude + ", Longitude: " + latLng.longitude);
                    }
                })
                .addOnFailureListener(exception -> Log.e("PlacesAPI", "Error fetching place details", exception));
    }

    // Submit donation with input validation
    private void submitDonation() {
        final String name = editTextName.getText().toString().trim();
        final String foodItems = editTextFoodItems.getText().toString().trim();
        final String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        final String address = editTextAddress.getText().toString().trim();

        if (name.isEmpty() || foodItems.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if selectedPlaceId is valid and latLng is already fetched
        if (selectedPlaceId != null && latLng != null) {
            saveDonationData(name, foodItems, phoneNumber, address, latLng.latitude, latLng.longitude); // Save with latLng data
        } else {
            Toast.makeText(DonateActivity.this, "Please select an address", Toast.LENGTH_SHORT).show();
        }
    }

    // Save donation details to Firebase
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

                            databaseReference.push().setValue(donationData)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(DonateActivity.this, "Donation submitted successfully", Toast.LENGTH_SHORT).show();
                                            clearFields();
                                            finish(); // Close activity on success
                                        } else {
                                            Toast.makeText(DonateActivity.this, "Error submitting donation", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }))
                .addOnFailureListener(e -> Toast.makeText(DonateActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show());
    }

    private void clearFields() {
        // Clear text fields by setting empty strings
        editTextName.setText("");
        editTextFoodItems.setText("");
        editTextPhoneNumber.setText("");
        editTextAddress.setText("");

        // Clear the image preview by setting a null resource
        imagePreview.setImageResource(0);

        // Reset the image URI to null to clear the selected image data
        imageUri = null;

        // Hide the address suggestions dropdown after submission
        addressSuggestions.setVisibility(View.GONE);
    }

}
