package com.example.tailorz.customerActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.apachat.loadingbutton.core.customViews.CircularProgressButton;
import com.bumptech.glide.Glide;
import com.example.tailorz.customerModels.ReviewModel;
import com.example.tailorz.helpers.Prefs;
import com.example.tailorz.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

public class ReviewsActivity extends AppCompatActivity {

    RatingBar ratingBar;
    EditText review;
    ImageView back_arrow, customer_profile;
    CircularProgressButton submitReview;
    Prefs prefs;
    ReviewModel reviewModel;
    float rateValue;
    String tailorName_txt,designId,designName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        prefs = new Prefs(getApplicationContext());
        submitReview = findViewById(R.id.submitReview);
        ratingBar = findViewById(R.id.ratingBar);
        review = findViewById(R.id.editText);
        reviewModel = new ReviewModel();
        SetBackArrowPressed();
        SetUserProfile();

        tailorName_txt = getIntent().getStringExtra("tailorName");
        designId = getIntent().getStringExtra("designID");
        designName = getIntent().getStringExtra("designName");

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rateValue = ratingBar.getRating();
            }
        });

        SubmitReview();
    }

    public void SubmitReview(){
        submitReview.startAnimation();

        submitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review_txt = review.getText().toString();
                reviewModel.setReview(review_txt);
                reviewModel.setRateValue(rateValue);
                reviewModel.setCustomerName(prefs.getUserName());
                reviewModel.setTailorName(tailorName_txt);
                reviewModel.setDesignName(designName);
                reviewModel.setDesignID(designId);

                FirebaseDatabase.getInstance().getReference().child("Reviews")
                        .push().setValue(reviewModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ReviewsActivity.this, "Your Review Has Been Added Successfully", Toast.LENGTH_SHORT).show();
                                Drawable drawable = getResources().getDrawable(R.drawable.checkbutton);
                                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                submitReview.doneLoadingAnimation(R.color.white, bitmap);
                                recreate();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ReviewsActivity.this, "Your Review Cannot Be Added. Please Try Again", Toast.LENGTH_SHORT).show();
                                Log.d("Review Error::", e.toString());
                                Drawable drawable = getResources().getDrawable(R.drawable.cancelbutton);
                                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                submitReview.doneLoadingAnimation(R.color.white, bitmap);
                                recreate();
                            }
                        });

            }
        });
    }

    public void SetBackArrowPressed() {
        //Setting Back Btn
        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void SetUserProfile() {
        //Setting Up Customer Profile
        customer_profile = findViewById(R.id.customer_profile);
        customer_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReviewsActivity.this, Customer_profile.class));
            }
        });
        Glide.with(getApplicationContext())
                .load(prefs.getUserProfileImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(customer_profile);
        //User Profile Done
    }
}