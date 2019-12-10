package com.sharpflux.supergodeliveryapp;

public class CheckOutItems {
    private String id;
    private String image;
    private String name;
    private double price;

    private int Quantity;

    public CheckOutItems(String id, String image, String name, double price,int Qty) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        this.Quantity=Qty;
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

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
