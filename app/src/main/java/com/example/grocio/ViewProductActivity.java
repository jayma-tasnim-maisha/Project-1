package com.example.grocio;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

public class ViewProductActivity extends AppCompatActivity {
    private ListView listViewProducts;
    private DatabaseHelper databaseHelper;
    private ProductAdapter productAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_color));

        listViewProducts = findViewById(R.id.list_view_products);
        searchView = findViewById(R.id.search_view);
        databaseHelper = new DatabaseHelper(this);

        // Initialize the adapter with all products
        displayProducts();

        // Setup search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the action when search is submitted
                searchProduct(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter products as the user types
                searchProduct(newText);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the displayed products
        displayProducts();
    }

    public void displayProducts() {
        Cursor cursor = databaseHelper.getAllProducts();
        productAdapter = new ProductAdapter(this, cursor, true);
        listViewProducts.setAdapter(productAdapter);
    }

    private void searchProduct(String query) {
        Cursor cursor = databaseHelper.getProductByName(query); // Search by name
        productAdapter = new ProductAdapter(this, cursor, true);
        listViewProducts.setAdapter(productAdapter);
    }
}