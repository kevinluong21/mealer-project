package com.seg2105.mealer_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    Button btnAddToCart;
    RadioGroup servingGroup;
    RadioButton oneServing;
    RadioButton twoServings;
    RadioButton threeServings;
    RadioButton fourServings;
    TextView textTotalPrice;

    public MealPage() {
        super(R.layout.activity_meal_page);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_page);
        btnAddToCart = (Button) findViewById(R.id.btnAddToCart);

        //when this activity is opened, a meal object is passed to display its information on this page
        this.meal = (Meal) getIntent().getSerializableExtra("meal");

        //sets the toolbar for this activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.icons8_chevron_left_24);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(currentUser.getRole().equals("Cook")){
            btnAddToCart.setVisibility(View.GONE);
        }
        if(currentUser.getRole().equals("Client")){
            btnAddToCart.setVisibility(View.VISIBLE);
        }

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
        servingGroup = (RadioGroup) findViewById(R.id.servingGroup);
        oneServing = (RadioButton) findViewById(R.id.radio1Serving);
        twoServings = (RadioButton) findViewById(R.id.radio2Servings);
        threeServings = (RadioButton) findViewById(R.id.radio3Servings);
        fourServings = (RadioButton) findViewById(R.id.radio4Servings);
        textTotalPrice = (TextView) findViewById(R.id.textTotalPrice);

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

        textTotalPrice.setText(Double.toString(meal.getPrice())); //by default

        servingGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (servingGroup.getCheckedRadioButtonId()) {
                    case R.id.radio1Serving:
                        textTotalPrice.setText(Double.toString(meal.getPrice()));
                        break;

                    case R.id.radio2Servings:
                        textTotalPrice.setText(Double.toString(meal.getPrice() * 2));
                        break;

                    case R.id.radio3Servings:
                        textTotalPrice.setText(Double.toString(meal.getPrice() * 3));
                        break;

                    case R.id.radio4Servings:
                        textTotalPrice.setText(Double.toString(meal.getPrice() * 4));
                        break;
                }
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMeal();
            }
        });
    }

    public void requestMeal() {
        int numberOfRequests; //number of servings that the client wants of this meal

        switch (servingGroup.getCheckedRadioButtonId()) {
            case R.id.radio1Serving:
                numberOfRequests = 1;
                break;

            case R.id.radio2Servings:
                numberOfRequests = 2;
                break;

            case R.id.radio3Servings:
                numberOfRequests = 3;
                break;

            case R.id.radio4Servings:
                numberOfRequests = 4;
                break;
        }

        MainActivity.checkUser(meal.getCookEmail(), new UserCallback<Administrator, Cook, Client>() {
            @Override
            public void onCallback(Administrator admin, Cook cook, Client client) {
                if (cook != null) { //cook with that email was found
                    MealRequest req = new MealRequest(meal, MainActivity.loggedInClient, cook);

                    MainActivity.users.child(cook.getEmailAddress()).child("purchaseRequests").child(meal.getName()).setValue(req);
                    MainActivity.users.child(MainActivity.currentUser.getEmailAddress()).child("requestedMeals").child(meal.getName()).setValue(req);
                    Toast.makeText(getApplicationContext(),"Purchase Request Complete", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //create toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //different toolbars based on the logged in user
        getMenuInflater().inflate(R.menu.toolbar_meal, menu);
        MenuItem actionComplaint = menu.findItem(R.id.actionComplaint);
        actionComplaint.setVisible(false);

        if (MainActivity.currentUser.getRole().equals("Client")) {
            //if the logged in user is a client and they are looking at a cook's profile, allow them to file a complaint
            actionComplaint.setVisible(true);
        }
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

            case R.id.actionComplaint: //create a complaint
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.action_dialog_rate_cook, null);
                dialogBuilder.setView(dialogView);

                final AlertDialog b = dialogBuilder.create();
                b.setCancelable(true);
                b.show();

                final TextView textCookName = (TextView) dialogView.findViewById(R.id.textCookName);
                final Button btnSendComplaint = (Button) dialogView.findViewById((R.id.btnSendComplaint));
                final EditText textComplaint = (EditText) dialogView.findViewById(R.id.textComplaint);
                final TextView errorMessage = (TextView) dialogView.findViewById(R.id.textErrorMessage);

                DatabaseReference complaints = FirebaseDatabase.getInstance().getReference("complaints");

                textCookName.setText(meal.getCookFirstName() + " " + meal.getCookLastName());

                btnSendComplaint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String description = textComplaint.getText().toString().trim();
                        if (TextUtils.isEmpty(description)) {
                            errorMessage.setText("All fields must be filled.");
                        }
                        else {
                            Complaint complaint = new Complaint(description, MainActivity.loggedInClient.getEmailAddress(), meal.getCookEmail(),
                                    complaints.push().getKey()); //id is auto-generated by firebase to avoid overlap
                            complaints.child(complaint.getId()).setValue(complaint);
                            Toast.makeText(getApplicationContext(), "Complaint Submitted", Toast.LENGTH_LONG).show();
                            b.dismiss();
                        }
                    }
                });
                break;

            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}