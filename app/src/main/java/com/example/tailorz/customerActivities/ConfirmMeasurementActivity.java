package com.example.tailorz.customerActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apachat.loadingbutton.core.customViews.CircularProgressButton;
import com.bumptech.glide.Glide;
import com.example.tailorz.helpers.Prefs;
import com.example.tailorz.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfirmMeasurementActivity extends AppCompatActivity {

    ImageView backbtn;
    CircleImageView profileImage;
    CircularProgressButton confirmMeasurement;
    Button retakeBtn;
    TextView chest_text, arm_text, leg_text,goBackTxt, full_text, waist_text;
    Prefs prefs;
    String arms_str, legs_str, waist_str, chest_str, full_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_measurement);

        prefs = new Prefs(getApplicationContext());
        SetBackArrowPressed();
        SetUserProfile();

        chest_text = findViewById(R.id.legs_Confirm);
        arm_text = findViewById(R.id.height_Confirm);
        leg_text = findViewById(R.id.calf_Confirm);
        full_text = findViewById(R.id.Arms_Confirm);
        waist_text = findViewById(R.id.thigh_Confirm);


        confirmMeasurement = findViewById(R.id.confirm_Measurement);
        retakeBtn = findViewById(R.id.retake_Btn);
        goBackTxt = findViewById(R.id.goBackTxt);

        arms_str = getIntent().getStringExtra("Arms");
        legs_str = getIntent().getStringExtra("Legs");
        chest_str = getIntent().getStringExtra("Chest");
        waist_str = getIntent().getStringExtra("Waist");
        full_str = getIntent().getStringExtra("Full");


        chest_text.setText("Chest : " + chest_str + " Meters");
        arm_text.setText("Arms length : " + arms_str + " Meters");
        leg_text.setText("Legs length : " + legs_str + " Meters");
        full_text.setText("Shoulder to knee : " + full_str + " Meters");
        waist_text.setText("Waist : " + waist_str + " Meters");


        retakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  startActivity(new Intent(ConfirmMeasurementActivity.this, TakeUserMeasurement.class));
            }
        });

        confirmMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmMeasurement.startAnimation();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prefs.setUserWaist(waist_str + " Meters");
                        prefs.setUserArms(arms_str + " Meters");
                        prefs.setUserLegs(legs_str + " Meters");
                        prefs.setUserChest(chest_str + " Meters");
                        prefs.setUserFull(full_str + " Meters");

                       // Log.d("Confirm Measurement : : ", prefs.getUserCalfLength() + prefs.getUserHeight() +  prefs.getUserThighLength());
                        Drawable drawable = getResources().getDrawable(R.drawable.checkbutton);
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        confirmMeasurement.doneLoadingAnimation(R.color.white, bitmap);
                        goBackTxt.setVisibility(View.VISIBLE);
                    }
                },300);

            }
        });


    }

    public void SetBackArrowPressed() {
        //Setting Back Btn
        backbtn = findViewById(R.id.backBtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void SetUserProfile() {
        //Setting Up Customer Profile
        profileImage = findViewById(R.id.customer_profile);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmMeasurementActivity.this, Customer_profile.class));
            }
        });
        Glide.with(getApplicationContext())
                .load(prefs.getUserProfileImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(profileImage);
        //User Profile Done
    }
}