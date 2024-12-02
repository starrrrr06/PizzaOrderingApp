package com.example.pizzaorderingapp;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    // Singleton instance
    private static CartManager instance;

    // Cart items
    private List<String> cartItems;

    // Private constructor
    private CartManager() {
        cartItems = new ArrayList<>();
    }

    // Method to get the singleton instance
    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Add item to the cart
    public void addToCart(String item) {
        cartItems.add(item);
    }

    // Get all cart items
    public List<String> getCartItems() {
        return cartItems;
    }

    // Clear all items in the cart
    public void clearCart() {
        cartItems.clear();
    }
}