package com.seg2105.mealer_project;

import static android.content.ContentValues.TAG;

import static com.seg2105.mealer_project.MainActivity.users;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.service.controls.templates.TemperatureControlTemplate;
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
    Button backToWelcomePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        database = FirebaseDatabase.getInstance().getReference("complaints");
        setContentView(R.layout.activity_admin_complaints);
        listViewComplaints = (ListView) findViewById(R.id.listViewComplaints);
        complaints = new ArrayList<>();
        onItemLongClick();


        backToWelcomePage = (Button) findViewById(R.id.btnBackToAdminWelcome);

        backToWelcomePage.setOnClickListener(new View.OnClickListener() {
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
        textViewClient.setText("Client: " + complaint.getClientEmail());
        textViewCook.setText("Cook: " + complaint.getCookEmail());
        textViewId.setVisibility(View.GONE);
        //textViewId.setText(complaint.getId());

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
                String date = editTextDate.getText().toString().trim();
                TextView textSuspendError = (TextView) findViewById(R.id.textSuspendError);

                textSuspendError.setText(""); //reset text view to empty

                if (TextUtils.isEmpty(date)) {
                    textSuspendError.setText("The date cannot be empty for a temporary suspension.");
                }
                else {
                    MainActivity.checkUser(complaint.getCookEmail(), new UserCallback<Administrator, Cook, Client>() {
                        @Override
                        public void onCallback(Administrator user1, Cook user2, Client user3) {
                            if (user2 != null){//if cook
                                user2.setAccountStatus(false);//deactivate account status of cook temporarily
                                user2.setSuspensionEndDate(date);
                                users.child(user2.getEmailAddress()).setValue(user2);
                                deleteComplaint(complaint.getId());
                                b.dismiss();
                            }
                        }
                    });
                }
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
                            users.child(user2.getEmailAddress()).setValue(user2);
                            deleteComplaint(complaint.getId());
                            b.dismiss();
                        }
                    }
                });
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