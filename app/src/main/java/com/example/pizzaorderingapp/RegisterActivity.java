package com.example.pizzaorderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db; // Firestore instance
    Button buttonRegister;
    EditText emailAddress, password, full_name, contact_number, home_address; // Added `home_address` and `contact_number`

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        buttonRegister = findViewById(R.id.buttonRegister);
        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);
        full_name = findViewById(R.id.full_name);
        contact_number = findViewById(R.id.contact_number);
        home_address = findViewById(R.id.home_address);

        buttonRegister.setOnClickListener(view -> {
            String email = emailAddress.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String fullName = full_name.getText().toString().trim();
            String contactNumber = contact_number.getText().toString().trim();
            String homeAddress = home_address.getText().toString().trim();

            // Validation
            if (email.isEmpty() || pass.isEmpty() || fullName.isEmpty() || contactNumber.isEmpty() || homeAddress.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            register(email, pass, fullName, contactNumber, homeAddress); // Pass all fields to the register method
        });
    }

    public void login(View view) {
        startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
    }

    public void mainActivity(View view) {
        startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
    }

    private void register(String email, String password, String fullName, String contactNumber, String homeAddress) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String userId = mAuth.getCurrentUser().getUid();

                // Create a map for user data
                Map<String, Object> userData = new HashMap<>();
                userData.put("fullName", fullName);
                userData.put("contactNumber", contactNumber);
                userData.put("homeAddress", homeAddress);

                // Save user data to Firestore
                db.collection("Users").document(userId)
                        .set(userData)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(RegisterActivity.this, "Register Successful!", Toast.LENGTH_SHORT).show();
                            // Redirect to main or profile activity after successful registration
                            startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(RegisterActivity.this, "Error saving user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                try {
                    throw task.getException();
                } catch (FirebaseAuthUserCollisionException e) {
                    Toast.makeText(RegisterActivity.this, "Email has already been taken", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(RegisterActivity.this, "An error occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}