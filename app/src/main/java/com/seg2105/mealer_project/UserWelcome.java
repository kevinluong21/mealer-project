package com.seg2105.mealer_project;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


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
