package com.example.tailorz.customerFragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tailorz.customerActivities.HireMeActivity;
import com.example.tailorz.customerAdapters.CustomerDesignAdapter;
import com.example.tailorz.R;
import com.example.tailorz.tailorModels.TailorDesignModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Customer_design_fragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference database;
    CustomerDesignAdapter customerDesignAdapter;
    ArrayList<TailorDesignModel> list;
    private View loaderView;

    public Customer_design_fragment() {
        // Required empty public constructor
    }

    public static Customer_design_fragment newInstance(String param1, String param2) {
        return new Customer_design_fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_design_fragment, container, false);

        loaderView = view.findViewById(R.id.loaderView);
        recyclerView = view.findViewById(R.id.Customer_Designs_recyclerView);
        recyclerView.setHasFixedSize(true);

        // Set GridLayoutManager with 2 columns (VERTICAL scrolling)
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        list = new ArrayList<>();
        customerDesignAdapter = new CustomerDesignAdapter(getContext(), list, new CustomerDesignAdapter.ItemClickListener() {
            @Override
            public void onItemClick(TailorDesignModel designModel) {
                Log.d("Customer_design_fragment", "Opening HireMeActivity for Tailor: " + designModel.getTailor_username());

                // Start HireMeActivity with already loaded tailor details
                Intent intent = new Intent(getContext(), HireMeActivity.class);
                intent.putExtra("HireDesignName", designModel.getDesign_name());
                intent.putExtra("HireDesignID", designModel.getDesign_id());
                intent.putExtra("HireTailorName", designModel.getTailor_username());
                intent.putExtra("HireTailorPhone", designModel.getTailor_phone() != null ? designModel.getTailor_phone() : "Not Available");
                intent.putExtra("HireTailorAddress", designModel.getTailor_address() != null ? designModel.getTailor_address() : "Not Available");

                startActivity(intent);
            }
        });

        recyclerView.setAdapter(customerDesignAdapter);
        showLoader();
        GetDataFromDatabase();
        return view;
    }

    private void GetDataFromDatabase() {
        database = FirebaseDatabase.getInstance().getReference();

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                DataSnapshot designSnapshot = snapshot.child("Design");
                DataSnapshot usersSnapshot = snapshot.child("Users");

                HashMap<String, DataSnapshot> usersMap = new HashMap<>();
                for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
                    String username = userSnapshot.child("username").getValue(String.class);
                    if (username != null) {
                        usersMap.put(username, userSnapshot);
                    }
                }

                for (DataSnapshot tailorSnapshot : designSnapshot.getChildren()) {
                    for (DataSnapshot designItem : tailorSnapshot.getChildren()) {
                        TailorDesignModel tailorDesignModel = designItem.getValue(TailorDesignModel.class);

                        if (tailorDesignModel != null) {
                            String tailorUsername = tailorDesignModel.getTailor_username();

                            if (tailorUsername != null && !tailorUsername.isEmpty()) {
                                // Fetch tailor details using the Users map
                                DataSnapshot userDetails = usersMap.get(tailorUsername);
                                if (userDetails != null) {
                                    String tailorPhone = userDetails.child("telephoneNumber").getValue(String.class);
                                    String tailorAddress = userDetails.child("address").getValue(String.class);

                                    // Debugging logs to check if values exist
                                    Log.d("FirebaseData", "Checking details for Tailor: " + tailorUsername);
                                    Log.d("FirebaseData", "Phone: " + tailorPhone);
                                    Log.d("FirebaseData", "Address: " + tailorAddress);

                                    tailorDesignModel.setTailor_phone(tailorPhone != null ? tailorPhone : "Not Available");
                                    tailorDesignModel.setTailor_address(tailorAddress != null ? tailorAddress : "Not Available");
                                } else {
                                    Log.e("FirebaseData", "Tailor NOT found in Users table: " + tailorUsername);
                                }
                            }

                            list.add(tailorDesignModel);
                        }
                    }
                }

                customerDesignAdapter.notifyDataSetChanged();
                hideLoader();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideLoader();
                Toast.makeText(getContext(), "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void showLoader() {
        loaderView.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        loaderView.setVisibility(View.GONE);
    }
}
