package com.example.grocio;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.grocio.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // When vegetable category is clicked
        binding.vegetableCategoryCard.setOnClickListener(v -> {
            openCategory("Vegetables");
        });

        // When fruit category is clicked
        binding.FruitCategoryCard.setOnClickListener(v -> {
            openCategory("Fruits");
        });

        // When fish category is clicked
        binding.FishCategoryCard.setOnClickListener(v -> {
            openCategory("Fish");
        });

        return binding.getRoot();
    }

    // Method to open the product list for a specific category
    private void openCategory(String category) {
        Intent intent = new Intent(getActivity(), ProductDisplayActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}