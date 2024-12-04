package com.example.pizzaorderingapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView fullNameTextView, emailTextView; // Add TextView for email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fullNameTextView = findViewById(R.id.textViewFullName);
        emailTextView = findViewById(R.id.textViewEmail); // Initialize email TextView

        loadProfileData();
    }

    private void loadProfileData() {
        // Retrieve user's email from FirebaseAuth
        String email = mAuth.getCurrentUser().getEmail();
        if (email != null) {
            emailTextView.setText(email);
        } else {
            emailTextView.setText("Email not available");
        }

        // Retrieve user's full name from Firestore
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String fullName = documentSnapshot.getString("fullName");
                        fullNameTextView.setText(fullName);
                    } else {
                        Toast.makeText(Profile.this, "No profile data found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Profile.this, "Error retrieving profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}