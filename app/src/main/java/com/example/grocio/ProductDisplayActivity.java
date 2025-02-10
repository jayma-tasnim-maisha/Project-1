package com.example.grocio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDisplayActivity extends AppCompatActivity {

    private ListView listViewProducts;
    private DatabaseHelper databaseHelper;
    private String category;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);

        listViewProducts = findViewById(R.id.product_list);
        databaseHelper = new DatabaseHelper(this);

        // Get category from intent
        Intent intent = getIntent();
        category = intent.getStringExtra("category");

        // Set category title
        TextView categoryTitle = findViewById(R.id.categoryTitle);
        categoryTitle.setText(category);

        // Load products for the selected category
        loadProducts();
    }

    private void loadProducts() {
        Cursor cursor = databaseHelper.getProductsByCategory(category);
        ProductAdapter adapter = new ProductAdapter(this, cursor, false);
        listViewProducts.setAdapter(adapter);
    }
}