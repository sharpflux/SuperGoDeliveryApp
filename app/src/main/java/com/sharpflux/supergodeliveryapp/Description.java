package com.sharpflux.supergodeliveryapp;

public class Description {


    private String id;
    private String image;
    private String name;
    private double price;
    private String MeasurementName;
    private String MeasurementId;


    public Description(String id, String image, String name, double price) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
    }

    public Description(String id, String image, String name, double price, String measurementName) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        MeasurementName = measurementName;
    }

    public Description(String id, String image, String name, double price, String measurementName, String measurementId) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        MeasurementName = measurementName;
        MeasurementId = measurementId;
    }


    public String getMeasurementId() {
        return MeasurementId;
    }

    public void setMeasurementId(String measurementId) {
        MeasurementId = measurementId;
    }

    public String getMeasurementName() {
        return MeasurementName;
    }

    public void setMeasurementName(String measurementName) {
        MeasurementName = measurementName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
