package com.example.grocio;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegisterName, etRegisterEmail, etRegisterPassword, etConfirmPassword, etRegisterPhone;
    private Button btnSignupLogin, btnSignupRegister;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegisterName = findViewById(R.id.et_register_name);
        etRegisterEmail = findViewById(R.id.et_register_email);
        etRegisterPassword = findViewById(R.id.et_register_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        etRegisterPhone = findViewById(R.id.et_register_phone);

        btnSignupLogin = findViewById(R.id.btn_sign_up_login);
        btnSignupRegister = findViewById(R.id.btn_sign_up_register);

        // Initializing FirebaseAuth instance for user authentication
        firebaseAuth = FirebaseAuth.getInstance();

        btnSignupRegister.setOnClickListener(v -> {
            if (validateInputs()) {
                String email = etRegisterEmail.getText().toString();
                String password = etRegisterPassword.getText().toString();

                // Creating a new user in Firebase using email and password
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {

                                    // Sending email verification after successful registration
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(verificationTask -> {
                                                if (verificationTask.isSuccessful()) {
                                                    Toast.makeText(RegisterActivity.this,
                                                            "Registration successful! Please check your email for verification.",
                                                            Toast.LENGTH_SHORT).show();

                                                    // Sign out the user and redirect to SignInActivity
                                                    firebaseAuth.signOut();
                                                    startActivity(new Intent(RegisterActivity.this, SignInActivity.class));
                                                    finish();
                                                }
                                                else {
                                                    Toast.makeText(RegisterActivity.this,
                                                            "Failed to send verification email: " + verificationTask.getException().getMessage(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(RegisterActivity.this,
                                        "Registration failed: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        btnSignupLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateInputs() {
        String name = etRegisterName.getText().toString();
        String email = etRegisterEmail.getText().toString();
        String password = etRegisterPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String phone = etRegisterPhone.getText().toString();

        if (TextUtils.isEmpty(name) || !isValidName(name)) {
            etRegisterName.setError("Invalid Name");
            return false;
        }

        if (TextUtils.isEmpty(email) || !isValidEmail(email)) {
            etRegisterEmail.setError("Invalid Email");
            return false;
        }

        if (!isValidPassword(password)) {
            etRegisterPassword.setError("Password must be at least 6 characters");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return false;
        }

        if (TextUtils.isEmpty(phone) || !isValidPhone(phone)) {
            etRegisterPhone.setError("Invalid Phone Number");
            return false;
        }

        return true;
    }

    private boolean isValidName(String name) {
        return name.matches("^([A-Z][a-z]+)(\\s[A-Z][a-z]+)*$");
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*\\d).{6,}$"); // Password must be at least 6 characters long with at least one digit
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("^(\\+8801[3-9]|01[3-9])[\\d]{8}$");
    }
}
