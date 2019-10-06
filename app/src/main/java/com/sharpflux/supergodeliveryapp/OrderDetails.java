package com.sharpflux.supergodeliveryapp;

public class OrderDetails {

    private String pickupAddress;
    private String deliveryAddress, TotalCharges,Distance,Duration,DeliveryStatus,DeliveryId,InsertionDate,InsertionTime;

    public OrderDetails(String pickupAddress, String TotalCharges) {
        this.pickupAddress = pickupAddress;
        this.TotalCharges = TotalCharges;
    }

    public OrderDetails(String pickupAddress,   String totalCharges, String distance, String duration, String deliveryStatus, String deliveryId, String insertionDate, String insertionTime) {
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        TotalCharges = totalCharges;
        Distance = distance;
        Duration = duration;
        DeliveryStatus = deliveryStatus;
        DeliveryId = deliveryId;
        InsertionDate = insertionDate;
        InsertionTime = insertionTime;
    }


    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getInsertionDate() {
        return InsertionDate;
    }

    public void setInsertionDate(String insertionDate) {
        InsertionDate = insertionDate;
    }

    public String getInsertionTime() {
        return InsertionTime;
    }

    public void setInsertionTime(String insertionTime) {
        InsertionTime = insertionTime;
    }

    public String getTotalCharges() {
        return TotalCharges;
    }

    public String getDeliveryStatus() {
        return DeliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        DeliveryStatus = deliveryStatus;
    }

    public void setTotalCharges(String totalCharges) {
        TotalCharges = totalCharges;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getDuration() {
        return Duration;
    }

    public String getDeliveryId() {
        return DeliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        DeliveryId = deliveryId;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }
}
