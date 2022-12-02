package com.seg2105.mealer_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CookClientEvaluation extends AppCompatActivity {

    ListView listViewCooks; //
    List<Cook> cookslist;
    List<Cook> users;
    DatabaseReference database;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        database = FirebaseDatabase.getInstance().getReference("users");
        setContentView(R.layout.activity_cook_client_evaluation);
        listViewCooks = (ListView) findViewById(R.id.listViewCooks); //using same layout
        cookslist = new ArrayList<>();
        onItemLongClick();


        backButton = (Button) findViewById(R.id.btnBack);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
    protected void onStart(){
        super.onStart();

        //attaching value event listener
        //Query databaseProducts;
        database.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                //HERE
                cookslist.clear();


                //List<Cook> users;
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Cook cook = postSnapshot.getValue(Cook.class);

                   String role = cook.getRole();
                    Log.d("myTag", role);
                    Log.d("myTag2",String.valueOf(role.equals("Client")));
                   //users.add(cook);
                    if(role.equals("Cook")){
                        cookslist.add(cook);
                    }


                }


               /* for (int i =0; i<users.size();i++){
                    if(users.get(i).getRole()=="Cook"){
                        cookslist.add(users.get(i));
                    }
                }*/



                CookList cooksAdapter = new CookList(CookClientEvaluation.this, cookslist);
                listViewCooks.setAdapter(cooksAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError){
                //

            }


        });
    }



    private void onItemLongClick() {

        listViewCooks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cook cook = cookslist.get(i);


                showActionDialog(cook);
                //showActionDialog(complaint.getID(), complaint.getDescription());
                return true;
            }
        });
    }

    private void showActionDialog(Cook cook) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.action_dialog_rate_cook, null);
        dialogBuilder.setView(dialogView);
//FIX HERE

        dialogBuilder.setTitle(cook.getFirstName());
        final AlertDialog b = dialogBuilder.create();
        b.show();


       /* btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteComplaint(cook.getFirstName());
                b.dismiss();
            }



        });

        */


}

   /*private boolean deleteComplaint(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("complaints").child(id);
        dR.removeValue();

        Toast.makeText(getApplicationContext(), "Complaint Deleted", Toast.LENGTH_LONG).show();
        return true;
    }
    */
}