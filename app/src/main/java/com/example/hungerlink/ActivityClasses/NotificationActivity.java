//package com.example.hungerlink;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//import androidx.core.content.ContextCompat;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.Manifest;
//import android.widget.Toast;
//
//public class NotificationActivity extends AppCompatActivity {
//
//    private final String CHANNEL_ID = "hungerlink_notifications";
//    private final int NOTIFICATION_ID = 001;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Create Notification Channel if necessary
//        createNotificationChannel();
//
//        // Check and request permission if needed
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                // Request the permission
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
//            } else {
//                // Permission already granted, proceed to show notification
//                displayNotification();
//            }
//        } else {
//            // No need to request permission for Android versions lower than 13
//            displayNotification();
//        }
//    }
//
//    private void displayNotification() {
//        // Check for intent extras to display the notification
//        Intent intent = getIntent();
//        if (intent != null) {
//            String title = intent.getStringExtra("title");
//            String message = intent.getStringExtra("message");
//            sendNotification(title, message);
//        }
//
//        // Close activity after showing the notification
//        finish();
//    }
//
//    // Create the notification channel for API 26+
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "HungerLink Notifications";
//            String description = "Channel for HungerLink notifications";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//
//            // Register the channel with the system
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            if (notificationManager != null) {
//                notificationManager.createNotificationChannel(channel);
//            }
//        }
//    }
//
//    // Method to send a notification
//    private void sendNotification(String title, String message) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.applogosmall) // Your notification icon
//                .setContentTitle(title)
//                .setContentText(message)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//        // Send the notification
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        notificationManager.notify(NOTIFICATION_ID, builder.build());
//    }
//
//    // Static method to launch NotificationActivity and display a notification
//    public static void showNotification(Context context, String title, String message) {
//        Intent intent = new Intent(context, NotificationActivity.class);
//        intent.putExtra("title", title);
//        intent.putExtra("message", message);
//        context.startActivity(intent);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission was granted, proceed with showing the notification
//                displayNotification();
//            } else {
//                // Permission was denied, notify the user appropriately
//                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//}



package com.example.hungerlink.ActivityClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.widget.Toast;

import com.example.hungerlink.R;

public class NotificationActivity extends AppCompatActivity {

    private final String CHANNEL_ID = "hungerlink_notifications"; // ID for the notification channel
    private final int NOTIFICATION_ID = 001; // Unique ID for the notification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create Notification Channel if necessary
        createNotificationChannel();

        // Check and request notification permission if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            } else {
                // Permission already granted, proceed to show notification
                displayNotification();
            }
        } else {
            // No need to request permission for Android versions lower than 13
            displayNotification();
        }
    }

    private void displayNotification() {
        // Check for intent extras to display the notification
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title"); // Get title from intent extras
            String message = intent.getStringExtra("message"); // Get message from intent extras
            if (title != null && message != null) {
                sendNotification(title, message); // Send the notification
            } else {
                Toast.makeText(this, "Notification title or message is missing", Toast.LENGTH_SHORT).show(); // Show error if title/message is null
            }
        } else {
            Toast.makeText(this, "Intent is null", Toast.LENGTH_SHORT).show(); // Show error if intent is null
        }

        // Close activity after showing the notification
        finish(); // End this activity
    }

    // Create the notification channel for API 26+
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "HungerLink Notifications"; // Name of the notification channel
            String description = "Channel for HungerLink notifications"; // Description of the channel
            int importance = NotificationManager.IMPORTANCE_DEFAULT; // Importance level
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance); // Create channel
            channel.setDescription(description); // Set channel description

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel); // Create the channel in the system
            } else {
                Toast.makeText(this, "Failed to create notification channel", Toast.LENGTH_SHORT).show(); // Show error if channel creation fails
            }
        }
    }

    // Method to send a notification
    private void sendNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.applogosmall) // Your notification icon
                .setContentTitle(title) // Set notification title
                .setContentText(message) // Set notification message
                .setPriority(NotificationCompat.PRIORITY_DEFAULT); // Set notification priority

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this); // Get NotificationManagerCompat

        // Send the notification
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Notification permission not granted", Toast.LENGTH_SHORT).show(); // Show error if permission not granted
            return; // Exit method if permission is not granted
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build()); // Send the notification
    }

    // Static method to launch NotificationActivity and display a notification
    public static void showNotification(Context context, String title, String message) {
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.putExtra("title", title); // Pass title to the activity
        intent.putExtra("message", message); // Pass message to the activity
        context.startActivity(intent); // Start the NotificationActivity
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, proceed with showing the notification
                displayNotification();
            } else {
                // Permission was denied, notify the user appropriately
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show(); // Show error message
            }
        }
    }
}
