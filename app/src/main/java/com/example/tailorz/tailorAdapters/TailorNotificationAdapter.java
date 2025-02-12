package com.example.tailorz.tailorAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tailorz.customerModels.NotificationModel;
import com.example.tailorz.databinding.TailoradapterLayoutBinding;

import java.util.ArrayList;

public class TailorNotificationAdapter extends RecyclerView.Adapter<TailorNotificationAdapter.NotificationViewHolder> {

    private final Context context;
    private final ArrayList<NotificationModel> list;
    private final ItemClickListener itemClickListener;

    public TailorNotificationAdapter(Context context, ArrayList<NotificationModel> list, ItemClickListener itemClickListener) {
        this.context = context;
        this.list = list;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TailoradapterLayoutBinding binding = TailoradapterLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationModel notificationModel = list.get(position);

        holder.binding.tvHeading.setText("Order ID: " + notificationModel.getOrderID());
        holder.binding.tvNotificationContent.setText("Design: " + notificationModel.getDesignName() + "\nCustomer: " + notificationModel.getCustomerName());

        int status = notificationModel.getStatusValue();

        holder.binding.btnConfirm.setVisibility(status == 1 ? View.VISIBLE : View.GONE);
        holder.binding.btnDecline.setVisibility(status == 1 ? View.VISIBLE : View.GONE);
        holder.binding.btnComplete.setVisibility(status == 2 ? View.VISIBLE : View.GONE);
        holder.binding.btnClose.setVisibility(status == 3 ? View.VISIBLE : View.GONE);

        holder.binding.btnConfirm.setOnClickListener(v -> itemClickListener.onConfirm(notificationModel));
        holder.binding.btnDecline.setOnClickListener(v -> itemClickListener.onClose(notificationModel));
        holder.binding.btnComplete.setOnClickListener(v -> itemClickListener.onComplete(notificationModel));
        holder.binding.btnClose.setOnClickListener(v -> itemClickListener.onClose(notificationModel));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TailoradapterLayoutBinding binding;

        public NotificationViewHolder(@NonNull TailoradapterLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ItemClickListener {
        void onConfirm(NotificationModel notificationModel);
        void onComplete(NotificationModel notificationModel);
        void onClose(NotificationModel notificationModel);
    }
}
