package com.example.pizzaorderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView emailTextView; // TextView for displaying email
    EditText fullNameEditText, contactNumberEditText, homeAddressEditText; // EditTexts for user inputs
    Button saveButton, homeButton, profileButton, cartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        emailTextView = findViewById(R.id.textViewEmail);  // TextView for email
        fullNameEditText = findViewById(R.id.editTextFullName);
        contactNumberEditText = findViewById(R.id.editTextContactNumber);
        homeAddressEditText = findViewById(R.id.editTextHomeAddress);

        saveButton = findViewById(R.id.buttonSave);
        homeButton = findViewById(R.id.home);
        profileButton = findViewById(R.id.profile);
        cartButton = findViewById(R.id.cart_id);

        // Load existing profile data
        loadProfileData();

        // Set up Save button click listener
        saveButton.setOnClickListener(view -> saveProfileData());

        // Set up navigation button click listeners
        homeButton.setOnClickListener(view -> navigateToHome());
        cartButton.setOnClickListener(view -> navigateToCart());
    }

    private void loadProfileData() {
        // Retrieve user's email from FirebaseAuth and display it in the TextView
        String email = mAuth.getCurrentUser().getEmail();
        if (email != null) {
            emailTextView.setText(email); // Display email in the TextView (read-only)
        }

        // Retrieve user's full name, contact number, and home address from Firestore
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve data from Firestore and load it into EditTexts
                        String fullName = documentSnapshot.getString("fullName");
                        String contactNumber = documentSnapshot.getString("contactNumber");
                        String homeAddress = documentSnapshot.getString("homeAddress");

                        // Set the data to EditTexts
                        fullNameEditText.setText(fullName);
                        contactNumberEditText.setText(contactNumber);
                        homeAddressEditText.setText(homeAddress);
                    } else {
                        Toast.makeText(Profile.this, "No profile data found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Profile.this, "Error retrieving profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveProfileData() {
        String fullName = fullNameEditText.getText().toString().trim();
        String contactNumber = contactNumberEditText.getText().toString().trim();
        String homeAddress = homeAddressEditText.getText().toString().trim();

        if (fullName.isEmpty() || contactNumber.isEmpty() || homeAddress.isEmpty()) {
            Toast.makeText(Profile.this, "All fields are required!", Toast.LENGTH_SHORT).show();
        } else {
            String userId = mAuth.getCurrentUser().getUid();

            // Save the updated data to Firestore
            db.collection("Users").document(userId)
                    .update("fullName", fullName, "contactNumber", contactNumber, "homeAddress", homeAddress)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(Profile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

                        // After saving the profile, navigate to OrderConfirmationActivity
                        navigateToOrderConfirmation(fullName, contactNumber, homeAddress, 500.0); // Pass a sample total price for now
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Profile.this, "Error updating profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void navigateToOrderConfirmation(String fullName, String contactNumber, String homeAddress, double totalPrice) {
        Intent intent = new Intent(Profile.this, Profile.class);
        intent.putExtra("customer_name", fullName);
        intent.putExtra("customer_contact", contactNumber);
        intent.putExtra("customer_address", homeAddress);
        intent.putExtra("TOTAL_PRICE", totalPrice);
        startActivity(intent);
    }

    private void navigateToHome() {
        Intent intent = new Intent(Profile.this, HomeScreenActivity.class); // Replace HomeActivity with your actual home activity class
        startActivity(intent);
    }

    private void navigateToCart() {
        Intent intent = new Intent(Profile.this, CartActivity2.class); // Replace CartActivity with your actual cart activity class
        startActivity(intent);
    }
}