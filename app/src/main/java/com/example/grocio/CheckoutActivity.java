package com.example.grocio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private EditText nameInput, addressInput, phoneInput;
    private TextView totalPriceTextView;
    private RadioGroup paymentMethodGroup;
    private Button confirmOrderButton;
    private ImageView backButton;
    private double totalAmount = 0.0; // Store total amount

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_color));

        // Initialize UI elements
        nameInput = findViewById(R.id.name_input);
        addressInput = findViewById(R.id.address_input);
        phoneInput = findViewById(R.id.phone_input);
        totalPriceTextView = findViewById(R.id.checkout_total_price);
        paymentMethodGroup = findViewById(R.id.payment_method_group);
        confirmOrderButton = findViewById(R.id.confirm_order_button);
        backButton = findViewById(R.id.back_button);

        // Retrieve total amount from the cart
        totalAmount = calculateTotalAmount();
        totalPriceTextView.setText("Total: " + totalAmount + " Tk");

        // Back button action
        backButton.setOnClickListener(v -> finish());

        // Confirm Order button action
        confirmOrderButton.setOnClickListener(v -> confirmOrder());
    }

    private double calculateTotalAmount() {
        List<Product> cartItems = CartManager.getInstance(this).getCartItems();
        double total = 0.0;
        for (Product product : cartItems) {
            total += product.getPrice();
        }
        return total;
    }

    private void confirmOrder() {
        String name = nameInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();

        if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPaymentMethodId = paymentMethodGroup.getCheckedRadioButtonId();
        if (selectedPaymentMethodId == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedPaymentMethod = findViewById(selectedPaymentMethodId);
        String paymentMethod = selectedPaymentMethod.getText().toString();

        // Here, you can implement order storage or API call to process order
        Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_SHORT).show();

        // Clear the cart after successful order
        CartManager.getInstance(this).clearCart();

        // Redirect to order confirmation page or home
        Intent intent = new Intent(CheckoutActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
