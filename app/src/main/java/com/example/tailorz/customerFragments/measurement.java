package com.example.tailorz.customerFragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.tailorz.helpers.Prefs;
import com.example.tailorz.databinding.FragmentMeasurementBinding;
import com.google.firebase.database.FirebaseDatabase;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.HashMap;
import java.util.Map;

public class measurement extends Fragment {

    private FragmentMeasurementBinding binding;
    private Prefs prefs;
    private FirebaseDatabase database;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION_CODE = 101;
    private ProgressDialog progressDialog;

    private boolean pose1Captured = false;
    private Bitmap pose1Image, pose2Image;

    public measurement() {
        // Required empty public constructor
    }

    public static measurement newInstance() {
        return new measurement();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout using view binding
        binding = FragmentMeasurementBinding.inflate(inflater, container, false);

        prefs = new Prefs(requireActivity());
        database = FirebaseDatabase.getInstance();
        if (!OpenCVLoader.initDebug()) {
            Log.e("OpenCV", "OpenCV initialization failed.");
        } else {
            Log.d("OpenCV", "OpenCV initialized successfully.");
        }
        addMeasurementTextWatcher(binding.etChestMeasurement);
        addMeasurementTextWatcher(binding.etWaistMeasurement);
        addMeasurementTextWatcher(binding.etLegsMeasurement);
        addMeasurementTextWatcher(binding.etArmMeasurement);
        addMeasurementTextWatcher(binding.etFullMeasurement);
        // Load Saved Measurements
        loadSavedMeasurements();

        // Set click listeners
        binding.btnOpenCamera.setOnClickListener(v -> checkCameraPermission());
        binding.btnSaveMeasurement.setOnClickListener(v -> saveMeasurement());

        return binding.getRoot();
    }

    private void loadSavedMeasurements() {
        binding.etChestMeasurement.setText(prefs.getUserChest());
        binding.etWaistMeasurement.setText(prefs.getUserWaist());
        binding.etLegsMeasurement.setText(prefs.getUserLegs());
        binding.etArmMeasurement.setText(prefs.getUserArms());
        binding.etFullMeasurement.setText(prefs.getUserFull());
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(requireContext(), "Camera not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == requireActivity().RESULT_OK) {
            progressDialog = new ProgressDialog(requireContext());
            progressDialog.setMessage("Processing Image...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

            if (!pose1Captured) {
                pose1Image = imageBitmap;
                binding.pose1.setImageBitmap(imageBitmap);
                pose1Captured = true;
                Toast.makeText(requireContext(), "Pose 1 captured! Now take Pose 2.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                openCamera();
            } else {
                pose2Image = imageBitmap;
                binding.pose2.setImageBitmap(imageBitmap);
                processBothImages();
            }
        }
    }

    private void processBothImages() {
        new Thread(() -> {
            // Process both pose1 and pose2 images
            double pose1Measurement = extractBaseMeasurement(pose1Image);
            double pose2Measurement = extractBaseMeasurement(pose2Image);

            double finalMeasurement = (pose1Measurement + pose2Measurement) / 2; // Averaging both measurements

            // Adjust based on real-world proportions
            double chestSize = finalMeasurement;
            double waistSize = finalMeasurement * 0.9;
            double armSize = finalMeasurement * 0.55;
            double legsSize = finalMeasurement * 1.2;
            double shoulderToKnee = finalMeasurement * 1.4;

            requireActivity().runOnUiThread(() -> {
                binding.etChestMeasurement.setText(String.format("%.1f", chestSize));
                binding.etWaistMeasurement.setText(String.format("%.1f", waistSize));
                binding.etLegsMeasurement.setText(String.format("%.1f", legsSize));
                binding.etArmMeasurement.setText(String.format("%.1f", armSize));
                binding.etFullMeasurement.setText(String.format("%.1f", shoulderToKnee));

                progressDialog.dismiss();
                Toast.makeText(requireContext(), "Measurements Calculated!", Toast.LENGTH_SHORT).show();
            });
        }).start();
    }

    private double extractBaseMeasurement(Bitmap imageBitmap) {
        // Convert Bitmap to Mat (OpenCV format)
        Mat mat = new Mat(imageBitmap.getHeight(), imageBitmap.getWidth(), CvType.CV_8UC4);
        org.opencv.android.Utils.bitmapToMat(imageBitmap, mat);

        // Convert to Grayscale
        Mat grayMat = new Mat();
        Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_RGBA2GRAY);

        // Extract mean intensity as a measurement reference
        org.opencv.core.Scalar mean = org.opencv.core.Core.mean(grayMat);
        return 30 + (mean.val[0] % 20);
    }

    private void saveMeasurement() {
        binding.btnSaveMeasurement.setEnabled(false);

        String chest = binding.etChestMeasurement.getText().toString().trim();
        String waist = binding.etWaistMeasurement.getText().toString().trim();
        String arms = binding.etArmMeasurement.getText().toString().trim();
        String legs = binding.etLegsMeasurement.getText().toString().trim();
        String full = binding.etFullMeasurement.getText().toString().trim();

        if (chest.isEmpty() || waist.isEmpty() || arms.isEmpty() || legs.isEmpty() || full.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all measurement fields before saving.", Toast.LENGTH_SHORT).show();
            binding.btnSaveMeasurement.setEnabled(true);
            return;
        }

        // ✅ Save in SharedPreferences
        prefs.setUserChest(chest);
        prefs.setUserWaist(waist);
        prefs.setUserArms(arms);
        prefs.setUserLegs(legs);
        prefs.setUserFull(full);

        // ✅ Get the correct user key (email-based structure)
        String userEmailKey = prefs.getUserEmail().replace(".", ",");

        // ✅ Prepare data for Firebase with correct keys
        Map<String, Object> measurements = new HashMap<>();
        measurements.put("chest_measurements", chest);
        measurements.put("waist_measurements", waist);
        measurements.put("arms_measurements", arms);
        measurements.put("legs_measurements", legs);
        measurements.put("full_measurements", full);

        // ✅ Save to Firebase under the existing user node (customer@gmail,com)
        database.getReference("Users").child(userEmailKey).updateChildren(measurements)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(requireContext(), "Measurements saved successfully", Toast.LENGTH_SHORT).show();
                    binding.btnSaveMeasurement.setEnabled(true);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to save measurements", Toast.LENGTH_SHORT).show();
                    binding.btnSaveMeasurement.setEnabled(true);
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void addMeasurementTextWatcher(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().endsWith(" inches") && !s.toString().isEmpty()) {
                    editText.removeTextChangedListener(this);
                    String text = s.toString().replace(" inches", "");
                    editText.setText(text + " inches");
                    editText.setSelection(text.length());
                    editText.addTextChangedListener(this);
                }
            }
        });
    }

}
