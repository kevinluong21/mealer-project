package com.seg2105.mealer_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientRegister#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientRegister extends Fragment implements View.OnClickListener { //implements OnClickListener for fragment button


    Button backClient;
    EditText editTextFirstName; //first name text field
    EditText editTextLastName; //last name text field
    EditText editTextEmailAddress; //email address text field
    EditText editTextAddress; //address text field
    EditText editTextCreditCard; //credit card text field
    EditText editTextPassword; //password text field
    TextView textClientErrorMessage; //error message text view
    Button buttonClientRegister; //register button
    DatabaseReference users = MainActivity.getUsers(); //get user database from MainActivity
    Person currentUser = MainActivity.getCurrentUser(); //get currentUser from MainActivity
    //ConstraintLayout clientConsLayout;


    //MOST OF THE THINGS BELOW WAS GENERATE BY DEFAULT WHEN I CREATED FRAGMENT - UNSURE WHAT WE HAVE TO KEEP

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClientRegister() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClientRegister.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientRegister newInstance(String param1, String param2) {
        ClientRegister fragment = new ClientRegister();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //backButton=View.findViewById
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_client_register, container, false);
        backClient = rootView.findViewById(R.id.backClient);

        editTextFirstName = (EditText) rootView.findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) rootView.findViewById(R.id.editTextLastName);
        editTextEmailAddress = (EditText) rootView.findViewById(R.id.editTextEmailAddress);
        editTextAddress = (EditText) rootView.findViewById(R.id.editTextAddress);
        editTextCreditCard = (EditText) rootView.findViewById(R.id.editTextCreditCard);
        editTextPassword = (EditText) rootView.findViewById(R.id.editTextPassword);
        textClientErrorMessage = (TextView) rootView.findViewById(R.id.textClientErrorMessage);
        buttonClientRegister = (Button) rootView.findViewById(R.id.buttonClientRegister);

        buttonClientRegister.setOnClickListener(this); //allows register button to be clicked

        return rootView;
    }

    @Override
    public void onClick(View v) { //method to occur when register button is clicked
        registerClient(v);
    }

    public void registerClient(View v) { //still need to validate inputs
        String firstNameRaw = editTextFirstName.getText().toString().trim().toLowerCase(); //raw input from text field
        String lastNameRaw = editTextLastName.getText().toString().trim().toLowerCase(); //raw input from text field
        String emailAddressRaw = editTextEmailAddress.getText().toString().trim().toLowerCase(); //raw input from text field
        String emailAddress = MainActivity.emailAddressToKey(emailAddressRaw); //email address properly formatted to a firebase key
        String address = editTextAddress.getText().toString().trim();
        String creditCard = editTextCreditCard.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        textClientErrorMessage.setText("");

        if (!TextUtils.isEmpty(firstNameRaw) && !TextUtils.isEmpty(lastNameRaw) && !TextUtils.isEmpty(emailAddressRaw) && !TextUtils.isEmpty(address)
        && !TextUtils.isEmpty(creditCard) && !TextUtils.isEmpty(password)) {
            String firstName = firstNameRaw.substring(0, 1).toUpperCase() + firstNameRaw.substring(1); //basic capitalization of first letter of first name
            String lastName = lastNameRaw.substring(0, 1).toUpperCase() + lastNameRaw.substring(1); //basic capitalization of first letter of last name
            MainActivity.checkUser(emailAddress, new MyCallback<Person>() {
                @Override
                public void onCallback(Person user) {
                    if (user == null) { //no account exists yet with this email
                        Person newClient = new Client(firstName, lastName, emailAddress, password, address, creditCard);
                        users.child(emailAddress).setValue(newClient);
                        MainActivity.setCurrentUser(newClient);
                        Toast.makeText(getActivity(), "Registered as " + firstName + " " + lastName, Toast.LENGTH_LONG).show();
                    }
                    else { //account already exists with this email
                        textClientErrorMessage.setText("An account already exists with this email");
                    }
                }
            });
        }
        else { //at least one text field is empty
            textClientErrorMessage.setText("All fields must be filled");
        }
    }
}

    /*@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize your view here for use view.findViewById("your view id")
        clientConsLayout = view.findViewById(R.id.clientConstraintLayout);
    }*/
