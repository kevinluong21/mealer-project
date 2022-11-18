//the code for implementing the recyclerview comes from https://www.geeksforgeeks.org/cardview-using-recyclerview-in-android-with-example/

package com.seg2105.mealer_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

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
    DatabaseReference cookMeals = FirebaseDatabase.getInstance().getReference("users").child(MainActivity.emailAddressToKey(MainActivity.loggedInCook.getEmailAddress()))
            .child("meals");

    public PersonalProfile() {
        super(R.layout.activity_personal_profile);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);

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

        textName.setText(MainActivity.loggedInCook.getFirstName() + " " + MainActivity.loggedInCook.getLastName());
        textRole.setText(MainActivity.loggedInCook.getRole());

        if (MainActivity.currentUser.getRole().equals("Cook")) {
            listOfferedMeals = (RecyclerView) findViewById(R.id.listOfferedMeals);
            listMeals = (RecyclerView) findViewById(R.id.listMeals);
            ArrayList<MealProfileModel> offeredMeals = new ArrayList<MealProfileModel>();
            ArrayList<MealProfileModel> meals = new ArrayList<MealProfileModel>();

            cookMeals.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    offeredMeals.clear();

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Meal meal = postSnapshot.getValue(Meal.class);

                        if (meal.isOffering()) {
                            offeredMeals.add(new MealProfileModel(meal.getName(), meal.getPrice(), meal.getRating(), meal.getNumSold()));
                        }
                        meals.add(new MealProfileModel(meal.getName(), meal.getPrice(), meal.getRating(), meal.getNumSold()));
                    }

                    ProfileMealsAdapter offeredMealsAdapter = new ProfileMealsAdapter(PersonalProfile.this, offeredMeals);
                    ProfileMealsAdapter mealsAdapter = new ProfileMealsAdapter(PersonalProfile.this, meals);

                    //creates a vertical list
                    LinearLayoutManager offeredMealsLayoutManager = new LinearLayoutManager(PersonalProfile.this, LinearLayoutManager.VERTICAL, false);
                    LinearLayoutManager mealsLayoutManager = new LinearLayoutManager(PersonalProfile.this, LinearLayoutManager.VERTICAL, false);

                    listOfferedMeals.setLayoutManager(offeredMealsLayoutManager);
                    listOfferedMeals.setAdapter(offeredMealsAdapter);

                    listMeals.setLayoutManager(mealsLayoutManager);
                    listMeals.setAdapter(mealsAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}