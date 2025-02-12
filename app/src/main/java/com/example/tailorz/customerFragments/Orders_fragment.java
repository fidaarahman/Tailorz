package com.example.tailorz.customerFragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tailorz.R;
import com.example.tailorz.customerActivities.CompleteOrdersActivity;
import com.example.tailorz.customerAdapters.CustomerAdapterTailor;
import com.example.tailorz.customerModels.NotificationModel;
import com.example.tailorz.databinding.FragmentOrdersFragmentBinding;
import com.example.tailorz.helpers.Prefs;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class Orders_fragment extends Fragment {

    private FragmentOrdersFragmentBinding binding;
    private DatabaseReference database;
    private CustomerAdapterTailor customerAdapterTailor;
    private ArrayList<NotificationModel> list;
    private Prefs prefs;

    public Orders_fragment() {
        // Required empty public constructor
    }

    public static Orders_fragment newInstance() {
        return new Orders_fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrdersFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize shared preferences
        prefs = new Prefs(requireContext());

        // Setup RecyclerView
        setupRecyclerView();

        // Show loader
        showLoader();

        // Fetch orders from Firebase
        fetchOrdersFromDatabase();

        return view;
    }

    private void setupRecyclerView() {
        list = new ArrayList<>();
        customerAdapterTailor = new CustomerAdapterTailor(getContext(), list, new CustomerAdapterTailor.ItemClickListener() {
            @Override
            public void onItemClick(NotificationModel notificationModel) {
                // Handle item click (open order details)
                Intent intent = new Intent(getContext(), CompleteOrdersActivity.class);
                intent.putExtra("orderID", notificationModel.getOrderID());
                startActivity(intent);
            }

            @Override
            public void onCancel(NotificationModel notificationModel) {
                // Handle order cancelation
                database.child(notificationModel.getOrderID())
                        .removeValue()
                        .addOnSuccessListener(aVoid -> {
                            list.remove(notificationModel);
                            customerAdapterTailor.notifyDataSetChanged();
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure
                        });
            }
        });

        binding.orderRecylerView.setHasFixedSize(true);
        binding.orderRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.orderRecylerView.setAdapter(customerAdapterTailor);
    }

    private void fetchOrdersFromDatabase() {
        new Handler().postDelayed(() -> {
            String customerID = prefs.getUserID();

            if (customerID == null || customerID.isEmpty()) {
                hideLoader();
                binding.noOrdersText.setVisibility(View.VISIBLE);
                binding.orderRecylerView.setVisibility(View.GONE);
                return;
            }
            database = FirebaseDatabase.getInstance().getReference("Orders").child(customerID);

            database.addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        NotificationModel notificationModel = dataSnapshot.getValue(NotificationModel.class);
                        if (notificationModel != null) {
                            list.add(notificationModel);
                        }
                    }

                    if (list.isEmpty()) {
                        binding.noOrdersText.setVisibility(View.VISIBLE);
                        binding.orderRecylerView.setVisibility(View.GONE);
                    } else {
                        binding.noOrdersText.setVisibility(View.GONE);
                        binding.orderRecylerView.setVisibility(View.VISIBLE);
                    }

                    customerAdapterTailor.notifyDataSetChanged();
                    hideLoader();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    hideLoader();
                    binding.noOrdersText.setVisibility(View.VISIBLE);
                    binding.orderRecylerView.setVisibility(View.GONE);
                }
            });
        }, 300);
    }



    private void showLoader() {
        binding.loaderView.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        if (binding != null && binding.loaderView != null) {
            binding.loaderView.setVisibility(View.GONE);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
