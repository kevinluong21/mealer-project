package com.seg2105.mealer_project;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends Activity {

    //Button registerButton;
    EditText editTextEmailAddress; //username text field
    EditText editTextPassword; //password text field
    Button buttonRegister; //button for registration
    Button buttonLogin; //button for login
    TextView textErrorMessage; //display text for error messages
    protected static DatabaseReference users; //refers to the Firebase database. used to read and write to database.
    protected static Person currentUser; //stores the current user that is logged in
    protected static Administrator loggedInAdmin; //stores the Administrator that is logged in
    protected static Cook loggedInCook; //stores the Cook that is logged in
    protected static Client loggedInClient; //stores the Client that is logged in

    protected static DatabaseReference complaints; // refers to firabase database the list of complaints


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmailAddress = (EditText) findViewById(R.id.editTextEmailAddress);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonRegister = (Button) findViewById(R.id.buttonClientRegister);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        textErrorMessage = (TextView) findViewById(R.id.textErrorMessage);

        users = FirebaseDatabase.getInstance().getReference("users"); //creates a list named "users" in the database
        complaints = FirebaseDatabase.getInstance().getReference("complaints"); //creates a list named "complaints" in the database

        //testing database and login
        Administrator admin = new Administrator("Support", "Admin", "admin", "123");
        users.child("admin").setValue(admin);



        // CREATING COMPLAINTS IN DB FOR DELIVERABLE 2
        // cook: test@gmail.com password
        //client: p@gmail.com password
        String clientEmail = "p@gmail.com";
        String cookEmail = "test@gmail.com";
        String description = "The cook never mentioned that the dish contains honey which lead to s severe alergic reaction. He did not reply to me after I tried to contact him";

        Complaint complaintFirst = new Complaint(description,clientEmail,cookEmail);
        complaints.child(complaintFirst.getId()).setValue(complaintFirst);

        Complaint complaintSecond = new Complaint("food was late","yay@person.com","ap@mail.com");
        Complaint complaintThird = new Complaint("cook was rude","yay@person.com","apetr@gmail.com");
        Complaint complaintFourth = new Complaint("didn't follow instructions about spiciness","testclient@gmail.com","chec@gmail.com");
        Complaint complaintFifth = new Complaint("food was cold","testclient@gmail.com","cook@gmail.com");
        complaints.child(complaintSecond.getId()).setValue(complaintSecond);
        complaints.child(complaintThird.getId()).setValue(complaintThird);
        complaints.child(complaintFourth.getId()).setValue(complaintFourth);
        complaints.child(complaintFifth.getId()).setValue(complaintFifth);



        //buttonRegister=findViewById(R.id.buttonRegister);
        //buttonRegister.setOnClickListener(this);
    }

    protected static DatabaseReference getUsers() { //method used across fragments
        return users;
    }

    protected static DatabaseReference getComplaints(){return complaints;}


    protected static String emailAddressToKey(String emailAddress) { //converts email address to a key (firebase does not allow certain characters in an email to be a key)
        emailAddress = emailAddress.replace(".", ",");
        return emailAddress;
    }

    protected static String keyToEmailAddress(String key) { //converts key (email without dot) back to the normal email format for display
        String email = key.replace(',', '.');
        return email;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                login(v);
                break;
        }
    }

    public void login(View v) { //still need to validate input
        String emailAddress = editTextEmailAddress.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        textErrorMessage.setText("");

        if (!TextUtils.isEmpty(emailAddress) && !TextUtils.isEmpty(password)) { //check if username and password are NOT empty
            checkUser(emailAddress, new UserCallback<Administrator, Cook, Client>() { //the MyCallback interface is used to deal with the asynchronous searching of the database
                //essentially, searching a database runs in the background so this interface ensures that we do not let it slip into the background
                //so that a value can be returned when it is ready and not just 'null'
                @Override
                //the idea of the MyCallback interface was taken from: https://stackoverflow.com/questions/47847694/how-to-return-datasnapshot-value-as-a-result-of-a-method/47853774
                //the implementation for the interface was done ourselves
                public void onCallback(Administrator admin, Cook cook, Client client) { //waits until the search is done and the user that was found is returned (as an argument)
                    /*checkUser returns null if the user is not of that role and returns the actual value in the argument where the user is of that role
                    * (e.g. if a Client is found, the return would be (null, null, clientFound)
                    * this approach was used to maintain the original class of the object being passed as an argument*/
                    if (admin != null || cook != null || client != null) { //match found as one of 3 classes
                        if (admin != null) {
                            currentUser = admin;
                            loggedInAdmin = admin;
                        }
                        else if (cook != null) {
                            currentUser = cook;
                            loggedInCook = cook;
                        }
                        else {
                            currentUser = client;
                            loggedInClient = client;
                        }
                        if (!currentUser.accountPassword.equals(password)) { //passwords do not match
                            textErrorMessage.setText("Incorrect password");
                            currentUser = null;
                        } else { //correct username and password
                            Toast.makeText(MainActivity.this, "Signed in as " + currentUser.firstName + " " + currentUser.lastName, Toast.LENGTH_LONG).show(); //display Toast of successful log in
                            //goes to the welcome page
                            //CRASHES NOT SURE WHY
                            Intent i = new Intent(MainActivity.this, UserWelcome.class);
                            startActivity(i);
                        }
                    } else { //user is not found in database (it is not an admin, cook, or client)
                        textErrorMessage.setText("An account with this email does not exist");
                    }
                }
            });
        } else { //username and password text fields are empty
            textErrorMessage.setText("Email address and password cannot be empty");
        }
    }

    protected static void checkUser(String emailAddress, UserCallback<Administrator, Cook, Client> userCallback) { //check for user in database by searching using the email and stores the match in the currentUser object
        emailAddress = emailAddressToKey(emailAddress); //it can be called outside, but this ensures that if it is forgotten, it does not throw an error
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference childRef = users.child(emailAddress);
        childRef.addListenerForSingleValueEvent(new ValueEventListener() { //reads the database for all children with the matching email address
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /*temporarily retrieves child as an Administrator object, checks the role, and then gets the value as
                the role stored in the database (since getValue will return a database child as an object of the class
                that is passed to it)
                we chose to use an Administrator class because Person is an abstract class and
                the role variable is present in the Cook and Client classes of Administrator as well and is accessible.
                */
                if (snapshot.exists()) {
                    if (snapshot.getValue(Administrator.class).getRole().equals("Administrator")) {
                        Administrator admin = snapshot.getValue(Administrator.class);
                        userCallback.onCallback(admin, null, null);
                    }
                    else if (snapshot.getValue(Administrator.class).getRole().equals("Cook")) {
                        Cook cook = snapshot.getValue(Cook.class);
                        userCallback.onCallback(null, cook, null);
                    }
                    else if (snapshot.getValue(Administrator.class).getRole().equals("Client")) {
                        Client client = snapshot.getValue(Client.class);
                        userCallback.onCallback(null, null, client);
                    }
                }
                else { //user is not found so call the method with a null Person object (null means no user was found)
                    userCallback.onCallback(null, null, null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    //call second activity when button pressed
    public void registerFormCall(View v) {
        Intent intent = new Intent(getApplicationContext(),
                RegisterUser.class);
        startActivity(intent);
    }


}