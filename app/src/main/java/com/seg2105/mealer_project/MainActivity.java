package com.seg2105.mealer_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //registerButton=findViewById(R.id.registerButton);
        //registerButton.setOnClickListener(this);
    }

        //call second activity when button pressed
        public void registerFormCall (View v) {
            Intent intent = new Intent(getApplicationContext(),
                    RegisterUser.class);
            startActivity(intent);
        }


}