package com.example.grocio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version
    public static final String DATABASE_NAME = "grocio.db";
    public static final int DATABASE_VERSION = 1;

    // Table and Column Names
    public static final String TABLE_PRODUCTS = "products";
    public static final String COL_ID = "_id";
    public static final String COL_PRODUCT_NAME = "productName";
    public static final String COL_PRODUCT_PRICE = "productPrice";
    public static final String COL_PRODUCT_CATEGORY = "productCategory";
    public static final String COL_PRODUCT_QUANTITY = "productQuantity";
    public static final String COL_PRODUCT_IMAGE_URI = "productImageUri";

    // SQL Create Table Statement
    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_PRODUCT_NAME + " TEXT, "
            + COL_PRODUCT_PRICE + " REAL, "
            + COL_PRODUCT_CATEGORY + " TEXT, "
            + COL_PRODUCT_QUANTITY + " INTEGER, "
            + COL_PRODUCT_IMAGE_URI + " TEXT)";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // OnCreate: Called when the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRODUCTS);
    }

    // OnUpgrade: Called when the database version is upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    // Method to insert a new product
    public long insertProduct(String name, double price, String category, int quantity, String imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PRODUCT_NAME, name);
        values.put(COL_PRODUCT_PRICE, price);
        values.put(COL_PRODUCT_CATEGORY, category);
        values.put(COL_PRODUCT_QUANTITY, quantity);
        values.put(COL_PRODUCT_IMAGE_URI, imageUri);
        return db.insert(TABLE_PRODUCTS, null, values);
    }

    // Method to get products by category (returns a Cursor)
    public Cursor getProductsByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COL_PRODUCT_CATEGORY + " = ?";
        return db.rawQuery(query, new String[]{category});
    }

    // Method to get all products (returns a Cursor)
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS;
        return db.rawQuery(query, null);
    }
}
