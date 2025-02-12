package com.example.tailorz.customerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tailorz.R;
import com.example.tailorz.customerModels.CustomerTailorModel;
import com.example.tailorz.databinding.SingleTailorlayoutBinding;

import java.util.ArrayList;

public class CustomerTailorAdapter extends RecyclerView.Adapter<CustomerTailorAdapter.CustomerTailorViewHolder> {

    private final Context context;
    private final ArrayList<CustomerTailorModel> list;

    public CustomerTailorAdapter(Context context, ArrayList<CustomerTailorModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CustomerTailorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SingleTailorlayoutBinding binding = SingleTailorlayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CustomerTailorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerTailorViewHolder holder, int position) {
        CustomerTailorModel tailor = list.get(position);

        // Set tailor name
        holder.binding.tailorName.setText(tailor.getUsername());

        // Load profile image with Glide
        Glide.with(context)
                .load(tailor.getProfile_image_url())
                .placeholder(R.drawable.logo) // While loading
                .error(R.drawable.imagenotfound) // If error
                .into(holder.binding.TailorProfileImage);

        // Set tailor category
        holder.binding.tailorCategory.setText(tailor.getTailor_category());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CustomerTailorViewHolder extends RecyclerView.ViewHolder {
        private final SingleTailorlayoutBinding binding;

        public CustomerTailorViewHolder(@NonNull SingleTailorlayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
