package com.example.tailorz.customerModels;

public class NotificationModel {
    private String orderID;
    private String designName;
    private String customerName;
    private String customerEmail;
    private String customerID; // ✅ NEW FIELD for encoded email
    private String tailorName;
    private int statusValue;
    private String date;
    private String time;

    // Default Constructor (required for Firebase)
    public NotificationModel() {}

    // Constructor with all fields
    public NotificationModel(String orderID, String designName, String customerName, String customerEmail, String customerID, String tailorName, int statusValue, String date, String time) {
        this.orderID = orderID;
        this.designName = designName;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerID = customerID; // ✅ Store encoded email
        this.tailorName = tailorName;
        this.statusValue = statusValue;
        this.date = date;
        this.time = time;
    }

    // Getters and Setters
    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getDesignName() {
        return designName;
    }

    public void setDesignName(String designName) {
        this.designName = designName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerID() {  // ✅ NEW Getter
        return customerID;
    }

    public void setCustomerID(String customerID) {  // ✅ NEW Setter
        this.customerID = customerID;
    }

    public String getTailorName() {
        return tailorName;
    }

    public void setTailorName(String tailorName) {
        this.tailorName = tailorName;
    }

    public int getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(int statusValue) {
        this.statusValue = statusValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
