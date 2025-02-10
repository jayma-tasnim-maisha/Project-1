package com.example.grocio;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextView userNameTextView, userEmailTextView;
    private Button editProfileButton, logoutButton;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);



        mAuth = FirebaseAuth.getInstance();

        userNameTextView = view.findViewById(R.id.user_name);
        userEmailTextView = view.findViewById(R.id.user_email);
        editProfileButton = view.findViewById(R.id.edit_profile_button);
        logoutButton = view.findViewById(R.id.logout_button);

        // Load and display user data
        loadUserData();

        // Open EditProfileActivity
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        // Logout
        logoutButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();

            Intent intent = new Intent(getActivity(), SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserProfile", MODE_PRIVATE);

        // Get saved username
        String savedUsername = sharedPreferences.getString("username", null);

        if (savedUsername != null && !savedUsername.isEmpty()) {
            userNameTextView.setText(savedUsername);
        } else if (currentUser != null) {
            String defaultUserName = (currentUser.getDisplayName() != null) ? currentUser.getDisplayName() : currentUser.getEmail().split("@")[0];
            userNameTextView.setText(defaultUserName);
        } else {
            userNameTextView.setText("Guest");
        }

        if (currentUser != null) {
            userEmailTextView.setText(currentUser.getEmail());
        } else {
            userEmailTextView.setText("No email available");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();  // Refresh username when returning from EditProfileActivity
    }
}
