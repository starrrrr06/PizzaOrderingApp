package com.example.pizzaorderingapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordAcitivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button buttonForgot;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password_acitivity);

        mAuth = FirebaseAuth.getInstance();
        buttonForgot = findViewById(R.id.buttonForgot);
        email = findViewById(R.id.email);


        buttonForgot.setOnClickListener(view ->{
            mAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(task ->{
                Toast.makeText(ResetPasswordAcitivity.this,
                        task.isSuccessful()?"Password reset link has been sent":"An error occured",
                        Toast.LENGTH_SHORT).show();
            });
        });
    }
}