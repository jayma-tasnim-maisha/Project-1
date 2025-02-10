package com.example.grocio;

import android.graphics.Bitmap;

public class Product {
    private String name;
    private Bitmap image;  // Store image as Bitmap
    private double price;

    // Constructor
    public Product(String name, Bitmap image, double price) {
        this.name = name;
        this.image = image;
        this.price = price;
    }


    public String getName() {
        return name;
    }


    public Bitmap getImage() {
        return image;
    }


    public double getPrice() {
        return price;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setImage(Bitmap image) {
        this.image = image;
    }


    public void setPrice(double price) {
        this.price = price;
    }
}