package com.example.grocio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Test_DB";
    public static final int DATABASE_VERSION = 3;

    // Products table constants
    public static final String TABLE_PRODUCTS = "products";
    public static final String COL_ID = "_id";
    public static final String COL_PRODUCT_NAME = "productName";
    public static final String COL_PRODUCT_PRICE = "productPrice";
    public static final String COL_PRODUCT_QUANTITY = "productQuantity";
    public static final String COL_PRODUCT_IMAGE_URI = "productImageUri";
    public static final String COL_PRODUCT_DESCRIPTION = "productDescription";
    public static final String COL_PRODUCT_CATEGORY = "productCategory";

    // Doctors table constants
    public static final String TABLE_DOCTORS = "doctors";
    public static final String COL_DOCTOR_ID = "_id";
    public static final String COL_DOCTOR_NAME = "doctorName";
    public static final String COL_DOCTOR_SPECIALIZATION = "doctorSpecialization";
    public static final String COL_DOCTOR_CONTACT = "doctorContact";
    public static final String COL_DOCTOR_EMAIL = "doctorEmail";

    public static final String TABLE_CART = "cart";
    public static final String COL_CART_PRODUCT_ID = "product_id";
    public static final String COL_CART_PRODUCT_NAME = "product_name";
    public static final String COL_CART_PRODUCT_PRICE = "product_price";


    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PRODUCT_NAME + " TEXT, " +
                COL_PRODUCT_DESCRIPTION + " TEXT, " +
                COL_PRODUCT_CATEGORY + " TEXT, " +
                COL_PRODUCT_PRICE + " REAL, " +
                COL_PRODUCT_QUANTITY + " INTEGER, " +
                COL_PRODUCT_IMAGE_URI + " BLOB)");

        // Create the doctors table
        db.execSQL("CREATE TABLE " + TABLE_DOCTORS + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "doctorName TEXT, " +
                "doctorSpecialization TEXT, " +
                "doctorContact TEXT, " +
                "doctorEmail TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_CART + " ("
                + COL_CART_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_CART_PRODUCT_NAME + " TEXT, "
                + COL_CART_PRODUCT_PRICE + " REAL)");



    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART); // Added this line
        onCreate(db);
    }


    // Get product by ID
    public Cursor getProductById(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COL_ID + " = ?", new String[]{String.valueOf(productId)});
    }

    // Update product details
    public boolean updateProduct(int id, String name, String description, String category, double price, int quantity, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PRODUCT_NAME, name);
        values.put(COL_PRODUCT_DESCRIPTION, description);
        values.put(COL_PRODUCT_CATEGORY, category);
        values.put(COL_PRODUCT_PRICE, price);
        values.put(COL_PRODUCT_QUANTITY, quantity);
        values.put(COL_PRODUCT_IMAGE_URI, image);

        int rowsAffected = db.update(TABLE_PRODUCTS, values, COL_ID + " = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }

    // Insert a product
    public void insertProduct(String name, String description, String category, double price, int quantity, byte[] imageByteArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PRODUCT_NAME, name);
        values.put(COL_PRODUCT_DESCRIPTION, description);
        values.put(COL_PRODUCT_CATEGORY, category);
        values.put(COL_PRODUCT_PRICE, price);
        values.put(COL_PRODUCT_QUANTITY, quantity);
        values.put(COL_PRODUCT_IMAGE_URI, imageByteArray);
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    // Get all products
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
    }

    public Cursor getProductsByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COL_PRODUCT_CATEGORY + " = ?", new String[]{category});
    }


    // Get product by name (search by product name)
    public Cursor getProductByName(String productName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COL_PRODUCT_NAME + " LIKE ?", new String[]{"%" + productName + "%"});
    }


    // Delete product by name
    public void deleteProductByName(String productName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COL_PRODUCT_NAME + " = ?", new String[]{productName});
        db.close();
    }


    public boolean addToCart(String productName, double productPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CART_PRODUCT_NAME, productName);
        values.put(COL_CART_PRODUCT_PRICE, productPrice);

        long result = db.insert(TABLE_CART, null, values);
        return result != -1;
    }
    public Cursor getCartItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CART, null, null, null, null, null, null);
    }




}