package com.seg2105.mealer_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/*this class is used for the activity_register_user fragment (choosing the role to register)*/

public class RegisterUser extends AppCompatActivity {

    Button callClientFragment;
    Button callCookFragment;
    LinearLayout selectRoleText;
    LinearLayout layoutRoles;
    Button btnBackToMain;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        //fragment

        //getSupportFragmentManager().beginTransaction().add(R.id.container, new ClientRegister()).commit();
        callClientFragment = findViewById(R.id.callClientFragment);
        //callClientFragment.setOnClickListener(this);

        callCookFragment = findViewById(R.id.callCookFragment);
        btnBackToMain = (Button) findViewById(R.id.btnBackToMain);

        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





        selectRoleText = findViewById(R.id.layoutSelectRole);
        layoutRoles = findViewById(R.id.layoutRoles);

    }

    public void RoleOnClick(View v){
        if (v.getId()==R.id.callClientFragment){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ClientRegister()).commit();
            //callClientFragment.setVisibility(View.GONE);
            //activity_register_user.setVisibility (View.GONE);
            selectRoleText.setVisibility(View.GONE);
            layoutRoles.setVisibility(View.GONE);

        }
        if( v.getId() == R.id.callCookFragment){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new CookRegister()).commit();
            selectRoleText.setVisibility(View.GONE);
            layoutRoles.setVisibility(View.GONE);
        }

    }


}