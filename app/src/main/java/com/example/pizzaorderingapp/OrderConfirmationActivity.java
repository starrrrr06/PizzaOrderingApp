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

    public TextView full_name;
    public TextView customerAddressTextView;
    public TextView customerContactTextView;
    public TextView orderTotalTextView;
    public Button backToHomeButton;
    public Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        full_name = findViewById(R.id.customerNameTextView);
        customerAddressTextView = findViewById(R.id.customerAddressTextView);
        customerContactTextView = findViewById(R.id.customerContactTextView);
        orderTotalTextView = findViewById(R.id.orderTotalTextView);
        backToHomeButton = findViewById(R.id.backToHomeButton);
        checkoutButton = findViewById(R.id.checkoutButton);

        // Get data from intent (passed from Profile activity)
        String customerName = getIntent().getStringExtra("customer_name");
        String customerAddress = getIntent().getStringExtra("customer_address");
        String customerContact = getIntent().getStringExtra("customer_contact");
        double totalPrice = getIntent().getDoubleExtra("TOTAL_PRICE", 0.0);

        // Set the profile data to the respective TextViews
        if (customerName != null) {
            full_name.setText(customerName);
        }
        if (customerAddress != null) {
            customerAddressTextView.setText(customerAddress);
        }
        if (customerContact != null) {
            customerContactTextView.setText(customerContact);
        }
        orderTotalTextView.setText("₱" + String.format("%.2f", totalPrice));

        // Send SMS
        sendOrderDetailsViaSms(customerName, customerAddress, customerContact, String.format("₱%.2f", totalPrice));

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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        } else {
            String message = "Order Details:\n"
                    + "Customer Name: " + (name != null ? name : "") + "\n"
                    + "Address: " + (address != null ? address : "") + "\n"
                    + "Contact: " + (contact != null ? contact : "") + "\n"
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
