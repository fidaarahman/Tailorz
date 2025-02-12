package com.example.tailorz.customerActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.apachat.loadingbutton.core.customViews.CircularProgressButton;
import com.bumptech.glide.Glide;
import com.example.tailorz.R;
import com.example.tailorz.databinding.ActivityCustomerProfileBinding;
import com.example.tailorz.helpers.Prefs;
import com.example.tailorz.register.Login;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Customer_profile extends AppCompatActivity {

    private ActivityCustomerProfileBinding binding;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private Prefs prefs;
    private Uri selectedImageUri; // Store selected image URI temporarily
    private ActivityResultLauncher<String> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefs = new Prefs(getApplicationContext());
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        setupUI();
        setupListeners();
    }

    private void setupUI() {
        binding.UserNametxt.setText(prefs.getUserName());
        binding.emailtxt.setText(prefs.getUserEmail());
        binding.telephonetxt.setText(prefs.getUserTelephone());
        binding.whatsapptxt.setText(prefs.getUserWhatsapp());
        binding.addresstxt.setText(prefs.getUserAddress());
        binding.gendertxt.setText(prefs.getUserGender());

        binding.etChestMeasurement.setText(prefs.getUserChest());
        binding.etWaistMeasurement.setText(prefs.getUserWaist());
        binding.etLegsMeasurement.setText(prefs.getUserLegs());
        binding.etArmMeasurement.setText(prefs.getUserArms());
        binding.etFullMeasurement.setText(prefs.getUserFull());

        // Load profile image
        Glide.with(getApplicationContext())
                .load(prefs.getUserProfileImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(binding.customerProfileImage);
    }

    private void setupListeners() {
        binding.backBtn.setOnClickListener(v -> finish());

        binding.logoutBtn.setOnClickListener(v -> {
            prefs.setLoginStatus(false);
            startActivity(new Intent(Customer_profile.this, Login.class));
            finish();
        });

        // Select image and display it but do not upload yet
        binding.editProfileBtn.setOnClickListener(v -> launcher.launch("image/*"));

        // Save profile button now uploads image if a new one is selected
        binding.saveProfileInfoBtn.setOnClickListener(v -> {
            if (selectedImageUri != null) {
                uploadImage(selectedImageUri); // Upload only if a new image is selected
            } else {
                saveProfileInfo(); // Just save profile info if no new image is selected
            }
        });

        binding.saveContactBtn.setOnClickListener(v -> saveContactInfo());

        // Initialize image picker
        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                selectedImageUri = uri; // Store the selected URI
                binding.customerProfileImage.setImageURI(uri); // Show selected image immediately
            }
        });
    }

    private void uploadImage(Uri uri) {
        if (uri == null) {
            Toast.makeText(getApplicationContext(), "Please select a profile image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show ProgressBar and disable button to prevent multiple clicks
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.saveProfileInfoBtn.setEnabled(false);

        String encodedEmail = encodeEmail(prefs.getUserEmail());

        StorageReference storageReference = storage.getReference()
                .child("CustomerProfileImages").child(encodedEmail);

        storageReference.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                        .addOnSuccessListener(downloadUri -> {
                            Map<String, Object> map = new HashMap<>();
                            map.put("profile_image_url", downloadUri.toString());

                            database.getReference().child("Users").child(encodedEmail)
                                    .updateChildren(map)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(getApplicationContext(), "Profile Image Updated Successfully", Toast.LENGTH_SHORT).show();
                                        prefs.setUserProfileImage(downloadUri.toString());

                                        // Load the new image into ImageView
                                        Glide.with(getApplicationContext())
                                                .load(downloadUri.toString())
                                                .placeholder(R.drawable.logo)
                                                .error(R.drawable.imagenotfound)
                                                .into(binding.customerProfileImage);

                                        saveProfileInfo(); // Save profile info after uploading image
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getApplicationContext(), "Profile Image Cannot be Updated!", Toast.LENGTH_SHORT).show();
                                        binding.progressBar.setVisibility(View.GONE); // Hide progress bar on failure
                                        binding.saveProfileInfoBtn.setEnabled(true); // Re-enable button
                                    });
                        }))
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Cannot Upload Image", Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                    binding.saveProfileInfoBtn.setEnabled(true);
                });
    }


    private void saveProfileInfo() {
        // Show progress bar and disable button
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.saveProfileInfoBtn.setEnabled(false);
        binding.saveProfileInfoBtn.startAnimation();

        new Handler().postDelayed(() -> {
            DatabaseReference databaseReference = database.getReference().child("Users").child(encodeEmail(prefs.getUserEmail()));

            Map<String, Object> updates = new HashMap<>();
            updates.put("gender", binding.gendertxt.getText().toString());
            updates.put("email", binding.emailtxt.getText().toString());

            databaseReference.updateChildren(updates)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(Customer_profile.this, "Profile Information Updated Successfully", Toast.LENGTH_SHORT).show();
                        updateButtonAnimation(binding.saveProfileInfoBtn);
                        binding.progressBar.setVisibility(View.GONE); // Hide progress bar
                        binding.saveProfileInfoBtn.setEnabled(true); // Re-enable button
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Customer_profile.this, "Profile Update Failed", Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE); // Hide progress bar on failure
                        binding.saveProfileInfoBtn.setEnabled(true); // Re-enable button
                    });
        }, 300);
    }


    private void saveContactInfo() {
        binding.saveContactBtn.startAnimation();

        new Handler().postDelayed(() -> {
            DatabaseReference databaseReference = database.getReference().child("Users").child(encodeEmail(prefs.getUserEmail()));

            Map<String, Object> updates = new HashMap<>();
            updates.put("telephoneNumber", binding.telephonetxt.getText().toString());
            updates.put("address", binding.addresstxt.getText().toString());
            updates.put("whatsappNumber", binding.whatsapptxt.getText().toString());

            databaseReference.updateChildren(updates)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(Customer_profile.this, "Contact Information Updated Successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(Customer_profile.this, "Contact Information Update Failed", Toast.LENGTH_SHORT).show());

            updateButtonAnimation(binding.saveContactBtn);
        }, 300);
    }

    private void updateButtonAnimation(CircularProgressButton button) {
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable drawable = getResources().getDrawable(R.drawable.checkbutton);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        button.doneLoadingAnimation(R.color.white, bitmap);
        recreate();
    }

    private String encodeEmail(String email) {
        return email.replace(".", ","); // Firebase does not allow dots in keys
    }
}
