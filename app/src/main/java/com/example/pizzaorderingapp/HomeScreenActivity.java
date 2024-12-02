package com.example.pizzaorderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeScreenActivity extends AppCompatActivity {
    public static String MENU_PEPPERONI = "Pepperoni Pizza";
    public static String MENU_HAWAIIAN = "Hawaiian Pizza";
    public static String MENU_CHEESE = "Cheese Pizza";
    public static String MENU_VEGETABLE = "Vegetable Pizza";
    public static String MENU_SPINACH = "Spinach Pizza";
    public static String MENU_ALLMEAT = "All Meat Pizza";

    private HashMap<String, Double> pizzaPrices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen); // The layout containing the buttons

        pizzaPrices = new HashMap<>();
        pizzaPrices.put(MENU_CHEESE, 7.99);
        pizzaPrices.put(MENU_VEGETABLE, 8.49);
        pizzaPrices.put(MENU_HAWAIIAN, 9.99);
        pizzaPrices.put(MENU_PEPPERONI, 8.99);
        pizzaPrices.put(MENU_ALLMEAT, 10.99);
        pizzaPrices.put(MENU_SPINACH, 8.99);

        // Find the home button by its ID
        Button homeButton = findViewById(R.id.home);

        // Set an onClickListener to the home button
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to HomeScreenActivity (if needed)
                Intent intent = new Intent(HomeScreenActivity.this, HomeScreenActivity.class);
                startActivity(intent);
            }
        });

        // Find the cart button by its ID
        Button cartButton = findViewById(R.id.cart_id);

        // Set an onClickListener to the cart button
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CartActivity when the button is clicked
                Intent cartButton = new Intent(HomeScreenActivity.this, CartActivity2.class);
                startActivity(cartButton);
            }
        });

        Button profButton = findViewById(R.id.profile);

        profButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profButton = new Intent(HomeScreenActivity.this, Profile.class);
                startActivity(profButton);
            }
        });

        ArrayList<String> menuArrayList = new ArrayList<String>();

        menuArrayList.add(MENU_CHEESE);
        menuArrayList.add(MENU_VEGETABLE);
        menuArrayList.add(MENU_HAWAIIAN);
        menuArrayList.add(MENU_PEPPERONI);
        menuArrayList.add(MENU_ALLMEAT);
        menuArrayList.add(MENU_SPINACH);

        ArrayAdapter<String> menuArrayAdapter = new ArrayAdapter<String>(HomeScreenActivity.this,
                android.R.layout.simple_spinner_dropdown_item, menuArrayList);

        menuArrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        Spinner orderSpinner = (Spinner) findViewById(R.id.spinner_id);

        orderSpinner.setAdapter(menuArrayAdapter);

        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedPizzaImage = menuArrayAdapter.getItem(i).toString();

                //Generate Image
                ImageView selectedPizza = (ImageView) findViewById(R.id.imageView);

                if (selectedPizzaImage == MENU_HAWAIIAN){
                    selectedPizza.setImageResource(R.drawable.hawaiian);

                } else if (selectedPizzaImage == MENU_CHEESE){
                    selectedPizza.setImageResource(R.drawable.cheese);

                } else if (selectedPizzaImage == MENU_PEPPERONI){
                    selectedPizza.setImageResource(R.drawable.pepperonipizza);

                } else if (selectedPizzaImage == MENU_VEGETABLE) {
                    selectedPizza.setImageResource(R.drawable.vegetable);

                } else if (selectedPizzaImage == MENU_ALLMEAT) {
                    selectedPizza.setImageResource(R.drawable.allmeat);

                } else if (selectedPizzaImage == MENU_SPINACH) {
                    selectedPizza.setImageResource(R.drawable.spinach);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Add to cart button
        Button addToCartButton = findViewById(R.id.order);
        addToCartButton.setOnClickListener(v -> {
            String selectedPizza = (String) orderSpinner.getSelectedItem();
            CartManager.getInstance().addToCart(selectedPizza);
            Toast.makeText(HomeScreenActivity.this, selectedPizza + " added to cart", Toast.LENGTH_SHORT).show();
        });

    }
}