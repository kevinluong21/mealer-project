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
 * Use the {@link CookRegister#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CookRegister extends Fragment implements View.OnClickListener {

    Button backCook;
    EditText editTextFirstName; //first name text field
    EditText editTextLastName; //last name text field
    EditText editTextEmailAddress; //email address text field
    EditText editTextAddress; //address text field
    EditText editTextDescription; //credit card text field
    EditText editTextVoidCheque; //void cheque text field
    EditText editTextPassword; //password text field
    TextView textCookErrorMessage; //error message text view
    Button buttonCookRegister; //register button
    DatabaseReference users = MainActivity.getUsers(); //get user database from MainActivity
    Person currentUser = MainActivity.getCurrentUser(); //get currentUser from MainActivity

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CookRegister() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CookRegister.
     */
    // TODO: Rename and change types and number of parameters
    public static CookRegister newInstance(String param1, String param2) {
        CookRegister fragment = new CookRegister();
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
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_cook_register, container, false);
        backCook = rootView.findViewById(R.id.backCook);

        editTextFirstName = (EditText) rootView.findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) rootView.findViewById(R.id.editTextLastName);
        editTextEmailAddress = (EditText) rootView.findViewById(R.id.editTextEmailAddress);
        editTextAddress = (EditText) rootView.findViewById(R.id.editTextAddress);
        editTextDescription = (EditText) rootView.findViewById(R.id.editTextDescription);
        editTextVoidCheque = (EditText) rootView.findViewById(R.id.editTextVoidCheque);
        editTextPassword = (EditText) rootView.findViewById(R.id.editTextPassword);
        textCookErrorMessage = (TextView) rootView.findViewById(R.id.textCookErrorMessage);
        buttonCookRegister = (Button) rootView.findViewById(R.id.buttonCookRegister);

        buttonCookRegister.setOnClickListener(this); //allows register button to be clicked

        return rootView;
    }

    @Override
    public void onClick(View v) { //method to occur when register button is clicked
        registerCook(v);
    }

    public void registerCook(View v) { //still need to validate inputs
        String firstNameRaw = editTextFirstName.getText().toString().trim().toLowerCase(); //raw input from text field
        String lastNameRaw = editTextLastName.getText().toString().trim().toLowerCase(); //raw input from text field
        String emailAddressRaw = editTextEmailAddress.getText().toString().trim().toLowerCase(); //raw input from text field
        String emailAddress = MainActivity.emailAddressToKey(emailAddressRaw); //email address properly formatted to firebase key
        String address = editTextAddress.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String voidCheque = editTextVoidCheque.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        textCookErrorMessage.setText("");

        if (!TextUtils.isEmpty(firstNameRaw) && !TextUtils.isEmpty(lastNameRaw) && !TextUtils.isEmpty(emailAddressRaw) && !TextUtils.isEmpty(address)
                && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(voidCheque) && !TextUtils.isEmpty(password)) {
            String firstName = firstNameRaw.substring(0, 1).toUpperCase() + firstNameRaw.substring(1); //basic capitalization of first letter of first name
            String lastName = lastNameRaw.substring(0, 1).toUpperCase() + lastNameRaw.substring(1); //basic capitalization of first letter of last name
            MainActivity.checkUser(emailAddress, new MyCallback<Person>() {
                @Override
                public void onCallback(Person user) {
                    if (user == null) { //no account exists yet with this email
                        Person newClient = new Cook(firstName, lastName, emailAddress, password, description, address, voidCheque);
                        users.child(emailAddress).setValue(newClient);
                        MainActivity.setCurrentUser(newClient);
                        Toast.makeText(getActivity(), "Registered as " + firstName + " " + lastName, Toast.LENGTH_LONG).show();
                    }
                    else { //account already exists with this email
                        textCookErrorMessage.setText("An account already exists with this email");
                    }
                }
            });
        }
        else { //at least one text field is empty
            textCookErrorMessage.setText("All fields must be filled");
        }
    }
}