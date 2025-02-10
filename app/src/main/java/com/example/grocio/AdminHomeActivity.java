package com.example.grocio;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Button btnInsertProduct = findViewById(R.id.btn_insert_product);
        Button btnViewProduct = findViewById(R.id.btn_view_product);
        Button btnUserView = findViewById(R.id.user_view); // New button

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_color));

        btnInsertProduct.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, InsertProductActivity.class);
            startActivity(intent);
        });

        btnViewProduct.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, ViewProductActivity.class);
            startActivity(intent);
        });

        btnUserView.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }

}
