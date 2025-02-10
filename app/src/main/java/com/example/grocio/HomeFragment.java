package com.example.grocio;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.grocio.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Click listeners for all categories
        binding.vegetableCategoryCard.setOnClickListener(v -> openCategory("Vegetable"));
        binding.fruitCategoryCard.setOnClickListener(v -> openCategory("Fruit"));
        binding.meatCategoryCard.setOnClickListener(v -> openCategory("Meat"));
        binding.fishCategoryCard.setOnClickListener(v -> openCategory("Fish"));
        binding.breadCategoryCard.setOnClickListener(v -> openCategory("Bread"));
        binding.dairyCategoryCard.setOnClickListener(v -> openCategory("Snacks"));

        return binding.getRoot();
    }

    // Method to open product display based on category
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
