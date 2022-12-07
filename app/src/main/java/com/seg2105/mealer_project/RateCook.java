package com.seg2105.mealer_project;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RateCook extends AppCompatActivity{

    RatingBar ratingBar;
    float rateValue;
    Button submitRatingBtn;
    String temp;
    double totalRating, averageRating;
    int numberofRatings = 0;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_dialog_rate_cook_stars);

        ratingBar = findViewById(R.id.ratingBar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                //rating passed
                rateValue = ratingBar.getRating();
            }
        });
        submitRatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalRating = totalRating+rateValue;
                numberofRatings +=1;
                //gets average rating to be displayed
                averageRating = totalRating/(numberofRatings*5);
            }
        });
    }
}
