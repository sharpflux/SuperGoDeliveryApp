package com.sharpflux.supergodeliveryapp;

public class OrderDetails {

    private String pickupAddress;
    private String  TotalCharges,Distance,Duration,DeliveryStatus,DeliveryId;
    private String DeliveryStatusId;
    private String InsertionTime;




    public OrderDetails(String pickupAddress, String totalCharges, String distance,
                        String duration, String deliveryStatus, String deliveryId, String deliveryStatusId,String InsertionTime) {
        this.pickupAddress = pickupAddress;

        this.TotalCharges = totalCharges;
        this.Distance = distance;
        this.Duration = duration;
        this.DeliveryStatus = deliveryStatus;
        this.DeliveryId = deliveryId;
        this.DeliveryStatusId = deliveryStatusId;
        this.InsertionTime = InsertionTime;
    }

    public String getInsertionTime() {
        return InsertionTime;
    }

    public void setInsertionTime(String insertionTime) {
        InsertionTime = insertionTime;
    }

    public String getDeliveryStatusId() {
        return DeliveryStatusId;
    }

    public void setDeliveryStatusId(String deliveryStatusId) {
        DeliveryStatusId = deliveryStatusId;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
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
