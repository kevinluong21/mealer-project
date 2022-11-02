package com.seg2105.mealer_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;


public class UserWelcome extends Activity {

    Person currentUser = MainActivity.currentUser; //get currentUser from MainActivity
    Button btnAdminComplaints;
    //Cook cookUser;

    TextView textViewWelcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientwelcome);
        btnAdminComplaints = (Button) findViewById(R.id.btnAdminComplaints);


        textViewWelcomeMessage = findViewById(R.id.textViewWelcome);
        textViewWelcomeMessage.setText("Welcome " + currentUser.getFirstName() + "\n" + "You are logged in as a " + currentUser.getRole());
        if (currentUser.getRole().equals("Administrator")) {
            btnAdminComplaints.setVisibility(View.VISIBLE);
        }

        if (currentUser.getRole().equals("Cook")) {
            if (!MainActivity.loggedInCook.getAccountStatus()) {

            }
        }

        btnAdminComplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserWelcome.this, AdminComplaints.class);
                startActivity(intent);
            }
        });

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clientLogOffID:
                clientLogOff(v);
                break;
        }
    }

    public void clientLogOff(View v) {
        //CRASHES ONLY WHEN ASKED TO GO TO MAIN ACTIVITY
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void showSuspensionMessage() {
        AlertDialog.Builder suspensionDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.action_dialog, null);
        suspensionDialog.setView(dialogView);

        suspensionDialog.setTitle("Your account has been suspended");
        suspensionDialog.setMessage("");

        final AlertDialog b = suspensionDialog.create();
        b.show();
    }
}
