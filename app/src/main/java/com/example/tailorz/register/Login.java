package com.example.tailorz.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tailorz.adminPanel.AdminMainPanel;
import com.example.tailorz.customerActivities.CustomerMain;
import com.example.tailorz.helpers.Prefs;
import com.example.tailorz.tailorActivities.TailorMain;
import com.example.tailorz.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.github.razir.progressbutton.DrawableButtonExtensionsKt;

import kotlin.Unit;

public class Login extends AppCompatActivity {

    private static final String DATABASE_URL = "https://flashspark-1c928-default-rtdb.firebaseio.com/";
    private ActivityLoginBinding binding;
    private Prefs prefs;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefs = new Prefs(getApplicationContext());

        // Check login status
        if (prefs.getLoginStatus()) {
            if ("Customer".equals(prefs.getUserCategory())) {
                startActivity(new Intent(Login.this, CustomerMain.class));
                finish();
            } else if ("Tailor".equals(prefs.getUserCategory())) {
                startActivity(new Intent(Login.this, TailorMain.class));
                finish();
            }
        }

        // Set up click listener for the "Sign Up" text
        binding.tvSignupLink.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignUp.class);
            startActivity(intent);
            finish();
        });

        binding.btLogin.setOnClickListener(v -> AuthenticateUser());
        binding.tvForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());

    }

    void AuthenticateUser() {

        DrawableButtonExtensionsKt.showProgress(binding.btLogin, progressParams -> {
            progressParams.setButtonText("Logging in...");
            return Unit.INSTANCE;
        });

        new Handler().postDelayed(() -> {
            String userName = binding.tvUsername.getText().toString();
            String password = binding.tvPassword.getText().toString();
            String encodedUserName = encodeEmail(userName);

            if (userName.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Please Enter Credentials", Toast.LENGTH_SHORT).show();
                resetLoginButton("Login");
            } else if ("admin".equalsIgnoreCase(userName) && "admin".equalsIgnoreCase(password)) {
                resetLoginButton("Login");
                startActivity(new Intent(Login.this, AdminMainPanel.class));
                finish();
            } else {
                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(encodedUserName)) {
                            String databasePass = snapshot.child(encodedUserName).child("password").getValue(String.class);
                            assert databasePass != null;
                            if (databasePass.equals(password)) {
                                Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                                String category = snapshot.child(encodedUserName).child("category").getValue(String.class);
                                saveUserData(snapshot, encodedUserName);

                                resetLoginButton("Success");

                                // Navigate based on user category
                                assert category != null;
                                if ("Customer".equals(category)) {
                                    startActivity(new Intent(Login.this, CustomerMain.class));
                                    finish();
                                } else if ("Tailor".equals(category)) {
                                    startActivity(new Intent(Login.this, TailorMain.class));
                                    finish();
                                }
                            } else {
                                Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                resetLoginButton("Login");
                            }
                        } else {
                            Toast.makeText(Login.this, "User Does Not Exist", Toast.LENGTH_SHORT).show();
                            resetLoginButton("Login");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Login.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        resetLoginButton("Login");
                    }
                });
            }
        }, 300);
    }

    private void resetLoginButton(String buttonText) {
        DrawableButtonExtensionsKt.hideProgress(binding.btLogin, buttonText);
    }

    private void saveUserData(DataSnapshot snapshot, String encodedUserName) {
        // Retrieve all user details from Firebase
        String username = snapshot.child(encodedUserName).child("username").getValue(String.class);
        String email = snapshot.child(encodedUserName).child("email").getValue(String.class);
        String address = snapshot.child(encodedUserName).child("address").getValue(String.class);
        String telephone = snapshot.child(encodedUserName).child("telephoneNumber").getValue(String.class);
        String whatsapp = snapshot.child(encodedUserName).child("whatsappNumber").getValue(String.class);
        String chest = snapshot.child(encodedUserName).child("chest_measurements").getValue(String.class);
        String waist = snapshot.child(encodedUserName).child("waist_measurements").getValue(String.class);
        String legs = snapshot.child(encodedUserName).child("legs_measurements").getValue(String.class);
        String arms = snapshot.child(encodedUserName).child("arms_measurements").getValue(String.class);
        String full = snapshot.child(encodedUserName).child("full_measurements").getValue(String.class);
        String profile_img = snapshot.child(encodedUserName).child("profile_image_url").getValue(String.class);
        String tailorCategory = snapshot.child(encodedUserName).child("tailor_category").getValue(String.class);
        String userGender = snapshot.child(encodedUserName).child("gender").getValue(String.class);
        String category = snapshot.child(encodedUserName).child("category").getValue(String.class);

        Log.d("LoginActivity", "User Data Retrieved:");
        Log.d("LoginActivity", "Username: " + username);
        Log.d("LoginActivity", "Email: " + email);
        Log.d("LoginActivity", "Address: " + address);
        Log.d("LoginActivity", "Telephone: " + telephone);
        Log.d("LoginActivity", "WhatsApp: " + whatsapp);
        Log.d("LoginActivity", "Chest Measurement: " + chest);
        Log.d("LoginActivity", "Waist Measurement: " + waist);
        Log.d("LoginActivity", "Legs Measurement: " + legs);
        Log.d("LoginActivity", "Arms Measurement: " + arms);
        Log.d("LoginActivity", "Full Body Measurement: " + full);
        Log.d("LoginActivity", "Profile Image URL: " + profile_img);
        Log.d("LoginActivity", "Tailor Category: " + tailorCategory);
        Log.d("LoginActivity", "Gender: " + userGender);
        Log.d("LoginActivity", "Category: " + category);

        // ✅ Store user ID (encoded email) in SharedPreferences
        prefs.setUserID(encodedUserName);

        // ✅ Store retrieved user data in SharedPreferences
        prefs.setUserEmail(email);
        prefs.setUserName(username);
        prefs.setUserCategory(category);
        prefs.setUserWhatsapp(whatsapp);
        prefs.setUserAddress(address);
        prefs.setUserTelephone(telephone);
        prefs.setUserProfileImage(profile_img);
        prefs.setTailorCategory(tailorCategory);
        prefs.setUserGender(userGender);
        prefs.setUserArms(arms);
        prefs.setUserChest(chest);
        prefs.setUserLegs(legs);
        prefs.setUserFull(full);
        prefs.setUserWaist(waist);
        prefs.setLoginStatus(true);
    }



    private String encodeEmail(String email) {
        return email.replace(".", ","); // Replace invalid characters with valid ones
    }
    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password");

        // Create Layout
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);

        // Email Input Field
        final EditText emailInput = new EditText(this);
        emailInput.setHint("Enter your email");
        emailInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        layout.addView(emailInput);

        // New Password Input Field
        final EditText passwordInput = new EditText(this);
        passwordInput.setHint("Enter new password");
        passwordInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(passwordInput);

        builder.setView(layout);

        // Set Buttons
        builder.setPositiveButton("Reset", (dialog, which) -> {
            String email = emailInput.getText().toString().trim();
            String newPassword = passwordInput.getText().toString().trim();
            resetPassword(email, newPassword);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }
    private void resetPassword(String email, String newPassword) {
        if (email.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String encodedEmail = encodeEmail(email); // Encode email to match Firebase keys

        DatabaseReference usersRef = databaseReference.child("Users");

        usersRef.child(encodedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Update Password in Firebase
                    usersRef.child(encodedEmail).child("password").setValue(newPassword)
                            .addOnSuccessListener(unused -> Toast.makeText(Login.this, "Password Reset Successful!", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(Login.this, "Failed to update password", Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(Login.this, "Email not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Login.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
