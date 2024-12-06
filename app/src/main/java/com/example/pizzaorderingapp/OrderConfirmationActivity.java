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

public class OrderConfirmationActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 100;
    private static final String SHOP_OWNER_PHONE = "09362288466"; // Replace with shop owner's phone number

    private TextView full_name;
    private TextView customerAddressTextView;
    private TextView customerContactTextView;
    private TextView orderTotalTextView;
    private Button backToHomeButton;
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        full_name = findViewById(R.id.customerNameTextView);
        customerAddressTextView = findViewById(R.id.customerAddressTextView);
        customerContactTextView = findViewById(R.id.customerContactTextView);
        orderTotalTextView = findViewById(R.id.orderTotalTextView);
        backToHomeButton = findViewById(R.id.backToHomeButton);
        checkoutButton = findViewById(R.id.checkoutButton); // New checkout button

        // Get data from intent (if passed from previous activity)
        String customerName = getIntent().getStringExtra("customer_name");
        String customerAddress = getIntent().getStringExtra("customer_address");
        String customerContact = getIntent().getStringExtra("customer_contact");
        String orderTotal = getIntent().getStringExtra("order_total");

        // Ensure total price is fetched from CartActivity2
        double totalPrice = getIntent().getDoubleExtra("TOTAL_PRICE", 0.0);
        if (orderTotal == null) {
            orderTotal = "₱" + String.format("%.2f", totalPrice); // Default to calculated total price
        }

        // Set data to TextViews
        full_name.setText(customerName != null ? customerName : "John Doe");
        customerAddressTextView.setText(customerAddress != null ? customerAddress : "123 Main Street, Cityville");
        customerContactTextView.setText(customerContact != null ? customerContact : "+1234567890");
        orderTotalTextView.setText(orderTotal);

        // Send SMS
        sendOrderDetailsViaSms(customerName, customerAddress, customerContact, orderTotal);

        // Handle back to home button click
        backToHomeButton.setOnClickListener(v -> finish());

        // Handle checkout button click
        checkoutButton.setOnClickListener(v -> {
            if (totalPrice <= 0) {
                Toast.makeText(this, "No items to checkout!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Show final confirmation message
            Toast.makeText(this, "Order has been confirmed and is being processed!", Toast.LENGTH_LONG).show();

            // Close activity after confirmation
            finish();
        });
    }

    private void sendOrderDetailsViaSms(String name, String address, String contact, String total) {
        // Check for SMS permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        } else {
            // Prepare and send SMS
            String message = "Order Details:\n"
                    + "Customer Name: " + (name != null ? name : "John Doe") + "\n"
                    + "Address: " + (address != null ? address : "123 Main Street, Cityville") + "\n"
                    + "Contact: " + (contact != null ? contact : "+1234567890") + "\n"
                    + "Total: " + total;

            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(SHOP_OWNER_PHONE, null, message, null, null);
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
