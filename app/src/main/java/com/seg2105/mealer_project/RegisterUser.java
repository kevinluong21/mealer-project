package com.seg2105.mealer_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class RegisterUser extends AppCompatActivity {

    Button callClientFragment;
    Button callCookFragment;
    LinearLayout selectRoleText;
    LinearLayout layoutRoles;
    Button backClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        //fragment

        //getSupportFragmentManager().beginTransaction().add(R.id.container, new ClientRegister()).commit();
        callClientFragment = findViewById(R.id.callClientFragment);
        //callClientFragment.setOnClickListener(this);

        callCookFragment = findViewById(R.id.callCookFragment);
        //backClient = findViewById(R.id.backClient);
        //View v = inflater




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