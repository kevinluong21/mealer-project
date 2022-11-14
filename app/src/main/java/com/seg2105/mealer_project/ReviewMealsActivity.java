package com.seg2105.mealer_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.os.Bundle;
import android.service.controls.templates.TemperatureControlTemplate;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReviewMealsActivity extends AppCompatActivity implements Serializable {


    ListView listViewMeals;
    List<Meal> mealsList;
    DatabaseReference database;
    Button btnBackHomeToCook;
    Person currentCook = MainActivity.currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        database = FirebaseDatabase.getInstance().getReference(); //access the path where the meals are stored
        setContentView(R.layout.activity_admin_complaints);
        listViewMeals = (ListView) findViewById(R.id.listViewComplaints);
        mealsList = new ArrayList<>();

        btnBackHomeToCook = (Button) findViewById(R.id.btnBackToHomeCook);

        /*btnBackHomeToCook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

    }

    protected void onStart(){
        super.onStart();

        //attaching value event listener
        //Query databaseProducts;
        MainActivity.users.child(currentCook.getEmailAddress()).child("meals").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                //
                mealsList.clear();

                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    //Complaint complaint = postSnapshot.getValue(Complaint.class);
                    Meal meal = postSnapshot.getValue(Meal.class);
                    mealsList.add(meal);
                }

                MealsList mealsAdapter = new MealsList(ReviewMealsActivity.this, mealsList);
                listViewMeals.setAdapter(mealsAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError){
                //

            }


        });

        /*database.child("users").child(currentCook.getEmailAddress()).child("meals").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
        }*/




}}