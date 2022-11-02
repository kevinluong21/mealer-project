package com.seg2105.mealer_project;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.service.controls.templates.TemperatureControlTemplate;
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

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//actionDialogue

public class AdminComplaints extends AppCompatActivity implements Serializable {

    ListView listViewComplaints;
    List<Complaint> complaints;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        database = FirebaseDatabase.getInstance().getReference("complaints");
        setContentView(R.layout.activity_admin_complaints);
        listViewComplaints = (ListView) findViewById(R.id.listViewComplaints);
        complaints = new ArrayList<>();
        onItemLongClick();

    }


    protected void onStart(){
        super.onStart();

        //attaching value event listener
        //Query databaseProducts;
        database.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                //
                complaints.clear();

                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Complaint complaint = postSnapshot.getValue(Complaint.class);
                    complaints.add(complaint);
                }

                ComplainList complaintsAdapter = new ComplainList(AdminComplaints.this, complaints);
                listViewComplaints.setAdapter(complaintsAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError){
                //

            }


        });
    }

    private void onItemLongClick() {

        listViewComplaints.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Complaint complaint = complaints.get(i);
                showActionDialog(complaint);
                //showActionDialog(complaint.getID(), complaint.getDescription());
                return true;
            }
        });
    }

    private void showActionDialog(Complaint complaint) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.action_dialog, null);
        dialogBuilder.setView(dialogView);


        final TextView textViewId = (TextView) dialogView.findViewById(R.id.textViewComplaintName);
        final TextView textViewClient = (TextView) dialogView.findViewById(R.id.textViewClient);
        final TextView textViewCook = (TextView) dialogView.findViewById(R.id.textViewCook);
        final TextView textViewDescription = (TextView) dialogView.findViewById((R.id.txtViewComplaintDescription));


        //final EditText editTextPrice = (EditText) dialogView.findViewById(R.id.dialog_editTextPrice);
        final Button btnDismiss = (Button) dialogView.findViewById(R.id.btnDismiss);
        final Button btnSuspendTemp = (Button) dialogView.findViewById(R.id.btnSuspendTemp);
        final Button btnSuspendPerm = (Button) dialogView.findViewById(R.id.btnSuspendPerm);

        //date edit text
        final EditText editTextDate = (EditText) dialogView.findViewById(R.id.editTextDate);

        textViewDescription.setText(String.valueOf(complaint.getDescription()));
        textViewClient.setText(complaint.getClientEmail());
        textViewCook.setText(complaint.getCookEmail());
        textViewId.setText(complaint.getId());

        dialogBuilder.setTitle(complaint.getId());
        final AlertDialog b = dialogBuilder.create();
        b.show();


        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteComplaint(complaint.getId());
                b.dismiss();
            }
        });


        btnSuspendTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //complaint.getCookEmail();
                //add a hind dd,mm,yyyy
                String date = editTextDate.getText().toString();
                MainActivity.checkUser(complaint.getCookEmail(), new UserCallback<Administrator, Cook, Client>() {
                    @Override
                    public void onCallback(Administrator user1, Cook user2, Client user3) {
                        if (user2 != null){//if cook
                            user2.setAccountStatus(false);//deactivate account status of cook temporarily
                            user2.setSuspensionEndDate(date);
                            b.dismiss();
                        }
                    }
                });
            }
        });

        btnSuspendPerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.checkUser(complaint.getCookEmail(), new UserCallback<Administrator, Cook, Client>() {
                    @Override
                    public void onCallback(Administrator user1, Cook user2, Client user3) {
                        if (user2 != null){//if cook
                            user2.setAccountStatus(false);//deactivate account status of cook temporarily
                            user2.setPermSuspension(true);
                            b.dismiss();
                        }
                    }
                });
            }
        });
    }

    public void showDatePickerDialogue(){
        DatePickerDialog.Builder dialogBuilder = new DatePickerDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.action_dialog, null);
        dialogBuilder.setView(dialogView);
    }

    private boolean deleteComplaint(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("complaints").child(id);
        dR.removeValue();

        Toast.makeText(getApplicationContext(), "Complaint Deleted", Toast.LENGTH_LONG).show();
        return true;
    }


}