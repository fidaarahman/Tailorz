///*
//package com.example.tailorz.customerActivities;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import com.camerakit.CameraKitView;
//import com.example.tailorz.R;
//import com.github.ybq.android.spinkit.style.FadingCircle;
//import com.google.mlkit.vision.common.InputImage;
//import com.google.mlkit.vision.pose.Pose;
//import com.google.mlkit.vision.pose.PoseDetection;
//import com.google.mlkit.vision.pose.PoseDetector;
//import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;
//
//import java.io.InputStream;
//
//public class TakeUserMeasurement extends AppCompatActivity implements View.OnClickListener {
//
//    LinearLayout cameraButton, galleryButton;
//    CameraKitView cameraViewPose;
//    ProgressBar progressBar;
//
//    private final int REQUEST_GALLERY = 1;
//
//    AccuratePoseDetectorOptions options = new AccuratePoseDetectorOptions.Builder()
//            .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
//            .build();
//    PoseDetector poseDetector;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_take_user_measurement);
//
//        // Initialize UI components
//        cameraButton = findViewById(R.id.cameraBtn);
//        galleryButton = findViewById(R.id.galleryBtn);
//        cameraViewPose = findViewById(R.id.poseCamera);
//        progressBar = findViewById(R.id.spin_kit);
//
//        // Initialize SpinKit ProgressBar
//        FadingCircle fadingCircle = new FadingCircle();
//        progressBar.setIndeterminateDrawable(fadingCircle);
//        progressBar.setVisibility(View.INVISIBLE);
//
//        // Initialize pose detector
//        poseDetector = PoseDetection.getClient(options);
//
//        // Set click listeners
//        cameraButton.setOnClickListener(this);
//        galleryButton.setOnClickListener(this);
//
//        // Add CameraKit event listener
////        cameraViewPose.addCameraKitListener(new CameraKitEventListener() {
////            @Override
////            public void onEvent(CameraKitEvent cameraKitEvent) {
////                Log.d("CameraKit", "Event: " + cameraKitEvent.getMessage());
////            }
////
////            @Override
////            public void onError(CameraKitEvent cameraKitEvent) {
////                Log.e("CameraKit", "Error: " + cameraKitEvent.getMessage());
////            }
////
////            @Override
////            public void onImage(CameraKitImage cameraKitImage) {
////                progressBar.setVisibility(View.VISIBLE);
////                Bitmap bitmap = cameraKitImage.getBitmap();
////                processImage(bitmap);
////            }
////
////            @Override
////            public void onVideo(CameraKitEvent cameraKitEvent) {
////                // Video handling not implemented
////            }
////        });
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.cameraBtn:
//                //captureImage();
//                break;
//            case R.id.galleryBtn:
//                openGallery();
//                break;
//        }
//    }
//
//    */
///*private void captureImage() {
//        cameraViewPose.start();
//        cameraViewPose.captureImage();
//    }*//*
//
//
//    private void openGallery() {
//        Intent mediaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(mediaIntent, REQUEST_GALLERY);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && data != null) {
//            try {
//                Uri imageUri = data.getData();
//                InputStream inputStream = getContentResolver().openInputStream(imageUri);
//                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                processImage(bitmap);
//            } catch (Exception e) {
//                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void processImage(Bitmap bitmap) {
//        try {
//            InputImage image = InputImage.fromBitmap(bitmap, 0);
//            poseDetector.process(image)
//                    .addOnSuccessListener(this::processPose)
//                    .addOnFailureListener(e -> {
//                        progressBar.setVisibility(View.INVISIBLE);
//                        Toast.makeText(TakeUserMeasurement.this, "Pose detection failed", Toast.LENGTH_SHORT).show();
//                    });
//        } catch (Exception e) {
//            progressBar.setVisibility(View.INVISIBLE);
//            Toast.makeText(this, "Error processing image", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void processPose(Pose pose) {
//        // Your existing pose processing logic
//        progressBar.setVisibility(View.INVISIBLE);
//        Log.d("PoseDetection", "Pose processed successfully");
//        Toast.makeText(this, "Pose detected successfully", Toast.LENGTH_SHORT).show();
//    }
//
//  */
///*  @Override
//    protected void onResume() {
//        super.onResume();
//        cameraViewPose.start();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        cameraViewPose.stop();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        cameraViewPose.stop();
//    }*//*
//
//}
//*/
