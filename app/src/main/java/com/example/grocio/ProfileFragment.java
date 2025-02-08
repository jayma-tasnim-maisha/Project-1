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

    // Firebase Authentication instance
    private FirebaseAuth mAuth;

    // Views for displaying the user's data
    private TextView userNameTextView;
    private TextView userEmailTextView;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Find views from layout
        userNameTextView = view.findViewById(R.id.user_name);
        userEmailTextView = view.findViewById(R.id.user_email);
        editProfileButton = view.findViewById(R.id.edit_profile_button);
        logoutButton = view.findViewById(R.id.logout_button);

        // Get the current logged-in user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // Get user details
            String userName = currentUser.getDisplayName();
            String userEmail = currentUser.getEmail();

            // If userName is null, fall back to the email's first part as a name
            if (userName == null || userName.isEmpty()) {
                userName = userEmail.split("@")[0];
            }

            // Display user information in the TextViews
            userNameTextView.setText(userName);
            userEmailTextView.setText(userEmail);
        } else {
            // No user is logged in
            Toast.makeText(getActivity(), "No user is logged in", Toast.LENGTH_SHORT).show();
        }

        // Set the Edit Profile button functionality
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        // Set the Logout button functionality
        // Set the Logout button functionality
        logoutButton.setOnClickListener(v -> {
            // Clear the login state from SharedPreferences
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false); // Reset login state
            editor.apply();

            // Navigate back to SignInActivity and clear the back stack
            Intent intent = new Intent(getActivity(), SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            // Close the current activity
            requireActivity().finish();  // Correct way to finish the hosting activity in a fragment
        });



        return view;
    }
}
