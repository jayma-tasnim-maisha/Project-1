package com.example.grocio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UpdateProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextProductName, editTextProductPrice, editTextProductQuantity, productDescriptionEditText;
    private Spinner categorySpinner;
    private ImageView imageViewProduct;
    private Button buttonUpdateProduct, buttonSelectImage;

    private DatabaseHelper databaseHelper;
    private String productId, selectedCategory;
    private byte[] productImageByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_color));

        // Initialize views
        editTextProductName = findViewById(R.id.edit_text_product_name);
        editTextProductPrice = findViewById(R.id.edit_text_product_price);
        editTextProductQuantity = findViewById(R.id.edit_text_product_quantity);
        productDescriptionEditText = findViewById(R.id.edit_text_product_description);
        categorySpinner = findViewById(R.id.spinner_category);
        imageViewProduct = findViewById(R.id.image_view_product);
        buttonUpdateProduct = findViewById(R.id.button_update_product);
        buttonSelectImage = findViewById(R.id.button_select_image);  // Image selection button

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Get product details from Intent
        Intent intent = getIntent();
        productId = intent.getStringExtra("product_id");
        String productName = intent.getStringExtra("product_name");
        String productDescription = intent.getStringExtra("product_description");
        String productCategory = intent.getStringExtra("product_category");
        double productPrice = intent.getDoubleExtra("product_price", 0);
        int productQuantity = intent.getIntExtra("product_quantity", 0);
        productImageByteArray = intent.getByteArrayExtra("product_image");

        // Set the data to the EditText fields and ImageView
        editTextProductName.setText(productName);
        productDescriptionEditText.setText(productDescription);
        editTextProductPrice.setText(String.valueOf(productPrice));
        editTextProductQuantity.setText(String.valueOf(productQuantity));

        // Populate category spinner using ArrayAdapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.category_array,  // Defined in strings.xml
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Convert array from resources to a List
        List<String> categories = Arrays.asList(getResources().getStringArray(R.array.category_array));

        // Set spinner selection to match the existing product category
        if (categories.contains(productCategory)) {
            categorySpinner.setSelection(categories.indexOf(productCategory));
        }

        // Handle category selection
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = "Select Category"; // Default category
            }
        });

        // Display image if available
        if (productImageByteArray != null) {
            imageViewProduct.setImageBitmap(BitmapFactory.decodeByteArray(productImageByteArray, 0, productImageByteArray.length));
        }

        // Set image selection functionality
        buttonSelectImage.setOnClickListener(v -> selectImage());

        // Set the update button functionality
        buttonUpdateProduct.setOnClickListener(v -> {
            if (productId == null || productId.isEmpty()) {
                Toast.makeText(this, "Invalid Product ID", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get updated data from input fields
            String updatedProductName = editTextProductName.getText().toString().trim();
            String updatedProductDescription = productDescriptionEditText.getText().toString().trim();
            String updatedProductCategory = selectedCategory;
            double updatedProductPrice;
            int updatedProductQuantity;

            // Validate price and quantity input
            try {
                updatedProductPrice = Double.parseDouble(editTextProductPrice.getText().toString().trim());
                updatedProductQuantity = Integer.parseInt(editTextProductQuantity.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid price or quantity!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ensure productImageByteArray is not null before updating
            if (productImageByteArray == null) {
                Toast.makeText(this, "Warning: No image found for this product.", Toast.LENGTH_SHORT).show();
            }

            // Update product in the database
            boolean isUpdated = databaseHelper.updateProduct(
                    Integer.parseInt(productId),
                    updatedProductName,
                    updatedProductDescription,
                    updatedProductCategory,
                    updatedProductPrice,
                    updatedProductQuantity,
                    productImageByteArray
            );

            if (isUpdated) {
                Toast.makeText(this, "Product updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update failed! No changes detected.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageViewProduct.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                productImageByteArray = byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }}