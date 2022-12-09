package com.seg2105.mealer_project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MealPage extends AppCompatActivity {

    private RecyclerView listIngredients;
    private RecyclerView listAllergens;
    private Meal meal;

    Person currentUser = MainActivity.currentUser;

    TextView textMealName;
    TextView textMealCook;
    TextView textMealRating;
    TextView textMealType;
    TextView textCuisineType;
    TextView textPrice;
    TextView textAboutMeal;
    TextView textDescription;
    Button btnAddToCart;
    LinearLayout layoutAddToCart;
    TextView textTotalPrice;

    public MealPage() {
        super(R.layout.activity_meal_page);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_page);

        //when this activity is opened, a meal object is passed to display its information on this page
        this.meal = (Meal) getIntent().getSerializableExtra("meal");

        //sets the toolbar for this activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.icons8_chevron_left_24);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        textTotalPrice = (TextView) findViewById(R.id.textTotalPrice);
        layoutAddToCart = (LinearLayout) findViewById(R.id.layoutAddToCart);
        btnAddToCart = (Button) findViewById(R.id.btnAddToCart);

        textMealName.setText(meal.getName());
        textMealCook.setText(meal.getCookFirstName() + " " + meal.getCookLastName());
        textMealRating.setText(Double.toString(meal.getRating()));
        textMealType.setText(meal.getMealType());
        textCuisineType.setText(meal.getCuisineType());
        textPrice.setText(Double.toString(meal.getPrice()));
        textAboutMeal.setText("About "+ meal.getName());
        textDescription.setText(meal.getDescription());

        if(currentUser.getRole().equals("Cook")){
            layoutAddToCart.setVisibility(View.GONE);
        }
        if(currentUser.getRole().equals("Client")){
            layoutAddToCart.setVisibility(View.VISIBLE);
        }

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

        if (!meal.isOffering()) { //clients cannot order a meal that is not being offered
            layoutAddToCart.setVisibility(View.GONE);
        }

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

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMeal();
            }
        });
    }

    public void requestMeal() {
        //notification that order was sent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Mealer";
            String description = "group 5 :)";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        if (MainActivity.currentUser.getRole().equals("Client")) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_ID")
                    .setSmallIcon(R.drawable.icons8_french_fries)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());


            MainActivity.checkUser(meal.getCookEmail(), new UserCallback<Administrator, Cook, Client>() {
                @Override
                public void onCallback(Administrator admin, Cook cook, Client client) {
                    if (cook != null) { //cook with that email was found
                        MealRequest req = new MealRequest(meal, MainActivity.loggedInClient, cook);

                        MainActivity.users.child(cook.getEmailAddress()).child("purchaseRequests").child(meal.getName()).setValue(req);
                        MainActivity.users.child(MainActivity.currentUser.getEmailAddress()).child("requestedMeals").child(meal.getName()).setValue(req);
                        Toast.makeText(getApplicationContext(),"Purchase Request Complete", Toast.LENGTH_LONG).show();

                        builder.setContentTitle("Awaiting Response...");
                        builder.setContentText("It can take a few minutes for the cook to respond to your order.");
                        notificationManager.notify(1, builder.build());
                    }
                }
            });
        }
    }

    //create toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //different toolbars based on the logged in user
        getMenuInflater().inflate(R.menu.toolbar_meal, menu);
        MenuItem actionComplaint = menu.findItem(R.id.actionComplaint);
        MenuItem actionSave = menu.findItem(R.id.actionSave);

        actionComplaint.setVisible(false);
        actionSave.setVisible(false);

        if (MainActivity.currentUser.getRole().equals("Client")) {
            //if the logged in user is a client and they are looking at a cook's profile, allow them to file a complaint
            actionComplaint.setVisible(true);
            actionSave.setVisible(true);

            MainActivity.users.child(MainActivity.loggedInClient.getEmailAddress()).child("likedMeals").child(meal.getName()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        actionSave.setIcon(R.drawable.icons8_bookmark_24_filled);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    //toolbar menu select action
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSave:
                //if already on the liked list of client, set icon to filled by default
                //if not, reset to default
                if (item.getIcon().getConstantState().equals(getDrawable(R.drawable.icons8_bookmark_24_filled).getConstantState())) {
                    //already in likedMeals
                    item.setIcon(R.drawable.icons8_bookmark_24);
                    //remove meal from likedMeals
                    MainActivity.users.child(MainActivity.loggedInClient.getEmailAddress()).child("likedMeals").child(meal.getName()).removeValue();
                }
                else {
                    item.setIcon(R.drawable.icons8_bookmark_24_filled);
                    //put liked meal into likedMeals of the client
                    MainActivity.users.child(MainActivity.loggedInClient.getEmailAddress()).child("likedMeals").child(meal.getName()).setValue(meal);
                }
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