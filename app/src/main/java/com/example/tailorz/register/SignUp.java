package com.example.tailorz.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apachat.loadingbutton.core.customViews.CircularProgressButton;
import com.example.tailorz.R;
import com.example.tailorz.helpers.Prefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    private EditText email_txt, mobileNo_txt, userName_txt, password_txt, confirmPassword_txt;
    RadioGroup genderRadioGroup, categoryRadioGroup, tailorCategoryRadioGroup;
    RadioButton genderRadioBtn, categoryRadioBtn, tailorCategory;

    private static final String DATABASE_URL = "https://flashspark-1c928-default-rtdb.firebaseio.com/";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email_txt = findViewById(R.id.emailtxt);
        mobileNo_txt = findViewById(R.id.mobileNotxt);
        userName_txt = findViewById(R.id.usernametxt);
        password_txt = findViewById(R.id.passwordtxt);
        confirmPassword_txt = findViewById(R.id.confirmPasswordtxt);
        genderRadioGroup = findViewById(R.id.genderRadioGrpId);
        categoryRadioGroup = findViewById(R.id.categoryRadioGrp);
        tailorCategoryRadioGroup = findViewById(R.id.TailorcategoryRadioGrp);

        TextView clickHere_txt = findViewById(R.id.clickheretxt);
        CircularProgressButton signUp_Btn = findViewById(R.id.signUpBtn);

        // Navigate to Login if the user already has an account
        clickHere_txt.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(SignUp.this, Login.class));
        });

        signUp_Btn.setOnClickListener(v -> {
            signUp_Btn.startAnimation();

            new Handler().postDelayed(() -> {
                final String email = email_txt.getText().toString();
                final String encodedEmail = encodeEmail(email); // Encode the email
                final String phoneNumber = mobileNo_txt.getText().toString();
                final String password = password_txt.getText().toString();
                final String conPass = confirmPassword_txt.getText().toString();
                final String userName = userName_txt.getText().toString();

                if (email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || userName.isEmpty() || conPass.isEmpty()) {
                    Toast.makeText(SignUp.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    recreate();
                } else if (!password.equals(conPass)) {
                    Toast.makeText(SignUp.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                    recreate();
                } else {
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(encodedEmail)) {
                                Toast.makeText(SignUp.this, "User Already Exists!", Toast.LENGTH_SHORT).show();
                                signUp_Btn.stopAnimation();
                                recreate();
                            } else {
                                // Save user details
                                databaseReference.child("Users").child(encodedEmail).child("username").setValue(userName);
                                databaseReference.child("Users").child(encodedEmail).child("password").setValue(password);
                                databaseReference.child("Users").child(encodedEmail).child("email").setValue(email);
                                databaseReference.child("Users").child(encodedEmail).child("telephoneNumber").setValue(phoneNumber);
                                databaseReference.child("Users").child(encodedEmail).child("whatsappNumber").setValue(phoneNumber);
                                databaseReference.child("Users").child(encodedEmail).child("address").setValue("");
                                databaseReference.child("Users").child(encodedEmail).child("profile_image_url").setValue("");
                                databaseReference.child("Users").child(encodedEmail).child("arms_measurements").setValue("");
                                databaseReference.child("Users").child(encodedEmail).child("legs_measurements").setValue("");
                                databaseReference.child("Users").child(encodedEmail).child("waist_measurements").setValue("");
                                databaseReference.child("Users").child(encodedEmail).child("full_measurements").setValue("");
                                databaseReference.child("Users").child(encodedEmail).child("chest_measurements").setValue("");

                                // Get radio button values
                                int selectedId1 = genderRadioGroup.getCheckedRadioButtonId();
                                int selectedId2 = categoryRadioGroup.getCheckedRadioButtonId();
                                int selectedId3 = tailorCategoryRadioGroup.getCheckedRadioButtonId();

                                if (selectedId1 == -1 || selectedId2 == -1 || selectedId3 == -1) {
                                    Toast.makeText(getApplicationContext(), "Please Fill All the Fields", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                genderRadioBtn = findViewById(selectedId1);
                                categoryRadioBtn = findViewById(selectedId2);
                                tailorCategory = findViewById(selectedId3);

                                String genderSelected_txt = genderRadioBtn.getText().toString();
                                String categorySelected_txt = categoryRadioBtn.getText().toString();
                                String tailorCategory_txt = tailorCategory.getText().toString();

                                databaseReference.child("Users").child(encodedEmail).child("gender").setValue(genderSelected_txt);
                                databaseReference.child("Users").child(encodedEmail).child("category").setValue(categorySelected_txt);
                                databaseReference.child("Users").child(encodedEmail).child("tailor_category").setValue(tailorCategory_txt);

                                Drawable drawable = getResources().getDrawable(R.drawable.checkbutton);
                                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                signUp_Btn.doneLoadingAnimation(R.color.white, bitmap);
                                Prefs prefs = new Prefs(getApplicationContext());
                                prefs.setUserID(encodedEmail);

                                Toast.makeText(SignUp.this, "Your Account Has Been Created Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp.this, Login.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(SignUp.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }, 300);
        });
    }

    // Helper method to encode email
    private String encodeEmail(String email) {
        return email.replace(".", ",");
    }
}
