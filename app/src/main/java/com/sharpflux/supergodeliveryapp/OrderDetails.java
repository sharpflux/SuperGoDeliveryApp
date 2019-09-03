package com.sharpflux.supergodeliveryapp;

public class OrderDetails {

    private String pickupAddress;
    private String deliveryAddress, TotalCharges,Distance,Duration,DeliveryStatus,DeliveryId;

    public OrderDetails(String pickupAddress, String TotalCharges) {
        this.pickupAddress = pickupAddress;
        this.TotalCharges = TotalCharges;
    }
    public OrderDetails(String pickupAddress, String TotalCharges,String Distance,String Duration,String DeliveryStatus,String DeliveryId) {
        this.pickupAddress = pickupAddress;
        this.TotalCharges = TotalCharges;
        this.Distance = Distance;
        this.Duration = Duration;
        this.DeliveryStatus = DeliveryStatus;
        this.DeliveryId = DeliveryId;
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
