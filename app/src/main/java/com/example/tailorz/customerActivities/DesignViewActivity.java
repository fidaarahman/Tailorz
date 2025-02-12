package com.example.tailorz.customerActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tailorz.helpers.Prefs;
import com.example.tailorz.R;

public class DesignViewActivity extends AppCompatActivity {

    ImageView customer_profile,back_arrow, designImage;
    Prefs prefs;
    String designUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_view);

        designImage = findViewById(R.id.designImage);
        customer_profile = findViewById(R.id.customer_profile);
        back_arrow = findViewById(R.id.back_arrow);
        prefs = new Prefs(getApplicationContext());
        designUrl = getIntent().getStringExtra("DesignViewUrl");

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        customer_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DesignViewActivity.this, Customer_profile.class));
            }
        });

        Glide.with(getApplicationContext())
                .load(prefs.getUserProfileImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(customer_profile);

        Glide.with(getApplicationContext())
                .load(designUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(designImage);

    }
}