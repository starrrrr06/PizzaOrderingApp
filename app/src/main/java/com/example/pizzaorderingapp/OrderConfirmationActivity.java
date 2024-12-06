package com.example.pizzaorderingapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderConfirmationActivity extends AppCompatActivity {

    private TextView customerNameTextView, customerAddressTextView, customerContactTextView, orderTotalTextView;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize Views
        customerNameTextView = findViewById(R.id.customerNameTextView);
        customerAddressTextView = findViewById(R.id.customerAddressTextView);
        customerContactTextView = findViewById(R.id.customerContactTextView);
        orderTotalTextView = findViewById(R.id.orderTotalTextView);

        // Retrieve Data Passed via Intent
        String totalPrice = getIntent().getStringExtra("TOTAL_PRICE");

        // Display the order total
        orderTotalTextView.setText("₱" + totalPrice);

        // Fetch User Data from Firestore
        loadUserData();
    }

    private void loadUserData() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve user data from Firestore
                        String fullName = documentSnapshot.getString("fullName");
                        String contactNumber = documentSnapshot.getString("contactNumber");
                        String homeAddress = documentSnapshot.getString("homeAddress");

                        // Populate the TextViews
                        customerNameTextView.setText(fullName);
                        customerAddressTextView.setText(homeAddress);
                        customerContactTextView.setText(contactNumber);
                    } else {
                        Toast.makeText(OrderConfirmationActivity.this, "Profile data not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrderConfirmationActivity.this, "Error loading profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
