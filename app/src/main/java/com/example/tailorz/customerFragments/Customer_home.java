package com.example.tailorz.customerFragments;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tailorz.R;
import com.example.tailorz.databinding.FragmentCustomerHomeBinding;

public class Customer_home extends Fragment {

    private FragmentCustomerHomeBinding binding;

    public Customer_home() {
    }

    public static Customer_home newInstance(String param1, String param2) {
        return new Customer_home();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout using View Binding
        binding = FragmentCustomerHomeBinding.inflate(inflater, container, false);

        replaceFragment(new Customer_design_fragment());

        binding.DesignsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Customer_design_fragment());
                binding.DesignsBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                binding.TailorsBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
            }
        });

        binding.TailorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Customer_tailors_fragment());
                binding.TailorsBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                binding.DesignsBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
            }
        });

        return binding.getRoot();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Customer_menu_frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
}
