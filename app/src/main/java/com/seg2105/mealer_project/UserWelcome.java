package com.seg2105.mealer_project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class UserWelcome extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    Person currentUser = MainActivity.currentUser; //get currentUser from MainActivity
    Button btnAdminComplaints;
    //Cook cookUser;

    Button btnAddMeal;

    TextView textViewWelcomeMessage;
    TextView textViewActionPrompt;

    ConstraintLayout layoutClient;
    ConstraintLayout layoutCook;
    LinearLayout layoutAdmin;

    DatabaseReference users;
    protected static ArrayList<Meal> meals;
    RecyclerView listClientMeals; //list that displays all the meals to the client
    ArrayList<MealListModel> mealModels;

    ArrayList<MealRequest> clientOrders; //stores all of a client's orders
    ArrayList<ClientOrderModel> clientOrderModels;
    RecyclerView listOrders; //list that displays all of a client's orders

    //cards for categories
    CardView cardMainDish;
    CardView cardSoup;
    CardView cardDessert;
    CardView cardItalian;

    RecyclerView orderRequests;
    RecyclerView acceptedOrders;

    LinearLayout searchBar;

    BottomNavigationView bottomNavBar;

    double rateValue;
    double totalRating;
    int numberofRatings = 0;
    double averageRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientwelcome);

        users = FirebaseDatabase.getInstance().getReference("users");
        listClientMeals = (RecyclerView) findViewById(R.id.listClientMeals);
        listOrders = (RecyclerView) findViewById(R.id.listOrders);
        orderRequests = (RecyclerView) findViewById(R.id.orderRequests);
        acceptedOrders = (RecyclerView) findViewById(R.id.acceptedOrders);

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

        //cardview categories
        cardMainDish = (CardView) findViewById(R.id.cardMainDish);
        cardSoup = (CardView) findViewById(R.id.cardSoup);
        cardDessert = (CardView) findViewById(R.id.cardDessert);
        cardItalian = (CardView) findViewById(R.id.cardItalian);

        cardMainDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserWelcome.this, Search.class);
                intent.putExtra("query", "Main Dish");
                startActivity(intent);
            }
        });

        cardSoup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserWelcome.this, Search.class);
                intent.putExtra("query", "Soup");
                startActivity(intent);
            }
        });

        cardDessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserWelcome.this, Search.class);
                intent.putExtra("query", "Dessert");
                startActivity(intent);
            }
        });

        cardItalian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserWelcome.this, Search.class);
                intent.putExtra("query", "Italian");
                startActivity(intent);
            }
        });

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
            btnAddMeal.setVisibility(View.GONE);

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
            btnAddMeal.setVisibility(View.VISIBLE);

            btnAddMeal.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(UserWelcome.this, MealActivity.class);
                    startActivity(intent);
                }
            } );

            if (!MainActivity.loggedInCook.getAccountStatus()) { //if a cook account IS SUSPENDED
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

            if (MainActivity.loggedInCook.getPurchaseRequests() != null) { //check that the orders are not null first
                ArrayList<MealRequest> requests = new ArrayList<MealRequest>();
                ArrayList<OrderModel> orderModels = new ArrayList<OrderModel>();

                users.child(currentUser.getEmailAddress()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Cook cook; //cook of meal

                        requests.clear();
                        orderModels.clear();

                        cook = snapshot.getValue(Cook.class);
                        if (cook.getPurchaseRequests() != null) {
                            for (MealRequest req : cook.getPurchaseRequests().values()) {
                                if (req.isActive()) { //might want to change this or add a new list for accepted but not completed meals
                                    requests.add(req);
                                    orderModels.add(new OrderModel(req));
                                }
                            }
                        }

                        OrderAdapter orderAdapter = new OrderAdapter(UserWelcome.this, orderModels);
                        LinearLayoutManager orderManager = new LinearLayoutManager(UserWelcome.this, LinearLayoutManager.VERTICAL, false);

                        orderRequests.setLayoutManager(orderManager);
                        orderRequests.setAdapter(orderAdapter);

                        orderRequests.addOnItemTouchListener(
                                new RecyclerItemClickListener(getApplicationContext(), orderRequests ,new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override public void onItemClick(View view, int position) {
                                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UserWelcome.this);
                                        dialogBuilder.setTitle(requests.get(position).getClientName()+" Purchase Request");
                                        dialogBuilder.setMessage("Would you like to accept or decline this meal request?");

                                        dialogBuilder.setPositiveButton("Accept",new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                requests.get(position).acceptRequest();

                                                cook.incrementSoldMeals(); //increment sold meals of meal cook
                                                users.child(cook.getEmailAddress()).setValue(cook); //update cook in firebase

                                                //replaces the request in firebase with the updated request
                                                //updates request in cook
                                                users.child(currentUser.getEmailAddress()).child("purchaseRequests").
                                                        child(requests.get(position).getMeal().getName()).setValue(requests.get(position));
                                                //updates request in client
                                                users.child(requests.get(position).getClientEmail()).child("requestedMeals").
                                                        child(requests.get(position).getMeal().getName()).setValue(requests.get(position));
//                                            requests.remove(position);
                                            }
                                        });
                                        dialogBuilder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                        dialogBuilder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                requests.get(position).declineRequest();
                                                //completely removes request from firebase
                                                users.child(currentUser.getEmailAddress()).child("purchaseRequests").
                                                        child(requests.get(position).getMeal().getName()).removeValue();

                                                //updates request in client
                                                users.child(requests.get(position).getClientEmail()).child("requestedMeals").
                                                        child(requests.get(position).getMeal().getName()).setValue(requests.get(position));
                                                requests.clear(); //allows the list to refresh to avoid a crash
                                            }
                                        });

                                        final AlertDialog dialog = dialogBuilder.create();
                                        dialog.show();
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
            if (MainActivity.loggedInCook.getPurchaseRequests() != null){
                ArrayList<MealRequest> accepted = new ArrayList<MealRequest>();
                ArrayList<AcceptedModel> acceptedModels = new ArrayList<AcceptedModel>();

                users.child(currentUser.getEmailAddress()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Cook cook;

                        accepted.clear();
                        acceptedModels.clear();

                        cook = snapshot.getValue(Cook.class);
                        for (MealRequest req : cook.getPurchaseRequests().values()) {
                            if (!req.isActive() && !req.isCompleted()) {
                                accepted.add(req);
                                acceptedModels.add(new AcceptedModel(req));
                            }
                        }
                        AcceptedAdapter acceptedAdapter = new AcceptedAdapter(UserWelcome.this, acceptedModels);
                        LinearLayoutManager acceptedManager = new LinearLayoutManager(UserWelcome.this, LinearLayoutManager.VERTICAL, false);

                        acceptedOrders.setLayoutManager(acceptedManager);
                        acceptedOrders.setAdapter(acceptedAdapter);

                        acceptedOrders.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), acceptedOrders, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                AlertDialog.Builder acceptedBuilder = new AlertDialog.Builder(UserWelcome.this);
                                acceptedBuilder.setTitle(accepted.get(position).getClientName()+" Meal Order");
                                acceptedBuilder.setMessage("Is this order completed?");

                                acceptedBuilder.setPositiveButton("Complete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        accepted.get(position).completeRequest();

                                        users.child(currentUser.getEmailAddress()).child("purchaseRequests").
                                                child(accepted.get(position).getMeal().getName()).setValue(accepted.get(position));

                                        users.child(accepted.get(position).getClientEmail()).child("requestedMeals").
                                                child(accepted.get(position).getMeal().getName()).setValue(accepted.get(position));
                                        accepted.clear();
                                    }
                                });
                                acceptedBuilder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                final AlertDialog dialog = acceptedBuilder.create();
                                dialog.show();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        }));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }

        else if (currentUser.getRole().equals("Client")) {
            layoutAdmin.setVisibility(View.GONE);
            layoutClient.setVisibility(View.VISIBLE);
            layoutCook.setVisibility(View.GONE);
            btnAddMeal.setVisibility(View.GONE);

            if (MainActivity.loggedInClient.getRequestedMeals() != null) { //check that client has orders
                clientOrders = new ArrayList<MealRequest>();
                clientOrderModels = new ArrayList<ClientOrderModel>();

                users.child(MainActivity.currentUser.getEmailAddress()).child("requestedMeals").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        clientOrders.clear();
                        clientOrderModels.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            MealRequest order = dataSnapshot.getValue(MealRequest.class);
                            clientOrders.add(order);
                            clientOrderModels.add(new ClientOrderModel(order));

                            //building notification
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

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_ID")
                                    .setSmallIcon(R.drawable.icons8_french_fries)
                                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH);
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                            switch(order.getProgress()) {
                                case "Your Order Is Being Prepared...":
                                    builder.setContentTitle("Your Order Is Being Prepared...");
                                    builder.setContentText("Hang tight! The cook is currently preparing your order.");
                                    notificationManager.notify(2, builder.build());
                                    break;

                                case "Your Order Is Ready For Pickup":
                                    builder.setContentTitle("Your Order Is Ready For Pickup");
                                    builder.setContentText("Your order is ready for pickup at " + order.getMeal().getCookAddress().formatAddress());
                                    notificationManager.notify(3, builder.build());

                                    AlertDialog.Builder dialogBuilderRateCook = new AlertDialog.Builder(UserWelcome.this);
                                    LayoutInflater inflaterRateCook = getLayoutInflater();
                                    final View dialogViewRateCook = inflaterRateCook.inflate(R.layout.action_dialog_rate_cook_stars, null);
                                    dialogBuilderRateCook.setView(dialogViewRateCook);

                                    final RatingBar ratingBar = (RatingBar) dialogViewRateCook.findViewById(R.id.ratingBar);
                                    final Button submitRating = (Button) dialogViewRateCook.findViewById(R.id.submitRatingBtn);

                                    final AlertDialog rateCook = dialogBuilderRateCook.create();
                                    rateCook.setCancelable(true);

                                    rateCook.show();

                                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                        @Override
                                        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                                            //rating passed
                                            rateValue = ratingBar.getRating();
                                        }
                                    });
                                    submitRating.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            totalRating = totalRating+rateValue;
                                            numberofRatings +=1;
                                            //gets average rating to be displayed
                                            averageRating = totalRating/(numberofRatings*5);
                                            rateCook.dismiss();

                                            order.getMeal().incrementNumberOfRatings();
                                            order.getMeal().incrementTotalRating(rateValue);
                                            order.getMeal().calculateRating();
                                            users.child(order.getCookEmail()).child("purchaseRequests").child(order.getMeal().getName()).setValue(order.getMeal());

                                            users.child(order.getClientEmail()).child("requestedMeals").child(order.getMeal().getName()).removeValue();

                                            MainActivity.checkUser(order.getCookEmail(), new UserCallback<Administrator, Cook, Client>() {
                                                @Override
                                                public void onCallback(Administrator user1, Cook user2, Client user3) {
                                                    user2.incrementTotalRating(rateValue);
                                                    user2.incrementNumberOfRatings();
                                                    user2.calculateRating();
                                                    MainActivity.users.child(user2.getEmailAddress()).setValue(user2);//update the cook again
                                                }
                                            });
                                        }
                                    });

                                    break;

                                case "Order Rejected":
                                    builder.setContentTitle("Order Rejected");
                                    builder.setContentText("Hey there! Unfortunately, the cook of this order cannot serve you at this time.");
                                    notificationManager.notify(4, builder.build());
                                    break;
                            }
                        }

                        ClientOrderAdapter clientOrderAdapter = new ClientOrderAdapter(UserWelcome.this, clientOrderModels);

                        //creates a vertical list
                        LinearLayoutManager clientOrderLayoutManager = new LinearLayoutManager(UserWelcome.this, LinearLayoutManager.VERTICAL, false);

                        listOrders.setLayoutManager(clientOrderLayoutManager);
                        listOrders.setAdapter(clientOrderAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            meals = new ArrayList<Meal>(); //for meal page
            mealModels = new ArrayList<MealListModel>(); //for recyclerview

            users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    meals.clear();
                    mealModels.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.getValue(Administrator.class).getRole().equals("Cook")) {
                            Cook cook = dataSnapshot.getValue(Cook.class);
                            if (cook.getAccountStatus() && cook.getMeals() != null) { //meals must come from a cook that is non-suspended and has meals
                                for (Meal meal : cook.getMeals().values()) {
                                    if (meal.isOffering()) { //meal must be currently offered by the cook
                                        meals.add(meal);
                                    }
                                }
                            }
                        }
                    }

                    //sort the meals by numSold and rating (meals with a higher numSold or rating appear first)
                    Collections.sort(meals, new Comparator<Meal>() {
                        @Override
                        public int compare(Meal meal1, Meal meal2) { //sort by numSold
                            if (meal1.getNumSold() != meal2.getNumSold()) {
                                Integer meal1Sold = meal1.getNumSold();
                                Integer meal2Sold = meal2.getNumSold();
                                return meal1Sold.compareTo(meal2Sold);
                            }
                            //if numSold is equal, sort by rating
                            Double meal1Rating = meal1.getRating();
                            Double meal2Rating = meal2.getRating();
                            return meal1Rating.compareTo(meal2Rating);
                        }
                    });
                    Collections.reverse(meals); //sort sorts lists by least to greatest so reverse it

                    for (Meal meal : meals) {
                        mealModels.add(new MealListModel(meal.getName(), meal.displayPrice(), meal.displayRating(), meal.getNumSold()));
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
