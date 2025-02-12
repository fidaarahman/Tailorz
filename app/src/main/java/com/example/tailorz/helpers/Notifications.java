//package com.example.tailorz.helpers;
//
//import android.content.Context;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.example.tailorz.customerModels.NotificationModel;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//public class Notifications {
//    private static final String TAG = "Notifications";
//
//    private final FirebaseDatabase database;
//    private final Prefs prefs; // Prefs instance
//
//    // Constructor to initialize Prefs with Context
//    public Notifications(Context context) {
//        this.database = FirebaseDatabase.getInstance();
//        this.prefs = new Prefs(context); // ✅ Initialize Prefs properly
//    }
//
//    public void sendNotificationsToTailor(String emailKey, String orderID, String designName, String customerName, String tailorName, Context context) {
//        // Validate input
//        if (emailKey == null || emailKey.isEmpty()) {
//            Log.e(TAG, "Email key is null or empty. Notification not sent.");
//            Toast.makeText(context, "Invalid tailor email. Notification not sent.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (orderID == null || orderID.isEmpty() || designName == null || designName.isEmpty() ||
//                customerName == null || customerName.isEmpty() || tailorName == null || tailorName.isEmpty()) {
//            Log.e(TAG, "Notification data is incomplete. Missing required fields.");
//            Toast.makeText(context, "Incomplete notification data. Notification not sent.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Get logged-in user ID (encoded email)
//        String customerEmailKey = prefs.getUserID();
//        if (customerEmailKey == null || customerEmailKey.isEmpty()) {
//            Log.e(TAG, "User ID not found. Notification not sent.");
//            Toast.makeText(context, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Prepare notification data
//        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
//
//        NotificationModel notificationModel = new NotificationModel(
//                orderID,
//                designName,
//                customerName,
//                tailorName,
//                1, // Default status is "New"
//                currentDate,
//                currentTime
//        );
//
//        // Sanitize email key for Firebase (Replace '.' with ',')
//        String sanitizedEmailKey = emailKey.replace(".", ",");
//
//        // Send notification to the tailor's Firebase node
//        database.getReference().child("Notifications").child(sanitizedEmailKey).push()
//                .setValue(notificationModel)
//                .addOnSuccessListener(unused -> {
//                    Toast.makeText(context, "Notification Sent Successfully", Toast.LENGTH_SHORT).show();
//                    Log.d(TAG, "Notification sent to " + sanitizedEmailKey);
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(context, "Failed to Send Notification", Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, "Failed to send notification to " + sanitizedEmailKey, e);
//                });
//    }
//}
