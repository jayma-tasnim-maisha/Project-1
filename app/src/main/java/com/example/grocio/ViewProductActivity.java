package com.example.grocio;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewProductActivity extends AppCompatActivity {

    private ListView listView;
    private ProductAdapter productAdapter;
    private DatabaseHelper databaseHelper;
    private String category = "Vegetable"; // Example category; change as needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        listView = findViewById(R.id.product_list_view);
        databaseHelper = new DatabaseHelper(this);

        // Get products by category from the database
        Cursor cursor = databaseHelper.getProductsByCategory(category);

        // Set up the adapter to display the products in the ListView
        productAdapter = new ProductAdapter(this, cursor, 0); // Cursor is passed here
        listView.setAdapter(productAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (productAdapter != null) {
            productAdapter.changeCursor(null); // Close the cursor when activity is destroyed
        }
    }
}