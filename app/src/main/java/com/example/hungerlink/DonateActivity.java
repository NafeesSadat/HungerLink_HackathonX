package com.example.hungerlink;////import android.content.Intent;
////import android.net.Uri;
////import android.os.Bundle;
////import android.view.View;
////import android.widget.EditText;
////import android.widget.ImageView;
////import android.widget.Toast;
////
////import androidx.activity.result.ActivityResultLauncher;
////import androidx.activity.result.contract.ActivityResultContracts;
////import androidx.appcompat.app.AppCompatActivity;
////
////import com.google.android.gms.tasks.OnSuccessListener;
////import com.google.firebase.database.DatabaseReference;
////import com.google.firebase.database.FirebaseDatabase;
////import com.google.firebase.storage.FirebaseStorage;
////import com.google.firebase.storage.StorageReference;
////import com.google.firebase.storage.UploadTask;
////
////public class DonateActivity extends AppCompatActivity {
////
////    private EditText editTextName, editTextFoodItems, editTextPhoneNumber, editTextAddress;
////    private ImageView imagePreview;
////    private DatabaseReference databaseReference;
////    private StorageReference storageReference;
////    private Uri imageUri; // To store the selected image URI
////
////    // Declare the ActivityResultLauncher
////    private ActivityResultLauncher<Intent> imagePickerLauncher;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_donate); // Change this to your actual layout file name
////
////        // Initialize Firebase Database and Storage
////        FirebaseDatabase database = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app");
////        databaseReference = database.getReference("Donation Information");
////        storageReference = FirebaseStorage.getInstance().getReference("donation_images"); // Folder in Storage
////
////        // Initialize UI components
////        editTextName = findViewById(R.id.editTextName_d);
////        editTextFoodItems = findViewById(R.id.editTextFoodItems);
////        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber_d);
////        editTextAddress = findViewById(R.id.editTextaddress);
////        imagePreview = findViewById(R.id.imagePreview);
////
////        // Initialize the ActivityResultLauncher for image selection
////        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
////            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
////                imageUri = result.getData().getData(); // Save the image URI
////                imagePreview.setImageURI(imageUri); // Set the selected image to ImageView
////            }
////        });
////
////        // Set click listener for the donate button
////        findViewById(R.id.buttonDonate).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                donate();
////            }
////        });
////
////        // Set up click listener for the image upload button
////        findViewById(R.id.buttonUploadImage).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                chooseImage();
////            }
////        });
////    }
////
////    private void donate() {
////        // Get user input
////        String name = editTextName.getText().toString().trim();
////        String foodItems = editTextFoodItems.getText().toString().trim();
////        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
////        String address = editTextAddress.getText().toString().trim();
////
////        // Validate inputs
////        if (name.isEmpty() || foodItems.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || imageUri == null) {
////            Toast.makeText(this, "Please fill in all fields and upload an image", Toast.LENGTH_SHORT).show();
////            return;
////        }
////
////        // Upload the image to Firebase Storage
////        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
////        fileReference.putFile(imageUri)
////                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
////                    @Override
////                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                        // Get the download URL
////                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
////                            @Override
////                            public void onSuccess(Uri uri) {
////                                String downloadUrl = uri.toString();
////
////                                // Create a Donation object
////                                Donation donation = new Donation(name, foodItems, phoneNumber, address, downloadUrl);
////
////                                // Save the donation data to Firebase
////                                databaseReference.push().setValue(donation)
////                                        .addOnCompleteListener(task -> {
////                                            if (task.isSuccessful()) {
////                                                Toast.makeText(DonateActivity.this, "Donation Successful!", Toast.LENGTH_SHORT).show();
////                                                clearInputs();
////                                            } else {
////                                                Toast.makeText(DonateActivity.this, "Donation Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
////                                            }
////                                        });
////                            }
////                        });
////                    }
////                })
////                .addOnFailureListener(e -> Toast.makeText(DonateActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
////    }
////
////    private void chooseImage() {
////        Intent intent = new Intent();
////        intent.setType("image/*");
////        intent.setAction(Intent.ACTION_GET_CONTENT);
////        // Use the ActivityResultLauncher to start the image picker
////        imagePickerLauncher.launch(Intent.createChooser(intent, "Select Donation Image"));
////    }
////
////    private void clearInputs() {
////        editTextName.setText("");
////        editTextFoodItems.setText("");
////        editTextPhoneNumber.setText("");
////        editTextAddress.setText("");
////        imagePreview.setImageResource(R.drawable.baseline_file_upload_24); // Reset the image
////        imageUri = null; // Clear the image URI
////    }
////
////    public static class Donation {
////        public String name;
////        public String foodItems;
////        public String phoneNumber;
////        public String address;
////        public String imageUrl; // Added to store the image URL
////
////        public Donation() {}
////
////        public Donation(String name, String foodItems, String phoneNumber, String address, String imageUrl) {
////            this.name = name;
////            this.foodItems = foodItems;
////            this.phoneNumber = phoneNumber;
////            this.address = address;
////            this.imageUrl = imageUrl; // Save the image URL
////        }
////    }
////}
//
//package com.example.hungerlink;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class DonateActivity extends AppCompatActivity {
//
//    private EditText editTextName, editTextFoodItems, editTextPhoneNumber, editTextAddress;
//    private ImageView imagePreview;
//    private Uri selectedImageUri;
//    private DatabaseReference databaseReference;
//    private FirebaseUser currentUser;
//
//    // Define ActivityResultLauncher for image selection
//    private final ActivityResultLauncher<Intent> getImageLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                    selectedImageUri = result.getData().getData();
//                    // Load image using Glide
//                    Glide.with(this)
//                            .load(selectedImageUri)
//                            .override(400, 200) // Resize to desired dimensions
//                            .into(imagePreview); // Set to your ImageView
//                }
//            }
//    );
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_donate);
//
//        // Initialize views
//        initializeViews();
//
//        // Initialize Firebase Database and Authentication
//        initializeFirebase();
//
//        // Set up button listeners
//        setupButtonListeners();
//    }
//
//    private void initializeViews() {
//        editTextName = findViewById(R.id.editTextName_d);
//        editTextFoodItems = findViewById(R.id.editTextFoodItems);
//        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber_d);
//        editTextAddress = findViewById(R.id.editTextaddress);
//        imagePreview = findViewById(R.id.imagePreview);
//    }
//
//    private void initializeFirebase() {
//        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
//                .getReference("Donation Information");
//
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        currentUser = auth.getCurrentUser(); // Get the current user
//    }
//
//    private void setupButtonListeners() {
//        Button buttonChooseImage = findViewById(R.id.buttonUploadImage);
//        Button buttonDonate = findViewById(R.id.buttonDonate);
//
//        // Button to choose image
//        buttonChooseImage.setOnClickListener(v -> {
//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            getImageLauncher.launch(Intent.createChooser(intent, "Select Donation Image"));
//        });
//
//        // Button to donate
//        buttonDonate.setOnClickListener(v -> saveDonationInfo());
//    }
//
//    private void saveDonationInfo() {
//        String name = editTextName.getText().toString().trim();
//        String foodItems = editTextFoodItems.getText().toString().trim();
//        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
//        String address = editTextAddress.getText().toString().trim();
//
//        if (name.isEmpty() || foodItems.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
//            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Create a donation information map
//        Map<String, Object> donationInfo = new HashMap<>();
//        donationInfo.put("name", name);
//        donationInfo.put("foodItems", foodItems);
//        donationInfo.put("phoneNumber", phoneNumber);
//        donationInfo.put("address", address);
//        donationInfo.put("imageUri", selectedImageUri != null ? selectedImageUri.toString() : ""); // Optional image URI
//
//        // Save to Firebase under the current user's ID
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//            databaseReference.child(userId).push().setValue(donationInfo).addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    Toast.makeText(DonateActivity.this, "Donation information saved successfully", Toast.LENGTH_SHORT).show();
//                    clearFields(); // Clear all fields after successful submission
//                } else {
//                    Toast.makeText(DonateActivity.this, "Failed to save donation information", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void clearFields() {
//        editTextName.setText("");
//        editTextFoodItems.setText("");
//        editTextPhoneNumber.setText("");
//        editTextAddress.setText("");
//        imagePreview.setImageResource(0); // Clear the image preview
//        selectedImageUri = null; // Reset the image URI
//    }
//}


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
//    private EditText editTextName, editTextFoodItems, editTextPhoneNumber;
//    private AutoCompleteTextView editTextAddress;
//    private ImageView imagePreview;
//    private Uri imageUri;
//    private DatabaseReference databaseReference;
//    private StorageReference storageReference;
//    private String userId;
//
//    private ListView addressSuggestions;
//    private ArrayAdapter<String> suggestionsAdapter;
//    private List<String> addressList;
//    private PlacesClient placesClient;
//    private String selectedPlaceId; // Store the selected place ID for fetching details
//
//    // Declare the ActivityResultLauncher
//    private ActivityResultLauncher<Intent> imagePickerLauncher;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_donate);
//
//        // Initialize Views
//        editTextName = findViewById(R.id.editTextName_d);
//        editTextFoodItems = findViewById(R.id.editTextFoodItems);
//        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber_d);
//        editTextAddress = findViewById(R.id.editTextAddress);
//        imagePreview = findViewById(R.id.imagePreview);
//        Button buttonDonate = findViewById(R.id.buttonDonate);
//        Button buttonUploadImage = findViewById(R.id.buttonUploadImage);
//        addressSuggestions = findViewById(R.id.addressSuggestions);
//
//        // Firebase references
//        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
//        databaseReference = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app")
//                .getReference().child("Donation Information").child(userId);
//        storageReference = FirebaseStorage.getInstance().getReference("donation_images");
//
//        // Initialize Places SDK
//        Places.initialize(getApplicationContext(), "AIzaSyA6yE8ghQUdlgS74NTIGxNwuXRTY4nAL6k"); // Replace with your API key
//        placesClient = Places.createClient(this);
//
//        // Initialize ActivityResultLauncher for image picking
//        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null && result.getData().getData() != null) {
//                imageUri = result.getData().getData();
//                try {
//                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    imagePreview.setImageBitmap(bitmap);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        // Address suggestions
//        addressList = new ArrayList<>();
//        suggestionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, addressList);
//        addressSuggestions.setAdapter(suggestionsAdapter);
//        addressSuggestions.setVisibility(View.GONE);
//
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
//        addressSuggestions.setOnItemClickListener((adapterView, view, i, l) -> {
//            String selectedAddress = suggestionsAdapter.getItem(i);
//            editTextAddress.setText(selectedAddress);
//            addressSuggestions.setVisibility(View.GONE);
//            fetchPlaceDetails(selectedAddress); // Fetch place details (lat/lng) when an address is selected
//        });
//
//        buttonUploadImage.setOnClickListener(v -> openImageChooser());
//        buttonDonate.setOnClickListener(v -> submitDonation());
//    }
//
//    private void openImageChooser() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        imagePickerLauncher.launch(intent); // Use the launcher to start the intent
//    }
//
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
//                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
//                        addressList.add(prediction.getFullText(null).toString());
//                    }
//                    suggestionsAdapter.notifyDataSetChanged();
//                    addressSuggestions.setVisibility(View.VISIBLE);
//                }).addOnFailureListener(exception -> Log.e("PlacesAPI", "Error fetching address suggestions", exception));
//    }
//
//    private void fetchPlaceDetails(String placeId) {
//        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, List.of(Place.Field.LAT_LNG));
//        placesClient.fetchPlace(request)
//                .addOnSuccessListener(response -> {
//                    selectedPlaceId = placeId; // Store selected place ID
//                })
//                .addOnFailureListener(exception -> Log.e("PlacesAPI", "Error fetching place details", exception));
//    }
//
//    private void submitDonation() {
//        final String name = editTextName.getText().toString();
//        final String foodItems = editTextFoodItems.getText().toString();
//        final String phoneNumber = editTextPhoneNumber.getText().toString();
//        final String address = editTextAddress.getText().toString();
//
//        if (name.isEmpty() || foodItems.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || imageUri == null) {
//            Toast.makeText(this, "Please fill all fields and upload an image", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Fetch latitude and longitude
//        if (selectedPlaceId != null) {
//            FetchPlaceRequest request = FetchPlaceRequest.newInstance(selectedPlaceId, List.of(Place.Field.LAT_LNG));
//            placesClient.fetchPlace(request)
//                    .addOnSuccessListener(response -> {
//                        Place place = response.getPlace();
//                        LatLng latLng = place.getLatLng();
//
//                        if (latLng != null) {
//                            saveDonationData(name, foodItems, phoneNumber, address, latLng.latitude, latLng.longitude);
//                        }
//                    })
//                    .addOnFailureListener(e -> Toast.makeText(DonateActivity.this, "Failed to fetch location data", Toast.LENGTH_SHORT).show());
//        } else {
//            Toast.makeText(this, "Please select an address", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void saveDonationData(String name, String foodItems, String phoneNumber, String address, double latitude, double longitude) {
//        final StorageReference fileReference = storageReference.child(userId + "/" + System.currentTimeMillis() + ".jpg");
//        fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
//                fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
//                    Map<String, Object> donationData = new HashMap<>();
//                    donationData.put("name", name);
//                    donationData.put("foodItems", foodItems);
//                    donationData.put("phoneNumber", phoneNumber);
//                    donationData.put("address", address);
//                    donationData.put("imageUrl", uri.toString());
//                    donationData.put("latitude", latitude);
//                    donationData.put("longitude", longitude);
//
//                    databaseReference.push().setValue(donationData).addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(DonateActivity.this, "Donation submitted successfully", Toast.LENGTH_SHORT).show();
//                            clearFields();
//                        } else {
//                            Toast.makeText(DonateActivity.this, "Failed to submit donation", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                })).addOnFailureListener(e -> Toast.makeText(DonateActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show());
//    }
//
//    private void clearFields() {
//        editTextName.setText("");
//        editTextFoodItems.setText("");
//        editTextPhoneNumber.setText("");
//        editTextAddress.setText("");
//        imagePreview.setImageResource(R.drawable.baseline_file_upload_24);  // Placeholder image
//        imageUri = null;
//        selectedPlaceId = null; // Clear selected place ID
//    }
//}




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
        Places.initialize(getApplicationContext(), "AIzaSyA6yE8ghQUdlgS74NTIGxNwuXRTY4nAL6k"); // Replace with your API key
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
            fetchPlaceDetails(selectedPlaceId); // Fetch place details (lat/lng) when an address is selected
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
                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                        addressList.add(prediction.getFullText(null).toString());
                    }
                    suggestionsAdapter.notifyDataSetChanged();
                    if (!addressList.isEmpty()) {
                        addressSuggestions.setVisibility(View.VISIBLE);
                    } else {
                        addressSuggestions.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(exception -> Log.e("PlacesAPI", "Error fetching address suggestions", exception));
    }

    private void fetchPlaceDetails(String placeId) {
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, List.of(Place.Field.LAT_LNG));
        placesClient.fetchPlace(request)
                .addOnSuccessListener(response -> {
                    selectedPlaceId = placeId; // Store selected place ID
                })
                .addOnFailureListener(exception -> Log.e("PlacesAPI", "Error fetching place details", exception));
    }

    private void submitDonation() {
        final String name = editTextName.getText().toString();
        final String foodItems = editTextFoodItems.getText().toString();
        final String phoneNumber = editTextPhoneNumber.getText().toString();
        final String address = editTextAddress.getText().toString();

        if (name.isEmpty() || foodItems.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch latitude and longitude
        if (selectedPlaceId != null) {
            FetchPlaceRequest request = FetchPlaceRequest.newInstance(selectedPlaceId, List.of(Place.Field.LAT_LNG));
            placesClient.fetchPlace(request)
                    .addOnSuccessListener(response -> {
                        Place place = response.getPlace();
                        LatLng latLng = place.getLatLng();

                        if (latLng != null) {
                            saveDonationData(name, foodItems, phoneNumber, address, latLng.latitude, latLng.longitude);
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(DonateActivity.this, "Failed to fetch location data", Toast.LENGTH_SHORT).show());
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
        imagePreview.setImageResource(R.drawable.baseline_file_upload_24);  // Placeholder image
        imageUri = null;
        selectedPlaceId = null; // Clear selected place ID
    }
}
