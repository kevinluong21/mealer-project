package com.seg2105.mealer_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
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
    DatabaseReference complaints;
    Button backButton;

//    Button submitComplain;
//    EditText complaintText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        database = FirebaseDatabase.getInstance().getReference("users");
        complaints = FirebaseDatabase.getInstance().getReference("complaints"); //creates a list named "complaints" in the database

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

                //FIX limit the cooks that actually got orders
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

        dialogBuilder.setTitle(cook.getFirstName());
        final AlertDialog b = dialogBuilder.create();
        b.show();

        final Button submitComplain = (Button) dialogView.findViewById((R.id.btnComplain));
        final EditText complaintText=(EditText) dialogView.findViewById(R.id.editTextComplain);
        final TextView errorMessage = (TextView) dialogView.findViewById(R.id.textViewErrorMessage);
       // String com = "Test";
//        Log.d("MyTag1",complaintDescription);
//        Log.d("MyTag2",String.valueOf(complaintDescription.getClass()));

//        submitComplain = (Button) findViewById((R.id.btnComplain));
//        complaintText=(EditText) findViewById(R.id.editTextComplain);


            submitComplain.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String complaintDescription = complaintText.getText().toString().trim();
                    if(!TextUtils.isEmpty(complaintDescription)){
                    Log.d("MyTag3",complaintDescription);
                    makeComplaint(complaintDescription, cook);
                    b.dismiss();
                }
                    else {
                        errorMessage.setText("All fields must be filled");
                    }
            }});

        }


//    Complaint complaintFirst = new Complaint(description,clientEmail,cookEmail, "ComplaintNumber1");
//        complaints.child(complaintFirst.getId()).setValue(complaintFirst);

    private boolean makeComplaint(String text, Cook cook) {

        //DatabaseReference dR = FirebaseDatabase.getInstance().getReference("complaints").child(id);
        String complaintID= ("Complaint received: " + System.currentTimeMillis()/1000);
        Complaint newComplaint = new Complaint(text,PersonalProfile.user.getEmailAddress(),cook.getEmailAddress(),complaintID );
        complaints.child(newComplaint.getId()).setValue(newComplaint);

        Toast.makeText(getApplicationContext(), "Complaint submittes", Toast.LENGTH_LONG).show();
        return true;
    }





   /*private boolean deleteComplaint(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("complaints").child(id);
        dR.removeValue();

        Toast.makeText(getApplicationContext(), "Complaint Deleted", Toast.LENGTH_LONG).show();
        return true;
    }
    */
}