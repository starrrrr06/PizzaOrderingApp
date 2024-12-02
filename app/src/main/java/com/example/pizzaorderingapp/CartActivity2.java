package com.example.pizzaorderingapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class CartActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity2);

        // Get the cart items from the CartManager
        List<String> cartItems = CartManager.getInstance().getCartItems();

        // Set up a ListView to display the cart items
        ListView cartListView = findViewById(R.id.cart_list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, cartItems);
        cartListView.setAdapter(adapter);

        // Set up the OnClickListener for the clear button
        Button clearButton = findViewById(R.id.clear);
        clearButton.setOnClickListener(v -> {
            // Clear the cart in the CartManager
            CartManager.getInstance().clearCart();

            // Notify the adapter of the changes
            adapter.clear();
            adapter.notifyDataSetChanged();

            // Show a feedback message
            Toast.makeText(CartActivity2.this, "Cart cleared", Toast.LENGTH_SHORT).show();
        });
    }
}