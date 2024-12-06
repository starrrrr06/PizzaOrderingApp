package com.example.pizzaorderingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LogInActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button buttonLogin;
    EditText emailAddress, password;
    TextView forgotPass;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acitivty);

        buttonLogin = findViewById(R.id.buttonRegister);
        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);
        forgotPass = findViewById(R.id.forgotPass);

        buttonLogin.setOnClickListener(view -> {
            performLogin(emailAddress.getText().toString(), password.getText().toString());
        });

        forgotPass.setOnClickListener(view -> {
            startActivity(new Intent(LogInActivity.this, ResetPasswordAcitivity.class));
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    public void register(View view) {
        startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
    }

    public void mainActivity(View view) {
        startActivity(new Intent(LogInActivity.this, HomeScreenActivity.class));
    }

    private void performLogin(String email, String password) {
        // Check if the fields are empty
        if (email.isEmpty()) {
            emailAddress.setError("Email is required");
            emailAddress.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            this.password.setError("Password is required");
            this.password.requestFocus();
            return;
        }

        // Proceed with Firebase Authentication if fields are not empty
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(LogInActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LogInActivity.this, HomeScreenActivity.class));
            } else {
                try {
                    throw task.getException();
                } catch (FirebaseAuthInvalidUserException e) {
                    Toast.makeText(LogInActivity.this, "Invalid email address", Toast.LENGTH_LONG).show();
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    Toast.makeText(LogInActivity.this, "Invalid password", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(LogInActivity.this, "An error occurred", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
