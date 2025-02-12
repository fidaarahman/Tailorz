package com.example.tailorz.tailorFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tailorz.R;
import com.example.tailorz.customerModels.NotificationModel;
import com.example.tailorz.databinding.FragmentContactInfoBinding;
import com.example.tailorz.helpers.Prefs;
import com.example.tailorz.tailorAdapters.TailorNotificationAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ContactInfoFragment extends Fragment {
    private FragmentContactInfoBinding binding;
    private DatabaseReference database;
    private TailorNotificationAdapter tailornotificationAdapter;
    private ArrayList<NotificationModel> list;
    private Prefs prefs;

    public ContactInfoFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentContactInfoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize Prefs
        prefs = new Prefs(requireContext());

        // Setup RecyclerView
        binding.notificationRecyclerView.setHasFixedSize(true);
        binding.notificationRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        list = new ArrayList<>();
        tailornotificationAdapter = new TailorNotificationAdapter(requireContext(), list, new TailorNotificationAdapter.ItemClickListener() {
            @Override
            public void onConfirm(NotificationModel notificationModel) {
                updateOrderStatus(notificationModel, 2); // Confirm changes status to 2
            }

            @Override
            public void onComplete(NotificationModel notificationModel) {
                updateOrderStatus(notificationModel, 3); // Complete changes status to 3
            }

            @Override
            public void onClose(NotificationModel notificationModel) {
                removeOrder(notificationModel); // Removes closed order
            }
        });

        binding.notificationRecyclerView.setAdapter(tailornotificationAdapter);

        showLoader();
        fetchOrdersFromDatabase();

        return view;
    }

    private void fetchOrdersFromDatabase() {
        String tailorName = prefs.getUserName();
        database = FirebaseDatabase.getInstance().getReference("Orders");

        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot customerSnapshot : snapshot.getChildren()) {
                    String customerID = customerSnapshot.getKey();

                    for (DataSnapshot orderSnapshot : customerSnapshot.getChildren()) {
                        if (orderSnapshot.getValue() instanceof Map) {
                            NotificationModel order = orderSnapshot.getValue(NotificationModel.class);

                            if (order != null && tailorName.equals(order.getTailorName())) {
                                order.setCustomerID(customerID);
                                list.add(order);
                            }
                        }
                    }
                }

                tailornotificationAdapter.notifyDataSetChanged();
                hideLoader();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideLoader();
                Toast.makeText(requireContext(), "Failed to fetch orders.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateOrderStatus(NotificationModel notificationModel, int newStatus) {
        DatabaseReference orderReference = FirebaseDatabase.getInstance()
                .getReference("Orders")
                .child(notificationModel.getCustomerID())
                .child(notificationModel.getOrderID());

        orderReference.child("statusValue").setValue(newStatus)
                .addOnSuccessListener(aVoid -> {
                    notificationModel.setStatusValue(newStatus);
                    tailornotificationAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to update order.", Toast.LENGTH_SHORT).show();
                });
    }

    private void removeOrder(NotificationModel notificationModel) {
        DatabaseReference orderReference = FirebaseDatabase.getInstance()
                .getReference("Orders")
                .child(notificationModel.getCustomerID())
                .child(notificationModel.getOrderID());

        orderReference.removeValue().addOnSuccessListener(aVoid -> {
            list.remove(notificationModel);
            tailornotificationAdapter.notifyDataSetChanged();
            Toast.makeText(requireContext(), "Order Closed", Toast.LENGTH_SHORT).show();
        });
    }

    private void showLoader() {
        binding.getRoot().findViewById(R.id.loaderView).setVisibility(View.VISIBLE);

    }

    private void hideLoader() {
        binding.getRoot().findViewById(R.id.loaderView).setVisibility(View.GONE);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
