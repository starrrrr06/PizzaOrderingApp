package com.example.pizzaorderingapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderConfirmationActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 100;
    private static final String SHOP_OWNER_PHONE = "09362288466"; // Replace with shop owner's phone number

    // Firebase instances
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // UI components
    public TextView fullNameTextView;
    public TextView customerAddressTextView;
    public TextView customerContactTextView;
    public TextView orderTotalTextView;
    public Button backToHomeButton;
    public Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        // Initialize Firebase and UI components
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        fullNameTextView = findViewById(R.id.customerNameTextView);
        customerAddressTextView = findViewById(R.id.customerAddressTextView);
        customerContactTextView = findViewById(R.id.customerContactTextView);
        orderTotalTextView = findViewById(R.id.orderTotalTextView);
        backToHomeButton = findViewById(R.id.backToHomeButton);
        checkoutButton = findViewById(R.id.checkoutButton);

        // Fetch and display order details
        loadOrderDetails();

        // Back to Home button
        backToHomeButton.setOnClickListener(v -> finish());

        // Checkout button
        checkoutButton.setOnClickListener(v -> {
            String totalText = orderTotalTextView.getText().toString().replace("₱", "");
            double totalPrice = Double.parseDouble(totalText);

            if (totalPrice <= 0) {
                Toast.makeText(this, "No items to checkout!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Order has been confirmed and is being processed!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void loadOrderDetails() {
        String userId = mAuth.getCurrentUser().getUid();

        // Fetch user and order data from Firestore
        db.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Fetch user details
                        String fullName = documentSnapshot.getString("fullName");
                        String contactNumber = documentSnapshot.getString("contactNumber");
                        String homeAddress = documentSnapshot.getString("homeAddress");

                        // Fetch order total (mock value or Firestore integration for dynamic total)
                        double orderTotal = getIntent().getDoubleExtra("TOTAL_PRICE", 500.0);

                        // Display details in TextViews
                        fullNameTextView.setText(fullName != null ? fullName : "Name not provided");
                        customerAddressTextView.setText(homeAddress != null ? homeAddress : "Address not provided");
                        customerContactTextView.setText(contactNumber != null ? contactNumber : "Contact not provided");
                        orderTotalTextView.setText("₱" + String.format("%.2f", orderTotal));

                        // Optionally send SMS
                        sendOrderDetailsViaSms(fullName, homeAddress, contactNumber, String.format("₱%.2f", orderTotal));
                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void sendOrderDetailsViaSms(String name, String address, String contact, String total) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        } else {
            String message = "Order Details:\n"
                    + "Customer Name: " + (name != null ? name : "N/A") + "\n"
                    + "Address: " + (address != null ? address : "N/A") + "\n"
                    + "Contact: " + (contact != null ? contact : "N/A") + "\n"
                    + "Total: " + total;

            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(SHOP_OWNER_PHONE, null, message, null, null);
                Toast.makeText(this, "Order details sent via SMS", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}