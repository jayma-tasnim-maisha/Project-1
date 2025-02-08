package com.example.grocio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnSignin;
    private TextView tvSignUp;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_color));

        // Check if the user is already logged in
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // If logged in, redirect to HomeActivity
            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnSignin = findViewById(R.id.btn_signin);
        tvSignUp = findViewById(R.id.tv_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
            startActivity(intent);  // Start RegisterActivity
        });

        btnSignin.setOnClickListener(v -> {

            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Check if the entered username and password are for the admin
            if (username.equals("admin") && password.equals("admin")) {
                Intent intent = new Intent(SignInActivity.this, AdminHomeActivity.class);
                startActivity(intent);
                finish();
                return; // Exit early to prevent Firebase login attempt
            }

            if (validateInputs(username, password)) {
                signInWithFirebase(username, password);
            } else {
                Toast.makeText(SignInActivity.this, "Please enter valid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs(String username, String password) {

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Enter Username/Email");
            return false;
        }

        if (!isValidUsername(username)) {
            etUsername.setError("Invalid Username/Email");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Enter Password");
            return false;
        }

        if (!isValidPassword(password)) {
            etPassword.setError("Invalid Password");
            return false;
        }

        return true;
    }

    private boolean isValidUsername(String username) {
        return username.matches("^[A-Za-z][A-Za-z0-9]*$") || Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6 && password.matches(".*[a-zA-Z].*") && password.matches(".*\\d.*");
    }

    private void signInWithFirebase(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        // Saving login state in SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true); // Save login state as true
                        editor.apply();

                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(SignInActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(SignInActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}