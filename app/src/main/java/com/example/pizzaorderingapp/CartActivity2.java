package com.example.pizzaorderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;

public class CartActivity2 extends AppCompatActivity {

    private TextView totalPriceTextView;
    private ListView cartListView;
    private CartManager cartManager;
    private HashMap<String, Double> pizzaPrices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity2);

        cartManager = CartManager.getInstance();
        pizzaPrices = new HashMap<>();
        pizzaPrices.put(HomeScreenActivity.MENU_PEPPERONI, 499.0);
        pizzaPrices.put(HomeScreenActivity.MENU_HAWAIIAN, 399.0);
        pizzaPrices.put(HomeScreenActivity.MENU_CHEESE, 399.0);
        pizzaPrices.put(HomeScreenActivity.MENU_VEGETABLE, 299.0);
        pizzaPrices.put(HomeScreenActivity.MENU_SPINACH, 399.0);
        pizzaPrices.put(HomeScreenActivity.MENU_ALLMEAT, 699.0);

        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        cartListView = findViewById(R.id.cart_list_view);

        // Get the cart items from the CartManager
        List<String> cartItems = CartManager.getInstance().getCartItems();

        // Set up a ListView to display the cart items
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, cartItems);
        cartListView.setAdapter(adapter);

        // Calculate and display the total price
        updateTotalPrice(cartItems);

        // Set up the OnClickListener for the clear button
        Button clearButton = findViewById(R.id.clear);
        clearButton.setOnClickListener(v -> {
            // Clear the cart in the CartManager
            CartManager.getInstance().clearCart();

            // Notify the adapter of the changes
            adapter.clear();
            adapter.notifyDataSetChanged();

            // Update the total price to 0
            updateTotalPrice(CartManager.getInstance().getCartItems());

            // Show a feedback message
            Toast.makeText(CartActivity2.this, "Cart cleared", Toast.LENGTH_SHORT).show();
        });

        // Set up the proceed to Order Confirmation button
        Button proceedButton = findViewById(R.id.make_order);
        proceedButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(CartActivity2.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Calculate the total price
            double finalTotalPrice = 0.0;
            for (String item : cartItems) {
                finalTotalPrice += pizzaPrices.getOrDefault(item, 0.0);
            }

            // Navigate to the Order Confirmation activity
            Intent intent = new Intent(CartActivity2.this, OrderConfirmationActivity.class);
            intent.putExtra("TOTAL_PRICE", finalTotalPrice);
            startActivity(intent);
        });
    }

    // Method to update the total price based on the cart items
    private void updateTotalPrice(List<String> cartItems) {
        double totalPrice = 0.0;
        for (String item : cartItems) {
            totalPrice += pizzaPrices.getOrDefault(item, 0.0);
        }
        // Update the total price TextView
        totalPriceTextView.setText("Total: ₱" + String.format("%.2f", totalPrice));
    }
}
