package com.example.grocio;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductAdapter extends CursorAdapter {

    private boolean isAdmin;
    private DatabaseHelper databaseHelper;

    public ProductAdapter(Context context, Cursor cursor, boolean isAdmin) {
        super(context, cursor, 0);
        this.isAdmin = isAdmin;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Load different layouts for admin and user
        if (isAdmin) {
            return inflater.inflate(R.layout.list_item_product, parent, false);
        } else {
            return inflater.inflate(R.layout.list_item_product_user, parent, false);
        }
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find views
        TextView nameTextView = view.findViewById(R.id.text_view_product_name);
        TextView priceTextView = view.findViewById(R.id.text_view_product_price);
        TextView quantityTextView = view.findViewById(R.id.text_view_product_quantity);
        TextView descriptionTextView = view.findViewById(R.id.text_view_product_details);  // New
        TextView categoryTextView = view.findViewById(R.id.text_view_product_category);  // New
        ImageView productImageView = view.findViewById(R.id.image_view_product);

        // Get product data from the cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_NAME));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_DESCRIPTION));  // New
        String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_CATEGORY));  // New
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_PRICE));
        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_QUANTITY));
        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_IMAGE_URI));

        // Set data to TextViews and ImageView
        nameTextView.setText(name);
        priceTextView.setText("Price: " + price + " Tk");
        quantityTextView.setText("Quantity: " + quantity);
        descriptionTextView.setText("Details: " + description);  // New
        categoryTextView.setText("Category: " + category);  // New

        // Decode image byte array to Bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        productImageView.setImageBitmap(bitmap);

        // If admin, show Update and Delete buttons
        if (isAdmin) {
            Button buttonUpdate = view.findViewById(R.id.button_update);
            Button buttonDelete = view.findViewById(R.id.button_delete);

            final String productName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_NAME));

            // Set click listeners for update and delete buttons
            buttonUpdate.setOnClickListener(v -> {
                Intent updateIntent = new Intent(context, UpdateProductActivity.class);
                updateIntent.putExtra("product_id", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)));
                updateIntent.putExtra("product_name", name);
                updateIntent.putExtra("product_description", description);  // New
                updateIntent.putExtra("product_category", category);  // New
                updateIntent.putExtra("product_price", price);
                updateIntent.putExtra("product_quantity", quantity);
                updateIntent.putExtra("product_image", imageBytes);
                context.startActivity(updateIntent);
            });

            buttonDelete.setOnClickListener(v -> {
                boolean deleted = deleteProductByName(productName);
                if (deleted) {
                    Toast.makeText(context, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                    ((ViewProductActivity) context).displayProducts();
                } else {
                    Toast.makeText(context, "Failed to delete product", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // For regular users, show "Add to Cart" button
            Button addToCartButton = view.findViewById(R.id.addToCartButton);
            addToCartButton.setOnClickListener(v -> {
                Product product = new Product(name, BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length), price);

              //  CartManager.getInstance().addProduct(product);
                String message = name + " has been added to your cart!";
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                // Optionally, store the product in SharedPreferences, SQLite, or an in-memory cart
                addToCart(name, price);
            });
        }
    }

    private boolean deleteProductByName(String productName) {
        databaseHelper.deleteProductByName(productName);
        return true;
    }

    // Example method to add product to the cart
    private void addToCart(String productName, double productPrice) {
        // You can store the product data in SharedPreferences or a database.
        // Example: For simplicity, we show a Toast message.
        // Replace this with your actual cart handling logic.
    }
}