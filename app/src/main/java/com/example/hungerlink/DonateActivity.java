package com.example.hungerlink;

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

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
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
                            notifyAllUsers(name, foodItems, address);
                        } else {
                            Toast.makeText(DonateActivity.this, "Failed to submit donation", Toast.LENGTH_SHORT).show();
                        }
                    });
                })).addOnFailureListener(e -> Toast.makeText(DonateActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show());
    }

    private void notifyAllUsers(String name, String foodItems, String address) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Users");

        Log.e("DonateActivity", usersRef.toString());

        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (DataSnapshot userSnapshot : task.getResult().getChildren()) {
                    String userId = userSnapshot.getKey();
                    Log.e("DonateActivity", userId);
                    // Check if this user is not the current donor
                    if (!Objects.equals(userId, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        // Send notification to this user
                        NotificationActivity.showNotification(
                                DonateActivity.this,
                                "New Donation Available",
                                "A new donation is available: " + name + " has donated " + foodItems + " at " + address
                        );
                    }
                }
            } else {
                Log.e("DonateActivity", "Failed to fetch users for notification.");
            }
        });
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
