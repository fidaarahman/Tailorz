package com.example.tailorz.customerActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tailorz.customerFragments.Customer_home;
import com.example.tailorz.customerFragments.Favorites_fragment;
import com.example.tailorz.customerFragments.Orders_fragment;
import com.example.tailorz.customerFragments.measurement;
import com.example.tailorz.helpers.Prefs;
import com.example.tailorz.R;
import com.example.tailorz.databinding.ActivityCustomerMainBinding;  // ViewBinding import
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerMain extends AppCompatActivity {

    private ActivityCustomerMainBinding binding;
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate layout using ViewBinding
        binding = ActivityCustomerMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefs = new Prefs(getApplicationContext());

      /*  // Set profile image using Glide
        Glide.with(getApplicationContext())
                .load(prefs.getUserProfileImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.imagenotfound)
                .into(binding.customerProfile);*/

        // Set up Notification button click listener
        binding.NotificationBtn.setOnClickListener(v -> {
            startActivity(new Intent(CustomerMain.this, CustomerChatActivity.class));
        });

        // Set up Profile button click listener
        binding.customerProfile.setOnClickListener(v -> {
            startActivity(new Intent(CustomerMain.this, Customer_profile.class));
        });

        // Bottom Navigation setup
        BottomNavigationView bottomNavigationView = binding.CustomerBottomNavigation;
        replaceFragment(new Customer_home());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.Customer_home:
                    replaceFragment(new Customer_home());
                    break;
                case R.id.measurement:
                    replaceFragment(new measurement());
                    break;
                case R.id.favorites:
                    replaceFragment(new Favorites_fragment());
                    break;
                case R.id.cart:
                    replaceFragment(new Orders_fragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Customer_frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
