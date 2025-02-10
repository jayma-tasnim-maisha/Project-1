package com.example.grocio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImage;
    private EditText editUsername;
    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_color));

        profileImage = findViewById(R.id.profile_image);
        editUsername = findViewById(R.id.edit_username);
        Button changeProfileButton = findViewById(R.id.change_profile_picture_button);
        Button saveButton = findViewById(R.id.save_button);
        Button cancelButton = findViewById(R.id.cancel_button);

        sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE);

        // Load saved username and profile image
        loadProfileData();

        // Change profile picture
        changeProfileButton.setOnClickListener(v -> openGallery());

        // Save profile changes
        saveButton.setOnClickListener(v -> saveProfile());

        // Cancel and return
        cancelButton.setOnClickListener(v -> finish());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
                saveProfileImage(imageUri.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveProfile() {
        String username = editUsername.getText().toString().trim();
        if (!username.isEmpty()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);
            editor.apply();
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProfileImage(String imageUri) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profile_image", imageUri);
        editor.apply();
    }

    private void loadProfileData() {
        String savedUsername = sharedPreferences.getString("username", "");
        String savedImageUri = sharedPreferences.getString("profile_image", "");

        editUsername.setText(savedUsername);
        if (!savedImageUri.isEmpty()) {
            profileImage.setImageURI(Uri.parse(savedImageUri));
        }
    }
}
