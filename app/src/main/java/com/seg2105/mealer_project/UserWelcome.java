package com.seg2105.mealer_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class UserWelcome extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    Person currentUser = MainActivity.currentUser; //get currentUser from MainActivity
    Button btnAdminComplaints;
    //Cook cookUser;

    Button btnAddMeal;
    Button btnReviewOfferedMeals;

    TextView textViewWelcomeMessage;
    TextView textViewActionPrompt;

    ConstraintLayout layoutClient;
    ConstraintLayout layoutCook;
    LinearLayout layoutAdmin;

    DatabaseReference users;
    protected static ArrayList<Meal> meals;
    RecyclerView listClientMeals;
    ArrayList<MealListModel> mealModels;

    LinearLayout searchBar;

    BottomNavigationView bottomNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientwelcome);

        users = FirebaseDatabase.getInstance().getReference("users");
        listClientMeals = (RecyclerView) findViewById(R.id.listClientMeals);

        bottomNavBar = (BottomNavigationView) findViewById(R.id.bottomNavBar);
        bottomNavBar.setOnItemSelectedListener(this);
        bottomNavBar.setSelectedItemId(R.id.btnHome);

        layoutAdmin = (LinearLayout) findViewById(R.id.layoutAdmin);
        layoutClient = (ConstraintLayout) findViewById(R.id.layoutClient);
        layoutCook = (ConstraintLayout) findViewById(R.id.layoutCook);

        btnAdminComplaints = (Button) findViewById(R.id.btnAdminComplaints);
        btnAddMeal = (Button) findViewById(R.id.btnAddMeal);

        textViewWelcomeMessage = findViewById(R.id.textViewWelcome);
        textViewWelcomeMessage.setText("Hi " + currentUser.getFirstName() + ",");

        textViewActionPrompt = findViewById(R.id.textViewActionPrompt);
        textViewActionPrompt.setText("You are logged in as a " + currentUser.getRole());

        searchBar = (LinearLayout) findViewById(R.id.searchBar);
        searchBar.setOnClickListener(new View.OnClickListener() { //opens fragment with search and search results
            @Override
            public void onClick(View view) {
                openSearchResults(view);
            }
        });

        if (currentUser.getRole().equals("Administrator")) {
            layoutAdmin.setVisibility(View.VISIBLE);
            layoutClient.setVisibility(View.GONE);
            layoutCook.setVisibility(View.GONE);

            btnAdminComplaints.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(UserWelcome.this, AdminComplaints.class);
                    startActivity(intent);
                }
            });
        }


        if (currentUser.getRole().equals("Cook")) {
            layoutAdmin.setVisibility(View.GONE);
            layoutClient.setVisibility(View.GONE);
            layoutCook.setVisibility(View.VISIBLE);

            btnAddMeal.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(UserWelcome.this, MealActivity.class);
                    startActivity(intent);
                }
            } );

            if (!MainActivity.loggedInCook.getAccountStatus()) {
                AlertDialog.Builder suspensionDialog = new AlertDialog.Builder(this);
                suspensionDialog.setCancelable(false); //cannot close the dialog by clicking outside of the box

                try {
                    if (!MainActivity.loggedInCook.getPermSuspension()) { //temporary suspension
                        if (new SimpleDateFormat("dd/MM/yyyy").parse(MainActivity.loggedInCook.getSuspensionEndDate()).before(new Date())) { //suspension end date has passed
                            MainActivity.loggedInCook.setAccountStatus(true);
                            MainActivity.loggedInCook.setSuspensionEndDate(null);
                            MainActivity.users.child(MainActivity.loggedInCook.getEmailAddress()).setValue(MainActivity.loggedInCook); //update firebase value
                        }
                        else {
                            suspensionDialog.setTitle("Account Suspended");
                            suspensionDialog.setMessage("Your account has been temporarily suspended until " + MainActivity.loggedInCook.getSuspensionEndDate() +
                                    " due to a violation of our app's policy.");
                            suspensionDialog.setPositiveButton("Log Out", (DialogInterface.OnClickListener) (dialog, which) -> {
                                Intent i = new Intent(this, MainActivity.class);
                                startActivity(i);
                            });
                            suspensionDialog.create();
                            suspensionDialog.show();
                        }
                    }
                    else { //permanent suspension
                        suspensionDialog.setTitle("Account Suspended");
                        suspensionDialog.setMessage("Your account has been permanently suspended due to a severe violation of our app's policy.");
                        suspensionDialog.setPositiveButton("Log Out", (DialogInterface.OnClickListener) (dialog, which) -> {
                            Intent i = new Intent(this, MainActivity.class);
                            startActivity(i);
                        });
                        suspensionDialog.create();
                        suspensionDialog.show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        if (currentUser.getRole().equals("Client")) {
            layoutAdmin.setVisibility(View.GONE);
            layoutClient.setVisibility(View.VISIBLE);
            layoutCook.setVisibility(View.GONE);

            meals = new ArrayList<Meal>(); //for meal page
            mealModels = new ArrayList<MealListModel>(); //for recyclerview

            users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    meals.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.getValue(Administrator.class).getRole().equals("Cook")) {
                            Cook cook = dataSnapshot.getValue(Cook.class);
                            if (cook.getAccountStatus() && cook.getMeals() != null) { //meals must come from a cook that is non-suspended and has meals
                                for (Meal meal : cook.getMeals().values()) {
                                    if (meal.isOffering()) { //meal must be currently offered by the cook
                                        meals.add(meal);
                                        mealModels.add(new MealListModel(meal.getName(), meal.displayPrice(), meal.displayRating(), meal.getNumSold()));
                                    }
                                }
                            }
                        }
                    }

                    MealListAdapter mealListAdapter = new MealListAdapter(UserWelcome.this, mealModels);

                    //creates a vertical list
                    LinearLayoutManager mealListLayoutManager = new LinearLayoutManager(UserWelcome.this, LinearLayoutManager.VERTICAL, false);

                    listClientMeals.setLayoutManager(mealListLayoutManager);
                    listClientMeals.setAdapter(mealListAdapter);

                    //allows for recyclerview items to be clicked (code from https://stackoverflow.com/questions/24471109/recyclerview-onclick)
                    listClientMeals.addOnItemTouchListener(
                            new RecyclerItemClickListener(getApplicationContext(), listClientMeals ,new RecyclerItemClickListener.OnItemClickListener() {
                                //onclick of an item, pass the meal into the meal page to display all its information to the user
                                @Override public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(UserWelcome.this, MealPage.class);
                                    intent.putExtra("meal", meals.get(position));
                                    startActivity(intent);
                                }

                                @Override public void onLongItemClick(View view, int position) {
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

    //log off moved to PersonalProfile
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.clientLogOffID:
//                clientLogOff(v);
//                break;
//        }
//    }
//
//    public void clientLogOff(View v) {
//        //CRASHES ONLY WHEN ASKED TO GO TO MAIN ACTIVITY
//        Intent i = new Intent(this, MainActivity.class);
//        startActivity(i);
//    }

    public void openSearchResults(View v) {
        Intent i = new Intent(this, Search.class);
        startActivity(i);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnHome:
                return true;
            case R.id.btnProfile:
                startActivity(new Intent(getApplicationContext(),PersonalProfile.class));
                overridePendingTransition(0,0);
                return true;
        }
        return false;
    }
}
