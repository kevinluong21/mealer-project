package com.seg2105.mealer_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

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
    CheckBox checkBoxAddToMenu;
    Button btnBack;

    DatabaseReference users = MainActivity.getUsers(); //get user database from MainActivity

    Button btnAddMeal;

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

        btnAddMeal = (Button) findViewById(R.id.btnAddMealCompleted);



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
        btnAddMeal.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                createMeal(view);
                Toast.makeText(getApplicationContext(), "Meal added", Toast.LENGTH_LONG).show();
                clearFields();
            }
        });


    }


    public void createMeal(View v){

        String mealName = editTextMealName.getText().toString().trim().toLowerCase();
        String mealType = editTextMealType.getText().toString().trim().toLowerCase();
        String cuisineType = editTextCuisineType.getText().toString().trim().toLowerCase();

        //MUST IMPLEMENT PARSING THROUGH THE STRING ARRAY
        //FOR NOW ONLY 1 INGREDIENT WILL BE ADDED TO TEST
        String listOfIngredients = editTextIngredients.getText().toString().trim().toLowerCase();
        String listOfAllergens = editTextAllergens.getText().toString().trim().toLowerCase();
        String priceString = editTextPrice.getText().toString().trim().toLowerCase();
        Double price = Double.parseDouble(priceString);
        String description = editTextDescription.getText().toString().trim().toLowerCase();
        boolean isInMenu = checkBoxAddToMenu.isChecked();

        Meal newMeal = new Meal(mealName,mealType,cuisineType,listOfIngredients,listOfAllergens,price,description,isInMenu);

        users.child(currentCook.getEmailAddress()).child("meals").child(newMeal.getName()).setValue(newMeal);

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


}