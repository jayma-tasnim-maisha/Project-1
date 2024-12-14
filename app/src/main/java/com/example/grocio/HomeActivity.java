package com.example.grocio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private Button shopNowBtn, ordersBtn, settingsBtn, logoutBtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize buttons
        shopNowBtn = findViewById(R.id.shop_now_btn);
        ordersBtn = findViewById(R.id.orders_btn);
        settingsBtn = findViewById(R.id.settings_btn);
        logoutBtn = findViewById(R.id.logout_btn);

        // Shop Now button click
        shopNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, ShopActivity.class);  // Create ShopActivity for shopping
//                startActivity(intent);
            }
        });

        // Orders button click
        ordersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, OrdersActivity.class);  // Create OrdersActivity for order viewing
//                startActivity(intent);
            }
        });

        // Settings button click
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);  // Create SettingsActivity
//                startActivity(intent);
            }
        });

        // Logout button click
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();  // Sign out from Firebase
                Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
