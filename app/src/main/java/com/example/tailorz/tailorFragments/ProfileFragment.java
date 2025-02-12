package com.example.tailorz.tailorFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.tailorz.R;
import com.example.tailorz.databinding.FragmentProfileBinding;
import com.example.tailorz.helpers.Prefs;
import com.example.tailorz.register.Login;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private Prefs prefs;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private ActivityResultLauncher<String> imagePickerLauncher;
    private Uri selectedImageUri;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize View Binding
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase & Preferences
        prefs = new Prefs(requireActivity().getApplicationContext());
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        // Set up UI with user data
        setupUserInfo();

        // Set up image picker launcher
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null && binding != null) { // Added null check for binding
                selectedImageUri = uri;

                // Load selected image
                Glide.with(requireActivity().getApplicationContext())
                        .load(uri)
                        .centerCrop()
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.imagenotfound)
                        .into(binding.ivUserProfile);

                // Show save button
                binding.btSave.setVisibility(View.VISIBLE);

                // Hide warning message
                binding.tvUploadWarning.setVisibility(View.GONE);
                binding.ivUploadWarning.setVisibility(View.GONE);
            }
        });

        // Click Listeners
        if (binding != null) {
            binding.cardUserImage.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));
            binding.btSave.setOnClickListener(v -> saveImageToFirebase());
            binding.ivLogout.setOnClickListener(v -> logoutUser());
        }
    }

    private void setupUserInfo() {
        if (binding == null) return;

        // Bind user data
        binding.tvPhone.setText(prefs.getUserTelephone());
        binding.tvWhatsapp.setText(prefs.getUserWhatsapp());
        binding.tvAddress.setText(prefs.getUserAddress());
        binding.tvUsername.setText(prefs.getUserName());

        // ✅ Load Profile Image from Database
        database.getReference()
                .child("Users")
                .child(prefs.getUserName()) // Tailor username as key
                .child("profile_image_url")
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        String imageUrl = snapshot.getValue(String.class);
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(requireContext())
                                    .load(imageUrl)
                                    .centerCrop()
                                    .placeholder(R.drawable.logo)
                                    .error(R.drawable.imagenotfound)
                                    .into(binding.ivUserProfile);
                        } else {
                            // Load default image if no profile picture exists
                            binding.warningLayout.setVisibility(View.VISIBLE);
                            Glide.with(requireContext())
                                    .load(R.drawable.dummy)
                                    .centerCrop()
                                    .into(binding.ivUserProfile);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    binding.warningLayout.setVisibility(View.VISIBLE);
                    Glide.with(requireContext())
                            .load(R.drawable.dummy)
                            .centerCrop()
                            .into(binding.ivUserProfile);
                });
    }


    private void saveImageToFirebase() {
        if (selectedImageUri == null || binding == null) {
            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Get the tailor's correct database key (email stored as key)
        String tailorKey = encodeEmail(prefs.getUserEmail()); // Convert email to Firebase-safe format

        // ✅ Storage Path: TailorProfileImages/{tailorEmail}/profile.jpg
        StorageReference storageReference = storage.getReference()
                .child("TailorProfileImages")
                .child(tailorKey)
                .child("profile.jpg");

        // Upload image
        storageReference.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Retrieve image URL after successful upload
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        if (binding != null) {
                            // ✅ Update profile_image_url in the correct tailor's database record
                            Map<String, Object> updateMap = new HashMap<>();
                            updateMap.put("profile_image_url", uri.toString()); // Save in the correct column

                            database.getReference()
                                    .child("Users") // Go to Users node
                                    .child(tailorKey) // Save in tailor's correct record
                                    .updateChildren(updateMap)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

                                        // ✅ Refresh profile with updated image
                                        setupUserInfo();
                                        binding.btSave.setVisibility(View.GONE);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getContext(), "Failed to Update Profile Image URL", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }).addOnFailureListener(e ->
                            Toast.makeText(getContext(), "Failed to Retrieve Image URL", Toast.LENGTH_SHORT).show()
                    );
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Failed to Upload Profile Image", Toast.LENGTH_SHORT).show()
                );
    }


    private String encodeEmail(String email) {
        return email.replace(".", ",");
    }

    private void logoutUser() {
        if (binding == null) return;

        prefs.setLoginStatus(false);
        startActivity(new Intent(getContext(), Login.class));
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
}
