package com.example.grocio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Product {
    private int id;
    private String name;
    private double price;
    private String category;
    private byte[] imageBytes;

    // Constructor
    public Product(int id, String name, double price, String category, byte[] imageBytes) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageBytes = imageBytes;
    }

    // Getters and setters (optional)
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public byte[] getImageBytes() { return imageBytes; }
}
