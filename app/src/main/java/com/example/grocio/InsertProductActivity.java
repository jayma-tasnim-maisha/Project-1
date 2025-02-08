package com.example.grocio;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InsertProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Spinner categorySpinner;
    private EditText productNameEditText, productPriceEditText, productQuantityEditText;
    private ImageView selectedImageView;
    private Button selectImageButton, insertProductButton;
    private Uri imageUri; // Holds the selected image URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_product);

        // Initialize views
        categorySpinner = findViewById(R.id.spinner_category);
        productNameEditText = findViewById(R.id.et_product_name);
        productPriceEditText = findViewById(R.id.et_product_price);
        productQuantityEditText = findViewById(R.id.et_product_quantity);
        selectedImageView = findViewById(R.id.iv_selected_image);
        selectImageButton = findViewById(R.id.btn_select_image);
        insertProductButton = findViewById(R.id.btn_insert_product);

        // Set up Spinner with category options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Set click listener for image selection
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

        // Set click listener for inserting product
        insertProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertProduct();
            }
        });
    }

    // Method to open the image chooser
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    // Handle the image selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            selectedImageView.setImageURI(imageUri);
        }
    }

    // Method to handle inserting the product
    private void insertProduct() {
        String category = categorySpinner.getSelectedItem().toString();
        String productName = productNameEditText.getText().toString().trim();
        String productPrice = productPriceEditText.getText().toString().trim();
        String productQuantity = productQuantityEditText.getText().toString().trim();

        // Validate user input
        if (productName.isEmpty() || productPrice.isEmpty() || productQuantity.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Optionally: Check if an image is selected
        if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert the product (here you can handle saving the data to a database, etc.)
        // Example: Showing a success message
        Toast.makeText(this, "Product inserted successfully", Toast.LENGTH_SHORT).show();

        // Clear the form for the next product insertion
        clearForm();
    }

    // Method to clear the form after inserting the product
    private void clearForm() {
        productNameEditText.setText("");
        productPriceEditText.setText("");
        productQuantityEditText.setText("");
        selectedImageView.setImageResource(R.drawable.upload_icon); // Reset the image to default icon
        categorySpinner.setSelection(0); // Reset spinner selection
        imageUri = null; // Reset image URI
    }
}
