package com.example.grocio;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private EditText etUsername,etPassword;
    private Button btnLogin,btnRegister;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        firebaseAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(v-> {
            Intent intent = new Intent(SignInActivity.this,RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v-> {

            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (validateInputs(username, password)) {
                signInWithFirebase(username, password);
            } else {
                Toast.makeText(SignInActivity.this,"Please enter valid credentials",Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class); // Replace HomePageActivity with your actual home page activity
                        startActivity(intent);
                        finish();
                        Toast.makeText(SignInActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(SignInActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
