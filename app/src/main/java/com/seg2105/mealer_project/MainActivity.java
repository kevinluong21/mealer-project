package com.seg2105.mealer_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {

    //Button registerButton;
    EditText editTextEmailAddress; //username text field
    EditText editTextPassword; //password text field
    Button buttonRegister; //button for registration
    Button buttonLogin; //button for login
    TextView textErrorMessage; //display text for error messages
    protected static DatabaseReference users; //refers to the Firebase database. used to read and write to database.
    protected static Person currentUser; //stores the current user logged in

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

        //testing database and login
        Administrator admin = new Administrator("Support", "Admin", "admin", "123");
        users.child("admin").setValue(admin);

        //buttonRegister=findViewById(R.id.buttonRegister);
        //buttonRegister.setOnClickListener(this);
    }

    protected static DatabaseReference getUsers() { //method used across fragments
        return users;
    }

    protected static Person getCurrentUser() { //method used across fragments
        return currentUser;
    }

    protected static void setCurrentUser(Person newUser) { //method used across fragments
        currentUser = newUser;
    }

    public void login(View v) { //still need to validate input
        String emailAddress = editTextEmailAddress.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        currentUser = null;
        textErrorMessage.setText("");

        if (!TextUtils.isEmpty(emailAddress) && !TextUtils.isEmpty(password)) { //check if username and password are NOT empty
            checkUser(emailAddress, new MyCallback<Person>() { //the MyCallback interface is used to deal with the asynchronous searching of the database
                //essentially, searching a database runs in the background so this interface ensures that we do not let it slip into the background
                //so that a value can be returned when it is ready and not just 'null'
                @Override
                public void onCallback(Person user) { //waits until the search is done and the user that was found is returned (as an argument)
                    //if no user is found, return null
                    if (user != null) { //match found
                        currentUser = user;
                        if (!currentUser.accountPassword.equals(password)) { //check that the passwords match
                            textErrorMessage.setText("Incorrect password");
                        } else { //correct username and password
                            Toast.makeText(MainActivity.this, "Signed in as " + currentUser.firstName + " " + currentUser.lastName, Toast.LENGTH_LONG).show(); //display Toast of successful log in
                        }
                    } else { //user is not found in database (it is null)
                        textErrorMessage.setText("An account with this email does not exist");
                    }
                }
            });
        } else { //username and password text fields are empty
            textErrorMessage.setText("Email address and password cannot be empty");
        }
    }

    protected static void checkUser(String emailAddress, MyCallback<Person> myCallback) { //check for user in database by searching using the email and stores the match in the currentUser object
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
        users.child(emailAddress).addListenerForSingleValueEvent(new ValueEventListener() { //reads the database for all children with the matching email address
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Person user; //temporary Person object
                if (snapshot.getValue(Administrator.class) != null) { //checks if the user is an Administrator object
                    user = snapshot.getValue(Administrator.class);
                    myCallback.onCallback(user); //calls the onCallback method defined in the myCallback interface that was passed as an argument to this method
                } else if (snapshot.getValue(Cook.class) != null) { //checks if the user is a Cook object
                    user = snapshot.getValue(Cook.class);
                    myCallback.onCallback(user);
                } else if (snapshot.getValue(Client.class) != null) { //checks if the user is a Client object
                    user = snapshot.getValue(Client.class);
                    myCallback.onCallback(user);
                }
                else { //user is not found so call the method with a null Person object (null means no user was found)
                    myCallback.onCallback(null);
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