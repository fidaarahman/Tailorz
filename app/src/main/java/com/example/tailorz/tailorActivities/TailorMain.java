package com.example.tailorz.tailorActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tailorz.R;
import com.example.tailorz.databinding.ActivityTailorMainBinding;
import com.example.tailorz.helpers.Prefs;
import com.example.tailorz.register.Login;
import com.example.tailorz.tailorFragments.ContactInfoFragment;
import com.example.tailorz.tailorFragments.DesignFragment;
import com.example.tailorz.tailorFragments.Homefragment;
import com.example.tailorz.tailorFragments.ProfileFragment;

public class TailorMain extends AppCompatActivity {

    private ActivityTailorMainBinding binding; // View Binding
    private Prefs prefs; // Shared Preferences for Logout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTailorMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefs = new Prefs(this); // Initialize Preferences

        replaceFragment(new Homefragment());

        // **Menu Button Click**
        binding.menu.setOnClickListener(v ->
                startActivity(new Intent(TailorMain.this, TailorChatListActivity.class))
        );

        // **Logout Button Click**
        binding.logoutBtn.setOnClickListener(v -> logoutUser());

        // **Bottom Navigation Clicks**
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new Homefragment());
                    break;
                case R.id.design:
                    replaceFragment(new DesignFragment());
                    break;
                case R.id.contactInfo:
                    replaceFragment(new ContactInfoFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

        // **Control Logout Button Visibility**
        if (fragment instanceof ProfileFragment) {
            binding.logoutBtn.setVisibility(View.VISIBLE);  // Show logout button on Profile Fragment
        } else {
            binding.logoutBtn.setVisibility(View.GONE);    // Hide on other fragments
        }
    }

    private void logoutUser() {
        prefs.setLoginStatus(false); // Clear login state
        startActivity(new Intent(TailorMain.this, Login.class));
        finish(); // Close activity
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Prevent memory leaks
    }
}
