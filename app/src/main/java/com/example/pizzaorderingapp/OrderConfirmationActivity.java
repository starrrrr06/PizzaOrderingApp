package com.example.pizzaorderingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderConfirmationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private TextView customerNameTextView;
    private TextView customerAddressTextView;
    private TextView customerContactTextView;
    private TextView orderTotalTextView;
    private TextView pizzaNameTextView;  // TextView to display pizza name
    private Button sendOrderButton;
    private Button backToHomeButton;  // Button to navigate back to home

    private static final String EMAIL_ADDRESS = "apppizza44@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        customerNameTextView = findViewById(R.id.customerNameTextView);
        customerAddressTextView = findViewById(R.id.customerAddressTextView);
        customerContactTextView = findViewById(R.id.customerContactTextView);
        orderTotalTextView = findViewById(R.id.orderTotalTextView);
        pizzaNameTextView = findViewById(R.id.pizzaNameTextView);  // Initialize pizza name TextView
        sendOrderButton = findViewById(R.id.checkoutButton);
        backToHomeButton = findViewById(R.id.backToHomeButton);  // Initialize back button

        loadOrderDetails();

        sendOrderButton.setOnClickListener(v -> sendOrderDetails());

        // Set up the back to home button
        backToHomeButton.setOnClickListener(v -> goBackToHome());
    }

    private void loadOrderDetails() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String fullName = documentSnapshot.getString("fullName");
                        String contactNumber = documentSnapshot.getString("contactNumber");
                        String homeAddress = documentSnapshot.getString("homeAddress");
                        double orderTotal = getIntent().getDoubleExtra("TOTAL_PRICE", 0.0);
                        String[] pizzaNames = getIntent().getStringArrayExtra("PIZZA_NAMES"); // Get the pizza names from the intent

                        customerNameTextView.setText(fullName != null ? fullName : "Name not provided");
                        customerAddressTextView.setText(homeAddress != null ? homeAddress : "Address not provided");
                        customerContactTextView.setText(contactNumber != null ? contactNumber : "Contact not provided");
                        orderTotalTextView.setText("₱" + String.format("%.2f", orderTotal));

                        // Join pizza names into a single string to display
                        if (pizzaNames != null && pizzaNames.length > 0) {
                            StringBuilder pizzaList = new StringBuilder();
                            for (String pizza : pizzaNames) {
                                pizzaList.append(pizza).append("\n");
                            }
                            pizzaNameTextView.setText(pizzaList.toString());
                        } else {
                            pizzaNameTextView.setText("No pizza selected");
                        }
                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void sendOrderDetails() {
        String name = customerNameTextView.getText().toString();
        String address = customerAddressTextView.getText().toString();
        String contact = customerContactTextView.getText().toString();
        String total = orderTotalTextView.getText().toString();
        String pizzaName = pizzaNameTextView.getText().toString();  // Get the pizza name

        if (name.isEmpty() || address.isEmpty() || contact.isEmpty() || total.isEmpty() || pizzaName.isEmpty()) {
            Toast.makeText(this, "Incomplete order details!", Toast.LENGTH_SHORT).show();
            return;
        }

        String emailSubject = "Pizza Order Details";
        String emailBody = "Order Details:\n\n"
                + "Name: " + name + "\n"
                + "Address: " + address + "\n"
                + "Contact: " + contact + "\n"
                + "Pizza: " + pizzaName + "\n"
                + "Total: " + total;

        // Create an Intent to send an email
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // Ensure only email apps handle this
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL_ADDRESS});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
            Toast.makeText(this, "Please click the sent and we will process the Orders", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error: Unable to launch email app!", Toast.LENGTH_SHORT).show();
        }
    }

    private void goBackToHome() {
        // Navigate back to the home screen
        Intent homeIntent = new Intent(OrderConfirmationActivity.this, HomeScreenActivity.class);  // Replace MainActivity with your home screen activity
        startActivity(homeIntent);
        finish();  // Optional: To ensure the current activity is closed after navigating
    }
}
