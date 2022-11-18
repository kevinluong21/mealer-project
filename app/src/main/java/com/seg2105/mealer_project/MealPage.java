package com.seg2105.mealer_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MealPage extends AppCompatActivity {

    private RecyclerView listIngredients;
    private Meal meal;

    TextView textMealName;
    TextView textMealCook;
    TextView textMealRating;
    TextView textMealType;
    TextView textCuisineType;
    TextView textPrice;

    public MealPage() {
        super(R.layout.activity_meal_page);
    }

//    @Override
//    public void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        this.meal = (Meal) getIntent().getSerializableExtra("meal");
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_page);

        Intent intent = getIntent();
        this.meal = (Meal) getIntent().getSerializableExtra("meal");

        listIngredients = (RecyclerView) findViewById(R.id.listIngredients);
        listIngredients.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<IngredientModel> ingredients = new ArrayList<IngredientModel>();

        for (String ingredient : meal.getIngredients().values()) {
            ingredients.add(new IngredientModel(ingredient));
        }

        IngredientAdapter ingredientAdapter = new IngredientAdapter(this, ingredients);
        LinearLayoutManager ingredientsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listIngredients.setLayoutManager(ingredientsLayoutManager);
        listIngredients.setAdapter(ingredientAdapter);
    }
}