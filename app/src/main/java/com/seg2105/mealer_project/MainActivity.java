package com.seg2105.mealer_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    //Button registerButton;
    EditText editTextEmailAddress; //username text field
    EditText editTextPassword; //password text field
    Button buttonRegister; //button for registration
    Button buttonLogin; //button for login
    TextView textErrorMessage; //display text for error messages
    protected DatabaseReference users; //refers to the Firebase database. used to read and write to database.
    protected Person currentUser; //stores the current user logged in

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmailAddress = (EditText) findViewById(R.id.editTextEmailAddress);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        textErrorMessage = (TextView) findViewById(R.id.textErrorMessage);

        users = FirebaseDatabase.getInstance().getReference("users"); //creates a list named "users" in the database

        //testing database and login
        Administrator admin = new Administrator("Support", "Admin", "support", "123");
        users.child("admin").setValue(admin);

        //buttonRegister=findViewById(R.id.buttonRegister);
        //buttonRegister.setOnClickListener(this);
    }

    public void login(View v) { //still need to validate input
        String emailAddress = editTextEmailAddress.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(emailAddress) && !TextUtils.isEmpty(password)) { //check if username and password are NOT empty
            checkClient(emailAddress, password);
            if (currentUser != null) { //match found so currentUser stores the user from the database
                if (!currentUser.accountPassword.equals(password)) { //check that the passwords match
                    textErrorMessage.setText("Incorrect password");
                }
                else { //correct username and password
                    Toast.makeText(this, "Signed in as " + currentUser.firstName, Toast.LENGTH_LONG).show(); //display Toast of successful log in
                }
            }
            else { //user is not found in database
                textErrorMessage.setText("An account with this email does not exist");
            }
        }
        else { //username and password text fields are empty
            textErrorMessage.setText("Email address and password cannot be empty");
        }

    }
    protected void checkClient(String emailAddress, String password) { //check for user in database by searching using the email and stores the match in the currentUser object
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
        users.orderByKey().equalTo(emailAddress).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                /*the getValue method returns an object of the inputted class. however, if Person.class is
                *inputted, an error will occur because Person is not an instantiable object. as a
                * temporary workaround, we must test that the returns of getValue are not null for
                * each class because if a user exists but it is not of that class,
                * it will return null and currentUser will then store null (which indicates
                * that there was no user in the database in the login method). */

                if (snapshot.getValue(Administrator.class) != null) {
                    currentUser = snapshot.getValue(Administrator.class);
                }
                else if (snapshot.getValue(Cook.class) != null) {
                    currentUser = snapshot.getValue(Cook.class);
                }
                else if (snapshot.getValue(Client.class) != null) {
                    currentUser = snapshot.getValue(Client.class);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //call second activity when button pressed
    public void registerFormCall(View v) {
        Intent intent = new Intent(getApplicationContext(),
                RegisterUser.class);
        startActivity(intent);
    }


}