package com.example.tailorz.customerActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tailorz.customerModels.OrderModel;
import com.example.tailorz.helpers.Prefs;
import com.example.tailorz.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompleteOrdersActivity extends AppCompatActivity {

    String orderID;
    OrderModel orderModel;
    ImageView back_arrow, customer_profile;
    Button markAsCompleted;
    Prefs prefs;

    TextView designName_orders,tailorName_orders,DesignID_order,OrderID_order,
            DeliveryAddress_order,CustomerWhatsapp_order,CustomerTelephone_order,
            OrderDate_order,TailorAddress_order,TailorWhatsapp_order,
            TailorTelephone_order,CustomerGender_order,CompletionExpectedBy_order,
            CustomerWaistMeasurement_order,CustomerCollarMeasurement_order,CustomerChestMeasurement_order,
            CustomerShoulderMeasurement_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_orders);

        orderID = getIntent().getStringExtra("orderID");
        InitViews();
        GetDataFromDataBase();
        SetBackArrowPressed();
        SetUserProfile();

    }

    public void GetDataFromDataBase() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
             FirebaseDatabase.getInstance().getReference().child("Orders").child(orderID).addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     orderModel = snapshot.getValue(OrderModel.class);
                     assert orderModel != null;
                     designName_orders.setText(orderModel.getDesignName());
                     tailorName_orders.setText(orderModel.getTailorName());
                     DesignID_order.setText(orderModel.getDesignID());
                     OrderID_order.setText(orderModel.getOrderID());
                     DeliveryAddress_order.setText(orderModel.getCustomerAddress());
                     CustomerWhatsapp_order.setText(orderModel.getCustomerWhatsapp());
                     CustomerTelephone_order.setText(orderModel.getCustomerPhone());
                     OrderDate_order.setText(orderModel.getOrderDate());
                     TailorAddress_order.setText(orderModel.getTailorAddress());
                     TailorWhatsapp_order.setText(orderModel.getTailorWhatsapp());
                     TailorTelephone_order.setText(orderModel.getTailorPhone());
                     CustomerGender_order.setText(orderModel.getCustomerGender());
                     CompletionExpectedBy_order.setText(orderModel.getCompletionExpectedBy());
                     CustomerWaistMeasurement_order.setText(orderModel.getCustomerWaistMeasurement());
                     CustomerCollarMeasurement_order.setText(orderModel.getCustomerCollarMeasurement());
                     CustomerChestMeasurement_order.setText(orderModel.getCustomerChestMeasurement());
                     CustomerShoulderMeasurement_order.setText(orderModel.getCustomerShoulderMeasurement());

                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             });
            }
        },300);
    }
    public void InitViews(){
        prefs = new Prefs(getApplicationContext());
        customer_profile = findViewById(R.id.customer_profile);
        back_arrow = findViewById(R.id.back_arrow);
        markAsCompleted = findViewById(R.id.markAsCompleted);
        designName_orders = findViewById(R.id.designName_orders);
        tailorName_orders = findViewById(R.id.tailorName_orders);
        DesignID_order = findViewById(R.id.DesignID_order);
        OrderID_order = findViewById(R.id.OrderID_order);
        DeliveryAddress_order = findViewById(R.id.DeliveryAddress_order);
        CustomerWhatsapp_order = findViewById(R.id.CustomerWhatsapp_order);
        CustomerTelephone_order = findViewById(R.id.CustomerTelephone_order);
        OrderDate_order = findViewById(R.id.OrderDate_order);
        TailorAddress_order = findViewById(R.id.TailorAddress_order);
        TailorWhatsapp_order = findViewById(R.id.TailorWhatsapp_order);
        TailorTelephone_order = findViewById(R.id.TailorTelephone_order);
        CustomerGender_order = findViewById(R.id.CustomerGender_order);
        CompletionExpectedBy_order = findViewById(R.id.CompletionExpectedBy_order);
        CustomerWaistMeasurement_order = findViewById(R.id.CustomerWaistMeasurement_order);
        CustomerCollarMeasurement_order = findViewById(R.id.CustomerCollarMeasurement_order);
        CustomerChestMeasurement_order = findViewById(R.id.CustomerChestMeasurement_order);
        CustomerShoulderMeasurement_order = findViewById(R.id.CustomerShoulderMeasurement_order);

        markAsCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReviewsActivity.class);
                intent.putExtra("tailorName", orderModel.getTailorName());
                intent.putExtra("designName", orderModel.getDesignName());
                intent.putExtra("designID", orderModel.getDesignID());
                Log.d("Tailor NAME ::::", orderModel.getTailorName());
                startActivity(intent);
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
                startActivity(new Intent(CompleteOrdersActivity.this, Customer_profile.class));
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