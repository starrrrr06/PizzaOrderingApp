package com.example.pizzaorderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button buttonRegister;
    EditText emailAddress, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        buttonRegister = findViewById(R.id.buttonRegister);
        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);

        buttonRegister.setOnClickListener(view -> {
            register(emailAddress.getText().toString(), password.getText().toString());
        });

    }

    public void login(View view){
        startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
    }

    public void mainActivity(View view){
        startActivity(new Intent(RegisterActivity.this,LogInActivity.class));
    }
    private void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(RegisterActivity.this, "Register Successful!", Toast.LENGTH_SHORT).show();
            }
            else {
                try{
                    throw task.getException();
                }catch(FirebaseAuthUserCollisionException e){
                    Toast.makeText(RegisterActivity.this, "Email has already taken",
                            Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Toast.makeText(RegisterActivity.this, "An error occurred!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}