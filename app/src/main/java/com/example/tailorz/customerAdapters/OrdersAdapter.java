//package com.example.tailorz.customerAdapters;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.tailorz.customerModels.NotificationModel;  // Assuming you're using NotificationModel now
//import com.example.tailorz.R;
//
//import java.util.ArrayList;
//
//public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {
//
//    Context context;
//    ArrayList<NotificationModel> list;  // Changed to NotificationModel for consistency
//    ItemClickListener item_click;
//
//    public OrdersAdapter(Context context, ArrayList<NotificationModel> list, ItemClickListener item_click) {
//        this.context = context;
//        this.list = list;
//        this.item_click = item_click;
//    }
//
//    @NonNull
//    @Override
//    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.singleorder, parent, false);
//        return new OrdersViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
//
//        NotificationModel notificationModel = list.get(position);
//
//        // Bind the data to views
//        holder.Date.setText("Order Date: " + notificationModel.getDate());
//        holder.orderID.setText("Order ID: " + notificationModel.getOrderID());
//        holder.tailorName.setText("Tailor Name: " + notificationModel.getTailorName());
//        holder.DesignID.setText("Design ID: " + notificationModel.getDesignName());
//        holder.DesignName.setText("Design Name: " + notificationModel.getDesignName());
//
//        // Load the design image using Glide
//        Glide.with(context)
//                .load(notificationModel.getDesignUrl())  // Assuming the field name is 'designUrl'
//                .placeholder(R.drawable.logo)
//                .error(R.drawable.imagenotfound)
//                .into(holder.DesignImage);
//
//        // Handle click events for the item
//        holder.itemView.setOnClickListener(v -> item_click.onItemClick(list.get(position)));
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public static class OrdersViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView DesignImage;
//        TextView DesignName, DesignID, tailorName, orderID, Date;
//
//        public OrdersViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            // Initialize the views
//            DesignImage = itemView.findViewById(R.id.TailorDesignImageCard);
//            DesignName = itemView.findViewById(R.id.DesignName);
//            DesignID = itemView.findViewById(R.id.DesignID);
//            tailorName = itemView.findViewById(R.id.TailorName);
//            orderID = itemView.findViewById(R.id.OrderID);
//            Date = itemView.findViewById(R.id.OrderDate);
//        }
//    }
//
//    // Define the item click listener interface
//    public interface ItemClickListener {
//        void onItemClick(NotificationModel notificationModel);  // Using NotificationModel here
//    }
//}
