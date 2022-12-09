package com.seg2105.mealer_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class MealActivity extends AppCompatActivity implements Serializable {

    Person currentCook = MainActivity.currentUser;
    EditText editTextMealName;
    EditText editTextMealType;
    EditText editTextCuisineType;
    EditText editTextIngredients; //separated by comma
    EditText editTextAllergens;
    EditText editTextPrice;
    EditText editTextDescription;
    TextView mealErrorMessage;
    CheckBox checkBoxAddToMenu;

    DatabaseReference users = MainActivity.getUsers(); //get user database from MainActivity

    Button btnAddMealCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        //sets the toolbar for this activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.icons8_chevron_left_24);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextMealName = (EditText) findViewById(R.id.editTextMealName);
        editTextMealType = (EditText) findViewById(R.id.editTextMealType);
        editTextCuisineType = (EditText) findViewById(R.id.editTextCuisineType);
        editTextIngredients = (EditText) findViewById(R.id.editTextIngredients);
        editTextAllergens = (EditText) findViewById(R.id.editTextAllergens);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        mealErrorMessage = (TextView) findViewById(R.id.mealErrorMessage);

        btnAddMealCompleted = (Button) findViewById(R.id.btnAddMealCompleted);

        checkBoxAddToMenu = (CheckBox) findViewById(R.id.checkBoxAddedToMenu);

        //AP: used to test the addition to db
        /*Meal check = new Meal("mealname", "mealtype", " cusinetype", "ingredients,klk", "allergens,jhjh", 10, "description", false);

        users.child(currentCook.getEmailAddress()).child("meals").child(check.getName()).setValue(check);
        */
        btnAddMealCompleted.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btnAddMealCompleted:
                        createMeal(view);
                        break;
                }
            }
        });
    }



    public void createMeal(View v){

        String mealName = editTextMealName.getText().toString().trim().toLowerCase();
        String mealType = editTextMealType.getText().toString().trim().toLowerCase();
        String cuisineType = editTextCuisineType.getText().toString().trim().toLowerCase();

        //MUST IMPLEMENT PARSING THROUGH THE STRING ARRAY
        String listOfIngredients = editTextIngredients.getText().toString().trim().toLowerCase();
        String listOfAllergens = editTextAllergens.getText().toString().trim().toLowerCase();
        String priceString = editTextPrice.getText().toString().trim().toLowerCase();
        if (checkPrice(priceString) == true) {
            Double price = Double.parseDouble(priceString);
            String description = editTextDescription.getText().toString().trim().toLowerCase();
            boolean isInMenu = checkBoxAddToMenu.isChecked();

            mealErrorMessage.setText("");

            if (!TextUtils.isEmpty(mealName) && !TextUtils.isEmpty(mealType) && !TextUtils.isEmpty(cuisineType) && !TextUtils.isEmpty(listOfIngredients)
                    && !TextUtils.isEmpty(listOfAllergens) && !TextUtils.isEmpty(priceString) && !TextUtils.isEmpty(description)) {
                if(inputValidation(mealType,cuisineType,priceString) == true){

                    Meal newMeal = new Meal(MainActivity.loggedInCook, mealName, mealType, cuisineType, listOfIngredients, listOfAllergens, price, description, isInMenu);

                    users.child(currentCook.getEmailAddress()).child("meals").child(newMeal.getName()).setValue(newMeal);
                    Toast.makeText(getApplicationContext(), "Meal added", Toast.LENGTH_LONG).show();
                    clearFields();
                }
            } else {
                mealErrorMessage.setText("All fields must be filled");
            }
        }else {
            mealErrorMessage.setText("Price must be formatted like this 1.00");
        }

    }

    public void clearFields(){
        editTextMealName.setText("");
        editTextMealType.setText("");
        editTextCuisineType.setText("");
        editTextIngredients.setText("");
        editTextAllergens.setText("");
        editTextPrice.setText("");
        editTextDescription.setText("");
        checkBoxAddToMenu.setChecked(false);

    }

    public boolean inputValidation(String meal, String cuisine, String price){
        if(checkMealType(meal) == true){
            if(checkCuisineType(cuisine) == true){
                if(checkPrice(price) == true){
                    return true;
                }else{
                mealErrorMessage.setText("Price must be formatted like this 1.00");
                return false;}
            }else{mealErrorMessage.setText("Cuisine type must only contain letters a through z");
                return false;}
        }else{mealErrorMessage.setText("Meal type must be main dish, side dish, appetizer, or dessert");
            return false;}
    }
    public static boolean checkMealType(String type){
        type = type.toLowerCase();
        if(type.equals("main dish") || type.equals("side dish") || type.equals("dessert") || type.equals("appetizer")){
            return true;
        }else{
            return false;
        }
    }
    public static boolean checkCuisineType(String type){
        if(type.matches("[a-zA-Z\\-]*")){
            return true;
        }
        return false;
    }
    public static boolean checkPrice(String p){
        if(p.matches("[0-9]*[.][0-9]{2}")){
            return true;
        }
        return false;
    }

    //create toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //toolbar menu select action
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}