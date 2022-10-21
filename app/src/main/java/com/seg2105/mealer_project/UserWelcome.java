package com.seg2105.mealer_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class UserWelcome extends Activity {

    Person currentUser = MainActivity.currentUser; //get currentUser from MainActivity
    //Cook cookUser;

    TextView textViewWelcomeMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientwelcome);

        textViewWelcomeMessage= findViewById(R.id.textViewWelcome);
            textViewWelcomeMessage.setText("Welcome "+  currentUser.getFirstName() + "\n" +"You are logged in as a " + currentUser.getRole());


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clientLogOffID:
                clientLogOff(v);
                break;
        }
    }

    public void clientLogOff (View v) {
        //CRASHES ONLY WHEN ASKED TO GO TO MAIN ACTIVITY
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
