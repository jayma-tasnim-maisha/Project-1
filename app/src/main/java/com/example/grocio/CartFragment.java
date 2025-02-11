package com.example.grocio;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private TextView totalPriceTextView;
    private Button checkoutButton;
    private ImageView backButton;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartRecyclerView = view.findViewById(R.id.cart_recycler_view);
        totalPriceTextView = view.findViewById(R.id.total_price);
        checkoutButton = view.findViewById(R.id.checkout_button);
        backButton = view.findViewById(R.id.back_button);

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        updateCart();

        // Back button functionality
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        // Checkout button functionality
        checkoutButton.setOnClickListener(v -> {
            if (CartManager.getInstance(requireContext()).getCartItems().isEmpty()) {
                Toast.makeText(requireContext(), "Your cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Proceeding to checkout!", Toast.LENGTH_SHORT).show();
                // Implement checkout logic here
            }
        });
    }

    private void updateCart() {
        List<Product> cartItems = CartManager.getInstance(requireContext()).getCartItems();
        cartAdapter = new CartAdapter(requireContext(), cartItems, this::updateTotalPrice);
        cartRecyclerView.setAdapter(cartAdapter);
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double totalPrice = 0.0;
        for (Product product : CartManager.getInstance(requireContext()).getCartItems()) {
            totalPrice += product.getPrice();
        }
        totalPriceTextView.setText("Total: " + totalPrice + " Tk");
    }
}
