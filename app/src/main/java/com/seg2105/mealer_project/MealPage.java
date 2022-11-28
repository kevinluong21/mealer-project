package com.seg2105.mealer_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MealPage extends AppCompatActivity {

    private RecyclerView listIngredients;
    private RecyclerView listAllergens;
    private Meal meal;

    BottomNavigationView bottomNavBar;

    TextView textMealName;
    TextView textMealCook;
    TextView textMealRating;
    TextView textMealType;
    TextView textCuisineType;
    TextView textPrice;
    TextView textAboutMeal;
    TextView textDescription;

    public MealPage() {
        super(R.layout.activity_meal_page);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_page);

        //when this activity is opened, a meal object is passed to display its information on this page
        this.meal = (Meal) getIntent().getSerializableExtra("meal");

        //bottom nav bar
        bottomNavBar = (BottomNavigationView) findViewById(R.id.bottomNavBar);
        bottomNavBar.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btnHome:
                        startActivity(new Intent(getApplicationContext(),UserWelcome.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.btnProfile:
                        startActivity(new Intent(getApplicationContext(),PersonalProfile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        bottomNavBar.getMenu().findItem(R.id.btnProfile).setChecked(true);

        listIngredients = (RecyclerView) findViewById(R.id.listIngredients);
        listAllergens = (RecyclerView) findViewById(R.id.listAllergens);
        textMealName = (TextView) findViewById(R.id.textMealName);
        textMealCook = (TextView) findViewById(R.id.textMealCook);
        textMealRating = (TextView) findViewById(R.id.textMealRating);
        textMealType = (TextView) findViewById(R.id.textMealType);
        textCuisineType = (TextView) findViewById(R.id.textCuisineType);
        textPrice = (TextView) findViewById(R.id.textPrice);
        textAboutMeal = (TextView) findViewById(R.id.textAboutMeal);
        textDescription = (TextView) findViewById(R.id.textDescription);

        textMealName.setText(meal.getName());
        textMealCook.setText(MainActivity.loggedInCook.getFirstName() + " " + MainActivity.loggedInCook.getLastName());
        textMealRating.setText(Double.toString(meal.getRating()));
        textMealType.setText(meal.getMealType());
        textCuisineType.setText(meal.getCuisineType());
        textPrice.setText(Double.toString(meal.getPrice()));
        textAboutMeal.setText("About "+ meal.getName());
        textDescription.setText(meal.getDescription());


        //this will input the ingredients and allergens into recyclerviews on the meal page
        ArrayList<IngredientModel> ingredients = new ArrayList<IngredientModel>();
        ArrayList<IngredientModel> allergens = new ArrayList<IngredientModel>();

        for (String ingredient : meal.getIngredients().values()) {
            ingredients.add(new IngredientModel(ingredient));
        }

        for (String allergen: meal.getAllergens().values()) {
            allergens.add(new IngredientModel(allergen));
        }

        IngredientAdapter ingredientAdapter = new IngredientAdapter(this, ingredients);
        AllergenAdapter allergenAdapter = new AllergenAdapter(this, allergens);

        //flexbox code taken from https://stackoverflow.com/questions/31744921/how-to-create-horizontal-recyclerview-that-auto-new-line-when-screen-space-is-fu
        FlexboxLayoutManager ingredientsLayoutManager = new FlexboxLayoutManager(this);
        ingredientsLayoutManager.setFlexDirection(FlexDirection.ROW);
        ingredientsLayoutManager.setJustifyContent(JustifyContent.FLEX_START);

        FlexboxLayoutManager allergensLayoutManager = new FlexboxLayoutManager(this);
        allergensLayoutManager.setFlexDirection(FlexDirection.ROW);
        allergensLayoutManager.setJustifyContent(JustifyContent.FLEX_START);

        listIngredients.setLayoutManager(ingredientsLayoutManager);
        listIngredients.setAdapter(ingredientAdapter);

        listAllergens.setLayoutManager(allergensLayoutManager);
        listAllergens.setAdapter(allergenAdapter);
    }
}