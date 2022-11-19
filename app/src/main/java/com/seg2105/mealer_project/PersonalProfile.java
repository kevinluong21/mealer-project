//the code for implementing the recyclerview comes from https://www.geeksforgeeks.org/cardview-using-recyclerview-in-android-with-example/

package com.seg2105.mealer_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PersonalProfile extends AppCompatActivity {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    TextView textName;
    TextView textRole;
    BottomNavigationView bottomNavBar;
    RecyclerView listOfferedMeals;
    RecyclerView listMeals;
    TextView textOfferedMenu;
    TextView textMenu;
    DatabaseReference cookMeals;

    int longClickCounter;

    public PersonalProfile() {
        super(R.layout.activity_personal_profile);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);

        listOfferedMeals = (RecyclerView) findViewById(R.id.listOfferedMeals);
        listMeals = (RecyclerView) findViewById(R.id.listMeals);

        //bottom nav bar for this activity
        bottomNavBar = (BottomNavigationView) findViewById(R.id.bottomNavBar);
        bottomNavBar.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btnHome:
                        startActivity(new Intent(getApplicationContext(),UserWelcome.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.btnProfile:
                        return true;
                }
                return false;
            }
        });
        bottomNavBar.setSelectedItemId(R.id.btnProfile);

        textName = (TextView) findViewById(R.id.textName);
        textRole = (TextView) findViewById(R.id.textRole);
        textOfferedMenu = (TextView) findViewById(R.id.textOfferedMenu);
        textMenu = (TextView) findViewById(R.id.textMenu);

        textName.setText(MainActivity.currentUser.getFirstName() + " " + MainActivity.currentUser.getLastName());
        textRole.setText(MainActivity.currentUser.getRole());

        textOfferedMenu.setVisibility(View.GONE);
        textMenu.setVisibility(View.GONE);
        listOfferedMeals.setVisibility(View.GONE);
        listMeals.setVisibility(View.GONE);


        //only display meals if the signed in user is a cook
        if (MainActivity.currentUser.getRole().equals("Cook")) {
            textOfferedMenu.setVisibility(View.VISIBLE);
            textMenu.setVisibility(View.VISIBLE);
            listOfferedMeals.setVisibility(View.VISIBLE);
            listMeals.setVisibility(View.VISIBLE);

            cookMeals = FirebaseDatabase.getInstance().getReference("users").child(MainActivity.emailAddressToKey(MainActivity.loggedInCook.getEmailAddress()))
                    .child("meals");

            ArrayList<MealProfileModel> offeredMealModels = new ArrayList<MealProfileModel>();
            ArrayList<MealProfileModel> mealModels = new ArrayList<MealProfileModel>();
            ArrayList<Meal> offeredMeals = new ArrayList<Meal>();
            ArrayList<Meal> meals = new ArrayList<Meal>();

            longClickCounter = 0; //tracks the number of long clicks

            //the event listener is to update the recyclerview with each meal for display
            cookMeals.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    offeredMealModels.clear();
                    mealModels.clear();
                    offeredMeals.clear();
                    meals.clear();

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Meal meal = postSnapshot.getValue(Meal.class);

                        if (meal.isOffering()) {
                            offeredMeals.add(meal);
                            offeredMealModels.add(new MealProfileModel(meal.getName(), meal.getPrice(), meal.getRating(), meal.getNumSold()));
                        }
                        meals.add(meal);
                        mealModels.add(new MealProfileModel(meal.getName(), meal.getPrice(), meal.getRating(), meal.getNumSold()));
                    }

                    ProfileMealsAdapter offeredMealsAdapter = new ProfileMealsAdapter(PersonalProfile.this, offeredMealModels);
                    ProfileMealsAdapter mealsAdapter = new ProfileMealsAdapter(PersonalProfile.this, mealModels);

                    //creates a vertical list
                    LinearLayoutManager offeredMealsLayoutManager = new LinearLayoutManager(PersonalProfile.this, LinearLayoutManager.VERTICAL, false);
                    LinearLayoutManager mealsLayoutManager = new LinearLayoutManager(PersonalProfile.this, LinearLayoutManager.VERTICAL, false);

                    listOfferedMeals.setLayoutManager(offeredMealsLayoutManager);
                    listOfferedMeals.setAdapter(offeredMealsAdapter);

                    listMeals.setLayoutManager(mealsLayoutManager);
                    listMeals.setAdapter(mealsAdapter);

                    //allows for recyclerview items to be clicked (code from https://stackoverflow.com/questions/24471109/recyclerview-onclick)
                    //handles clicks and long clicks on the offeredMeals recyclerview
                    listOfferedMeals.addOnItemTouchListener(
                            new RecyclerItemClickListener(getApplicationContext(), listOfferedMeals ,new RecyclerItemClickListener.OnItemClickListener() {
                                //onclick of an item, pass the meal into the meal page to display all its information to the user
                                @Override public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(PersonalProfile.this, MealPage.class);
                                    intent.putExtra("meal", offeredMeals.get(position));
                                    startActivity(intent);
                                }

                                //onlongclick of an item, open a dialog with the option to remove the meal from the offered menu
                                //but keep it in the menu
                                @Override public void onLongItemClick(View view, int position) {
                                    if (longClickCounter == 0) {
                                        AlertDialog.Builder removeMeal = new AlertDialog.Builder(PersonalProfile.this);
                                        removeMeal.setCancelable(false);
                                        removeMeal.setTitle("Remove " + offeredMeals.get(position).getName() + " from Offered Menu?");
                                        removeMeal.setMessage("Click 'Remove' to stop offering this meal. The meal will still be on the Menu.");
                                        removeMeal.setPositiveButton("Remove", (DialogInterface.OnClickListener) (dialog, which) -> {
                                            Meal toRemove = offeredMeals.get(position);
                                            toRemove.setOffering(false);
                                            cookMeals.child(toRemove.getName()).setValue(toRemove);
                                            Toast.makeText(getApplicationContext(), "Meal Removed", Toast.LENGTH_LONG).show();
                                            longClickCounter = 0;
                                            //AP dialogue dismiss
                                            dialog.dismiss();
                                        });
                                        removeMeal.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
                                            longClickCounter = 0;
                                            dialog.dismiss();
                                        });
                                        removeMeal.create();
                                        removeMeal.show();
                                        longClickCounter++;
                                    }
                                }
                            })
                    );

                    //handles clicks and long clicks on the meals (the standard menu) recyclerview
                    listMeals.addOnItemTouchListener(
                            new RecyclerItemClickListener(getApplicationContext(), listMeals ,new RecyclerItemClickListener.OnItemClickListener() {
                                //onclick of an item, pass the meal into the meal page to display all its information to the user
                                @Override public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(PersonalProfile.this, MealPage.class);
                                    intent.putExtra("meal", meals.get(position));
                                    startActivity(intent);
                                }

                                //onlongclick of an item, open a dialog with the option to remove the meal from the offered menu
                                //but keep it in the menu
                                @Override public void onLongItemClick(View view, int position) {
                                    if (longClickCounter == 0) {
                                        AlertDialog.Builder offerDeleteMeal = new AlertDialog.Builder(PersonalProfile.this);
                                        offerDeleteMeal.setCancelable(false);
                                        offerDeleteMeal.setTitle("Offer or Delete " + meals.get(position).getName() + " from Menu?");
                                        offerDeleteMeal.setMessage("Choose the appropriate button below to offer or permanently delete the meal " +
                                                "from the menu.");
                                        offerDeleteMeal.setPositiveButton("Delete", (DialogInterface.OnClickListener) (dialog, which) -> {
                                            cookMeals.child(meals.get(position).getName()).removeValue();
                                            Toast.makeText(getApplicationContext(), "Meal Deleted", Toast.LENGTH_LONG).show();
                                            longClickCounter = 0;
                                            //AP
                                            dialog.dismiss();
                                        });
                                        offerDeleteMeal.setNegativeButton("Offer", (DialogInterface.OnClickListener) (dialog, which) -> {
                                            Meal toOffer = meals.get(position);
                                            toOffer.setOffering(true);
                                            cookMeals.child(toOffer.getName()).setValue(toOffer);
                                            Toast.makeText(getApplicationContext(), "Meal Now Offering", Toast.LENGTH_LONG).show();
                                            longClickCounter = 0;
                                            //AP
                                            dialog.dismiss();
                                        });
                                        offerDeleteMeal.setNeutralButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
                                            longClickCounter = 0;
                                            dialog.dismiss();
                                        });
                                        offerDeleteMeal.create();
                                        offerDeleteMeal.show();
                                        longClickCounter++;
                                    }
                                }
                            })
                    );
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}