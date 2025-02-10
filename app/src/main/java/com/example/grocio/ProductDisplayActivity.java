package com.example.grocio;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class ProductDisplayActivity extends AppCompatActivity {

    private ListView listViewProducts;
    private DatabaseHelper databaseHelper;
    private String category;
    private ProductAdapter productAdapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_color));

        listViewProducts = findViewById(R.id.listViewProducts);
        databaseHelper = new DatabaseHelper(this);

        // Get category from intent
        Intent intent = getIntent();
        category = intent.getStringExtra("category");


        // Load products from database
        loadProducts();
    }

    private void loadProducts() {
        // Fetch products for the selected category
        cursor = databaseHelper.getProductsByCategory(category);

        if (cursor != null) {
            productAdapter = new ProductAdapter(this, cursor, false); // false for regular users
            listViewProducts.setAdapter(productAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
    }
}
