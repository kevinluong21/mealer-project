package com.seg2105.mealer_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.service.controls.templates.TemperatureControlTemplate;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.List;

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
        final Button btnSuspend = (Button) dialogView.findViewById(R.id.btnSuspendCook);

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
    }

    private boolean deleteComplaint(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("complaints").child(id);
        dR.removeValue();

        Toast.makeText(getApplicationContext(), "Complaint Deleted", Toast.LENGTH_LONG).show();
        return true;
    }


}