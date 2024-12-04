//package com.example.pizzaorderingapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthUserCollisionException;
//
//public class RegisterActivity extends AppCompatActivity {
//
//    FirebaseAuth mAuth;
//    Button buttonRegister;
//    EditText emailAddress, password, full_name;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        mAuth = FirebaseAuth.getInstance();
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//
//        buttonRegister = findViewById(R.id.buttonRegister);
//        emailAddress = findViewById(R.id.emailAddress);
//        password = findViewById(R.id.password);
//
//        buttonRegister.setOnClickListener(view -> {
//            register(emailAddress.getText().toString(), password.getText().toString());
//        });
//
//    }
//
//    public void login(View view){
//        startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
//    }
//
//    public void mainActivity(View view){
//        startActivity(new Intent(RegisterActivity.this,LogInActivity.class));
//    }
//    private void register(String email, String password) {
//        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
//            if(task.isSuccessful()){
//                Toast.makeText(RegisterActivity.this, "Register Successful!", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                try{
//                    throw task.getException();
//                }catch(FirebaseAuthUserCollisionException e){
//                    Toast.makeText(RegisterActivity.this, "Email has already taken",
//                            Toast.LENGTH_SHORT).show();
//                }catch(Exception e){
//                    Toast.makeText(RegisterActivity.this, "An error occurred!",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//}
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
    FirebaseFirestore db; // Add Firestore instance
    Button buttonRegister;
    EditText emailAddress, password, full_name; // Added `full_name`

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        buttonRegister = findViewById(R.id.buttonRegister);
        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);
        full_name = findViewById(R.id.full_name); // Initialize full_name EditText

        buttonRegister.setOnClickListener(view -> {
            String email = emailAddress.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String fullName = full_name.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty() || fullName.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            register(email, pass, fullName); // Pass full name to register method
        });
    }

    public void login(View view) {
        startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
    }

    public void mainActivity(View view) {
        startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
    }

    private void register(String email, String password, String fullName) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String userId = mAuth.getCurrentUser().getUid();

                // Save full name to Firestore
                Map<String, Object> userData = new HashMap<>();
                userData.put("fullName", fullName);

                db.collection("Users").document(userId)
                        .set(userData)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(RegisterActivity.this, "Register Successful!", Toast.LENGTH_SHORT).show();
                            // Redirect to main or profile activity after successful registration
                            startActivity(new Intent(RegisterActivity.this, Profile.class));
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