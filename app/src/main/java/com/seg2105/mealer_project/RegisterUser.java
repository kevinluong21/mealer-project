package com.seg2105.mealer_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/*this class is used for the activity_register_user fragment (choosing the role to register)*/

public class RegisterUser extends AppCompatActivity {

    Button callClientFragment;
    Button callCookFragment;
    LinearLayout selectRoleText;
    LinearLayout layoutRoles;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        callClientFragment = findViewById(R.id.callClientFragment);
        callCookFragment = findViewById(R.id.callCookFragment);
        selectRoleText = findViewById(R.id.layoutSelectRole);
        layoutRoles = findViewById(R.id.layoutRoles);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.icons8_chevron_left_24);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void RoleOnClick(View v){
        if (v.getId()==R.id.callClientFragment){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ClientRegister()).addToBackStack(null).commit();
            selectRoleText.setVisibility(View.GONE);
            layoutRoles.setVisibility(View.GONE);

        }
        if( v.getId() == R.id.callCookFragment){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new CookRegister()).addToBackStack(null).commit();
            selectRoleText.setVisibility(View.GONE);
            layoutRoles.setVisibility(View.GONE);
        }

    }

    //create toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //toolbar menu select action
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
            selectRoleText.setVisibility(View.VISIBLE);
            layoutRoles.setVisibility(View.VISIBLE);
        }

    }


}