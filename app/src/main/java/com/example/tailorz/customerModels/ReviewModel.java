package com.example.tailorz.customerModels;

public class ReviewModel {

    float rateValue;
    String review, customerName, tailorName, designName, designID;

    public ReviewModel() {
    }

    public ReviewModel(float rateValue, String review, String customerName, String tailorName, String designName, String designID) {
        this.rateValue = rateValue;
        this.review = review;
        this.customerName = customerName;
        this.tailorName = tailorName;
        this.designName = designName;
        this.designID = designID;
    }

    public float getRateValue() {
        return rateValue;
    }

    public void setRateValue(float rateValue) {
        this.rateValue = rateValue;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTailorName() {
        return tailorName;
    }

    public void setTailorName(String tailorName) {
        this.tailorName = tailorName;
    }

    public String getDesignName() {
        return designName;
    }

    public void setDesignName(String designName) {
        this.designName = designName;
    }

    public String getDesignID() {
        return designID;
    }

    public void setDesignID(String designID) {
        this.designID = designID;
    }
}
