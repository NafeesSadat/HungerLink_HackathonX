package com.example.hungerlink;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DonateActivity extends AppCompatActivity {

    private EditText editTextName, editTextFoodItems, editTextPhoneNumber, editTextAddress;
    private ImageView imagePreview;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri imageUri; // To store the selected image URI

    // Declare the ActivityResultLauncher
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate); // Change this to your actual layout file name

        // Initialize Firebase Database and Storage
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://hunger-link-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = database.getReference("Donation Information");
        storageReference = FirebaseStorage.getInstance().getReference("donation_images"); // Folder in Storage

        // Initialize UI components
        editTextName = findViewById(R.id.editTextName_d);
        editTextFoodItems = findViewById(R.id.editTextFoodItems);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber_d);
        editTextAddress = findViewById(R.id.editTextaddress);
        imagePreview = findViewById(R.id.imagePreview);

        // Initialize the ActivityResultLauncher for image selection
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                imageUri = result.getData().getData(); // Save the image URI
                imagePreview.setImageURI(imageUri); // Set the selected image to ImageView
            }
        });

        // Set click listener for the donate button
        findViewById(R.id.buttonDonate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donate();
            }
        });

        // Set up click listener for the image upload button
        findViewById(R.id.buttonUploadImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    private void donate() {
        // Get user input
        String name = editTextName.getText().toString().trim();
        String foodItems = editTextFoodItems.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();

        // Validate inputs
        if (name.isEmpty() || foodItems.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill in all fields and upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload the image to Firebase Storage
        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
        fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the download URL
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();

                                // Create a Donation object
                                Donation donation = new Donation(name, foodItems, phoneNumber, address, downloadUrl);

                                // Save the donation data to Firebase
                                databaseReference.push().setValue(donation)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(DonateActivity.this, "Donation Successful!", Toast.LENGTH_SHORT).show();
                                                clearInputs();
                                            } else {
                                                Toast.makeText(DonateActivity.this, "Donation Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(DonateActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Use the ActivityResultLauncher to start the image picker
        imagePickerLauncher.launch(Intent.createChooser(intent, "Select Donation Image"));
    }

    private void clearInputs() {
        editTextName.setText("");
        editTextFoodItems.setText("");
        editTextPhoneNumber.setText("");
        editTextAddress.setText("");
        imagePreview.setImageResource(R.drawable.baseline_file_upload_24); // Reset the image
        imageUri = null; // Clear the image URI
    }

    public static class Donation {
        public String name;
        public String foodItems;
        public String phoneNumber;
        public String address;
        public String imageUrl; // Added to store the image URL

        public Donation() {}

        public Donation(String name, String foodItems, String phoneNumber, String address, String imageUrl) {
            this.name = name;
            this.foodItems = foodItems;
            this.phoneNumber = phoneNumber;
            this.address = address;
            this.imageUrl = imageUrl; // Save the image URL
        }
    }
}
