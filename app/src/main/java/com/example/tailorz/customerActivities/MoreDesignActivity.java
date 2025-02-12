package com.example.tailorz.customerActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tailorz.helpers.Prefs;
import com.example.tailorz.R;
import com.google.firebase.database.DatabaseReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class MoreDesignActivity extends AppCompatActivity {

    String tailorName_txt, tailorCategory_txt, designUrl, tailorProfileImageUrl, designName, designId;
    ImageView moreDesign_image, moreTailorRating;
    CircleImageView TailorProfileImage;
    TextView moretailorName, moreTailorCategory, moreDesignName,moreDesignTailorName;
    Button hireMe;
    Prefs prefs;
    DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_design);

        moreDesign_image = findViewById(R.id.moreDesignImage);
        TailorProfileImage = findViewById(R.id.TailorProfileImage);
        moretailorName = findViewById(R.id.tailorName);
        moreDesignTailorName = findViewById(R.id.moreDesignTailorName);
        moreTailorCategory = findViewById(R.id.tailorCategory);
        moreDesignName = findViewById(R.id.moreDesignName);
        hireMe = findViewById(R.id.hireMe_Btn);
        prefs = new Prefs(getApplicationContext());

        tailorName_txt = getIntent().getStringExtra("moreTailorName");
        designUrl = getIntent().getStringExtra("moreDesignUrl");
        designId = getIntent().getStringExtra("moreDesignId");
        designName = getIntent().getStringExtra("moreDesignName");
        Log.d("MORE DESIGN Tailor Name :::: ", tailorName_txt);
        Log.d("MORE DESIGN ID :::: ", designId);

        hireMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), HireMeActivity.class);
                intent.putExtra("HireDesignUrl", designUrl);
                intent.putExtra("HireDesignName",designName);
                intent.putExtra("HireTailorName", tailorName_txt);
                intent.putExtra("HireDesignID", designId);
                Log.d("Tailor NAME ::::", tailorName_txt);
                startActivity(intent);

            }
        });


        Glide.with(this)
                .load(designUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(moreDesign_image);

        moretailorName.setText(tailorName_txt);
        moreDesignName.setText(designName);
        moreDesignTailorName.setText(tailorName_txt);

//        FirebaseDatabase.getInstance().getReference().child("Users").child(tailorName_txt).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                CustomerTailorModel customerTailorModel  = snapshot.getValue(CustomerTailorModel.class);
//                assert customerTailorModel != null;
//                moreTailorCategory.setText(customerTailorModel.getTailor_category());
//                Glide.with(getApplicationContext())
//                        .load(customerTailorModel.getProfile_image_url())
//                        .placeholder(R.drawable.logo)
//                        .error(R.drawable.imagenotfound)
//                        .into(TailorProfileImage);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });




    }//ON CREATE


}//END