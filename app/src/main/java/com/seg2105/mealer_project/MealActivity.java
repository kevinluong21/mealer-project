package com.seg2105.mealer_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
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
    //currentUser.getRole();


    EditText editTextMealName;
    EditText editTextMealType;
    EditText editTextCuisineType;
    EditText editTextIngredients; //separated by comma
    EditText editTextAllergens;
    EditText editTextPrice;
    EditText editTextDescription;
    TextView mealErrorMessage;
    CheckBox checkBoxAddToMenu;
    Button btnBack;

    DatabaseReference users = MainActivity.getUsers(); //get user database from MainActivity

    Button btnAddMealCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);


        editTextMealName = (EditText) findViewById(R.id.editTextMealName);
        editTextMealType = (EditText) findViewById(R.id.editTextMealType);
        editTextCuisineType = (EditText) findViewById(R.id.editTextCuisineType);
        editTextIngredients = (EditText) findViewById(R.id.editTextIngredients);
        editTextAllergens = (EditText) findViewById(R.id.editTextAllergens);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        mealErrorMessage = (TextView) findViewById(R.id.mealErrorMessage);

        btnAddMealCompleted = (Button) findViewById(R.id.btnAddMealCompleted);



        btnBack = (Button) findViewById(R.id.btnBackHomeToCook);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

                    Meal newMeal = new Meal(mealName, mealType, cuisineType, listOfIngredients, listOfAllergens, price, description, isInMenu);

                    users.child(currentCook.getEmailAddress()).child("meals").child(newMeal.getName()).setValue(newMeal);
                    Toast.makeText(getApplicationContext(), "Meal added", Toast.LENGTH_LONG).show();
                    clearFields();
                }
            } else {
                mealErrorMessage.setText("All fields must be filled");
            }
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
                }else{return false;}
            }else{return false;}
        }else{return false;}
    }
    public boolean checkMealType(String type){
        type = type.toLowerCase();
        if(type.equals("main dish") || type.equals("side dish") || type.equals("dessert") || type.equals("appetizer")){
            return true;
        }else{
            mealErrorMessage.setText("Meal type must be main dish, side dish, appetizer, or dessert");
            return false;
        }
    }
    public boolean checkCuisineType(String type){
        if(type.matches("[a-zA-Z\\-]*")){
            return true;
        }
        mealErrorMessage.setText("Cuisine type must only contain letters a through z");
        return false;
    }
    public boolean checkPrice(String p){
        if(p.matches("[0-9]*[.][0-9]{2}")){
            return true;
        }
        mealErrorMessage.setText("Price must be formatted like this 1.00");
        return false;
    }



}