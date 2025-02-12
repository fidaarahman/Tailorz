package com.example.tailorz.customerFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tailorz.customerAdapters.CustomerTailorAdapter;
import com.example.tailorz.customerModels.CustomerTailorModel;
import com.example.tailorz.databinding.FragmentCustomerTailorsFragmentBinding;
import com.example.tailorz.helpers.Prefs;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class Customer_tailors_fragment extends Fragment {

    private FragmentCustomerTailorsFragmentBinding binding;
    private DatabaseReference database;
    private CustomerTailorAdapter customerTailorAdapter;
    private ArrayList<CustomerTailorModel> list;
    private Prefs prefs;

    public Customer_tailors_fragment() {
    }

    public static Customer_tailors_fragment newInstance() {
        return new Customer_tailors_fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCustomerTailorsFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        prefs = new Prefs(requireContext());
        list = new ArrayList<>();
        customerTailorAdapter = new CustomerTailorAdapter(requireContext(), list);

        binding.CustomerTailorsRecyclerView.setHasFixedSize(true);
        binding.CustomerTailorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.CustomerTailorsRecyclerView.setAdapter(customerTailorAdapter);

        showLoader();
        fetchTailorsFromDatabase();

        return view;
    }

    private void fetchTailorsFromDatabase() {
        database = FirebaseDatabase.getInstance().getReference("Users");

        new Handler().postDelayed(() -> {
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CustomerTailorModel tailor = dataSnapshot.getValue(CustomerTailorModel.class);
                        if (tailor != null && "Tailor".equalsIgnoreCase(dataSnapshot.child("category").getValue(String.class))) {
                            list.add(tailor);
                        }
                    }

                    customerTailorAdapter.notifyDataSetChanged();
                    hideLoader();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    hideLoader();
                }
            });
        }, 300);
    }

    private void showLoader() {
        binding.loaderView.getRoot().setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        binding.loaderView.getRoot().setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}
