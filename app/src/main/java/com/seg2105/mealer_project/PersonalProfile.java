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
    DatabaseReference cookMeals;

    public PersonalProfile() {
        super(R.layout.activity_personal_profile);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);

        listOfferedMeals = (RecyclerView) findViewById(R.id.listOfferedMeals);
        listMeals = (RecyclerView) findViewById(R.id.listMeals);

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

        textName.setText(MainActivity.currentUser.getFirstName() + " " + MainActivity.currentUser.getLastName());
        textRole.setText(MainActivity.currentUser.getRole());

        listOfferedMeals.setVisibility(View.GONE);
        listMeals.setVisibility(View.GONE);

        if (MainActivity.currentUser.getRole().equals("Cook")) {
            listOfferedMeals.setVisibility(View.VISIBLE);
            listMeals.setVisibility(View.VISIBLE);

            cookMeals = FirebaseDatabase.getInstance().getReference("users").child(MainActivity.emailAddressToKey(MainActivity.loggedInCook.getEmailAddress()))
                    .child("meals");

            ArrayList<MealProfileModel> offeredMealModels = new ArrayList<MealProfileModel>();
            ArrayList<MealProfileModel> mealModels = new ArrayList<MealProfileModel>();
            ArrayList<Meal> offeredMeals = new ArrayList<Meal>();
            ArrayList<Meal> meals = new ArrayList<Meal>();

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
                    listOfferedMeals.addOnItemTouchListener(
                            new RecyclerItemClickListener(getApplicationContext(), listOfferedMeals ,new RecyclerItemClickListener.OnItemClickListener() {
                                @Override public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(PersonalProfile.this, MealPage.class);
                                    intent.putExtra("meal", offeredMeals.get(position));
                                    startActivity(intent);
                                }

                                @Override public void onLongItemClick(View view, int position) {
                                    AlertDialog.Builder removeMeal = new AlertDialog.Builder(getApplicationContext());
                                    removeMeal.setCancelable(true);
                                    removeMeal.setTitle("Remove " + offeredMeals.get(position).getName() + " from Offered Menu?");
                                    removeMeal.setPositiveButton("Remove", (DialogInterface.OnClickListener) (dialog, which) -> {
                                        cookMeals.child(offeredMeals.get(position).getName()).removeValue();
                                        Toast.makeText(getApplicationContext(), "Meal Removed", Toast.LENGTH_LONG).show();
                                    });
                                    removeMeal.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
                                        dialog.dismiss();
                                    });
                                }
                            })
                    );

                    listMeals.addOnItemTouchListener(
                            new RecyclerItemClickListener(getApplicationContext(), listMeals ,new RecyclerItemClickListener.OnItemClickListener() {
                                @Override public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(PersonalProfile.this, MealPage.class);
                                    intent.putExtra("meal", meals.get(position));
                                    startActivity(intent);
                                }

                                @Override public void onLongItemClick(View view, int position) {
                                    // do whatever
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