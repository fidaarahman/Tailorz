package com.example.tailorz.customerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tailorz.customerModels.NotificationModel;
import com.example.tailorz.databinding.CustomerAdapterLayoutBinding;

import java.util.ArrayList;

public class CustomerAdapterTailor extends RecyclerView.Adapter<CustomerAdapterTailor.NotificationViewHolder> {

    private final Context context;
    private final ArrayList<NotificationModel> list;
    private final ItemClickListener itemClickListener;

    public CustomerAdapterTailor(Context context, ArrayList<NotificationModel> list, ItemClickListener itemClickListener) {
        this.context = context;
        this.list = list;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomerAdapterLayoutBinding binding = CustomerAdapterLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationModel notificationModel = list.get(position);

        holder.binding.tvDesignName.setText("Design Name: " + notificationModel.getDesignName());
        holder.binding.tvTailorName.setText("Tailor Name: " + notificationModel.getTailorName());
        holder.binding.tvDate.setText("Date: " + notificationModel.getDate());
        holder.binding.tvTime.setText("Time: " + notificationModel.getTime());
        int statusValue = notificationModel.getStatusValue();
        String status;

        switch (statusValue) {
            case 1:
                status = "Pending";
                break;
            case 2:
                status = "Processing";
                holder.binding.btnCancel.setVisibility(View.GONE);
                break;
            case 3:
                status = "Completed";
                holder.binding.btnCancel.setVisibility(View.GONE);
                break;
            default:
                status = "Unknown";
                break;
        }

        holder.binding.tvOrderStatus.setText("Status: " + status);

        holder.binding.btnCancel.setOnClickListener(v -> {
            itemClickListener.onCancel(notificationModel);
            Toast.makeText(context, "Order Cancelled", Toast.LENGTH_SHORT).show();
        });

        if (statusValue == 2 || statusValue == 3) {
            holder.binding.btnCancel.setVisibility(View.GONE);
        } else {
            holder.binding.btnCancel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        CustomerAdapterLayoutBinding binding;

        public NotificationViewHolder(@NonNull CustomerAdapterLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ItemClickListener {
        void onItemClick(NotificationModel notificationModel);
        void onCancel(NotificationModel notificationModel);
    }
}
