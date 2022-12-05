package com.seg2105.mealer_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MealPage extends AppCompatActivity {

    private RecyclerView listIngredients;
    private RecyclerView listAllergens;
    private Meal meal;

    Person currentUser = MainActivity.currentUser;

    BottomNavigationView bottomNavBar;

    TextView textMealName;
    TextView textMealCook;
    TextView textMealRating;
    TextView textMealType;
    TextView textCuisineType;
    TextView textPrice;
    TextView textAboutMeal;
    TextView textDescription;
    Button requestMealBtn;

    public MealPage() {
        super(R.layout.activity_meal_page);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_page);
        requestMealBtn = (Button) findViewById(R.id.requestMealBtn);

        //when this activity is opened, a meal object is passed to display its information on this page
        this.meal = (Meal) getIntent().getSerializableExtra("meal");

        //sets the toolbar for this activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.icons8_chevron_left_24);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(currentUser.getRole().equals("Cook")){
            requestMealBtn.setVisibility(View.GONE);
        }
        if(currentUser.getRole().equals("Client")){
            requestMealBtn.setVisibility(View.VISIBLE);
        }

        //bottom nav bar no longer needed now that there is a toolbar
//        //bottom nav bar
//        bottomNavBar = (BottomNavigationView) findViewById(R.id.bottomNavBar);
//        bottomNavBar.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.btnHome:
//                        startActivity(new Intent(getApplicationContext(),UserWelcome.class));
//                        overridePendingTransition(0,0);
//                        return true;
//                    case R.id.btnProfile:
//                        startActivity(new Intent(getApplicationContext(),PersonalProfile.class));
//                        overridePendingTransition(0,0);
//                        return true;
//                }
//                return false;
//            }
//        });
//        bottomNavBar.getMenu().findItem(R.id.btnProfile).setChecked(true);

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
        textMealCook.setText(meal.getCookFirstName() + " " + meal.getCookLastName());
        textMealRating.setText(Double.toString(meal.getRating()));
        textMealType.setText(meal.getMealType());
        textCuisineType.setText(meal.getCuisineType());
        textPrice.setText(Double.toString(meal.getPrice()));
        textAboutMeal.setText("About "+ meal.getName());
        textDescription.setText(meal.getDescription());

        //take the user to the profile of the cook on click of their name
        textMealCook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealPage.this, PersonalProfile.class);
                MainActivity.checkUser(meal.getCookEmail(), new UserCallback<Administrator, Cook, Client>() {
                    @Override
                    public void onCallback(Administrator user1, Cook user2, Client user3) {
                        intent.putExtra("user", user2);
                        startActivity(intent);
                    }
                });
            }
        });

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

        requestMealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMeal();
            }
        });
    }

//    public void onClick(View v){
//        switch(v.getId()){
//            case R.id.requestMealBtn:
//                requestMeal();
//                break;
//        }
//    }

    public void requestMeal() {
        Log.d("TAG", "meal requesting...");
        MainActivity.checkUser(meal.getCookEmail(), new UserCallback<Administrator, Cook, Client>() {
            @Override
            public void onCallback(Administrator admin, Cook cook, Client client) {
                if (cook != null) { //cook with that email was found
                    MealRequest req = new MealRequest(meal, MainActivity.loggedInClient.getEmailAddress(), cook.getEmailAddress());
                    cook.receiveRequest(req); //updates the Cook object
                    MainActivity.loggedInClient.requestMeal(req); //updates the Client object

                    MainActivity.users.child(cook.getEmailAddress()).setValue(cook); //upload the updated Cook to firebase
                    MainActivity.users.child(MainActivity.currentUser.getEmailAddress()).setValue(MainActivity.loggedInClient); //upload the updated
                    //client to firebase
                    Toast.makeText(getApplicationContext(),"Purchase Request Complete", Toast.LENGTH_LONG).show();
                }
//                MainActivity.checkUser(meal.getCookEmail(), new UserCallback<Administrator, Cook, Client>() {
//                    @Override
//                    public void onCallback(Administrator user1b, Cook user2b, Client user3b) {
//                        MealRequest req = new MealRequest(meal, user3a, user2b);
//                        user3a.requestMeal(req);
//                        user2b.receiveRequest(req);
//                        Toast.makeText(getApplicationContext(),"Purchase Request Complete", Toast.LENGTH_LONG).show();
//                    }
//                });
            }
        });
    }

    //create toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //different toolbars based on the logged in user
        getMenuInflater().inflate(R.menu.toolbar_meal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //toolbar menu select action
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionLike:
                //if already on the liked list of client, set icon to filled by default
                //if not, reset to default
                item.setIcon(R.drawable.icons8_favorite_24_filled);
                break;

            case R.id.actionComplaint:
                //insert action complaint activity here!
                break;

            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}