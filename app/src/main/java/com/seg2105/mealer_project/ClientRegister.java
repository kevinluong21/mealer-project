package com.seg2105.mealer_project;

import android.content.Intent;
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


    Button btnBackClientToRegistration;
    EditText editTextFirstName; //first name text field
    EditText editTextLastName; //last name text field
    EditText editTextEmailAddress; //email address text field
    EditText editTextAddressNumber; //address number text field
    EditText editTextAddressStreet; //address street text field
    EditText editTextCreditCardNumber; //credit card number text field
    EditText editTextCreditCardCVV; //credit card CVV text field
    EditText editTextCreditCardExpiryDate; //credit card expiry date text field
    EditText editTextPassword; //password text field
    TextView textClientErrorMessage; //error message text view
    Button buttonClientRegister; //register button
    DatabaseReference users = MainActivity.getUsers(); //get user database from MainActivity
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
        btnBackClientToRegistration = (Button) rootView.findViewById(R.id.btnBackToRegistration);

        btnBackClientToRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getActivity(), RegisterUser.class);
                startActivity(intent);
            }
        });



        editTextFirstName = (EditText) rootView.findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) rootView.findViewById(R.id.editTextLastName);
        editTextEmailAddress = (EditText) rootView.findViewById(R.id.editTextEmailAddress);
        editTextAddressNumber = (EditText) rootView.findViewById(R.id.editTextAddressNumber);
        editTextAddressStreet = (EditText) rootView.findViewById(R.id.editTextAddressStreet);
        editTextCreditCardNumber = (EditText) rootView.findViewById(R.id.editTextCreditCardNumber);
        editTextCreditCardCVV = (EditText) rootView.findViewById(R.id.editTextCreditCardCVV);
        editTextCreditCardExpiryDate = (EditText) rootView.findViewById(R.id.editTextCreditCardExpiryDate);
        editTextPassword = (EditText) rootView.findViewById(R.id.editTextPassword);
        textClientErrorMessage = (TextView) rootView.findViewById(R.id.textClientErrorMessage);
        buttonClientRegister = (Button) rootView.findViewById(R.id.buttonClientRegister);

        buttonClientRegister.setOnClickListener(this); //allows register button to be clicked

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonClientRegister:
                registerClient(v);
                break;
        }
    }


    public void registerClient(View v) { //still need to validate inputs
        String firstNameRaw = editTextFirstName.getText().toString().trim().toLowerCase(); //raw input from text field
        String lastNameRaw = editTextLastName.getText().toString().trim().toLowerCase(); //raw input from text field
        String emailAddressRaw = editTextEmailAddress.getText().toString().trim().toLowerCase(); //raw input from text field
        String emailAddress = MainActivity.emailAddressToKey(emailAddressRaw); //email address properly formatted to a firebase key
        String addressNumber = editTextAddressNumber.getText().toString().trim();
        String addressStreet = editTextAddressStreet.getText().toString().trim();
        String creditCardNumber = editTextCreditCardNumber.getText().toString().trim();
        String creditCardCVV = editTextCreditCardCVV.getText().toString().trim();
        String creditCardExpiryDate = editTextCreditCardExpiryDate.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        textClientErrorMessage.setText("");

        if (inputValidation(firstNameRaw,lastNameRaw,emailAddress,addressNumber,addressStreet,creditCardNumber,creditCardCVV,creditCardExpiryDate,password) == true) {

            if (!TextUtils.isEmpty(firstNameRaw) && !TextUtils.isEmpty(lastNameRaw) && !TextUtils.isEmpty(emailAddressRaw) && !TextUtils.isEmpty(addressNumber)
                    && !TextUtils.isEmpty(addressNumber) && !TextUtils.isEmpty(creditCardNumber) && !TextUtils.isEmpty(creditCardCVV) && !TextUtils.isEmpty(creditCardExpiryDate) && !TextUtils.isEmpty(password)) {
                String firstName = firstNameRaw.substring(0, 1).toUpperCase() + firstNameRaw.substring(1); //basic capitalization of first letter of first name
                String lastName = lastNameRaw.substring(0, 1).toUpperCase() + lastNameRaw.substring(1); //basic capitalization of first letter of last name
                MainActivity.checkUser(emailAddress, new MyCallback<Administrator, Cook, Client>() {
                    @Override
                    public void onCallback(Administrator admin, Cook cook, Client client) {
                        if (admin == null && cook == null && client == null) { //no account exists yet with this email
                            Address address = new Address(addressNumber, addressStreet);
                            CreditCard card = new CreditCard(creditCardExpiryDate, creditCardNumber, creditCardCVV);
                            Client newClient = new Client(firstName, lastName, emailAddress, password, address, card);
                            users.child(emailAddress).setValue(newClient);
                            Toast.makeText(getActivity(), "Registered as " + firstName + " " + lastName, Toast.LENGTH_LONG).show();
                            //button navigation
                            Intent intent = new Intent(getActivity(), ClientWelcome.class);
                            startActivity(intent);
                        } else { //account already exists with this email
                            textClientErrorMessage.setText("An account already exists with this email");
                        }
                    }
                });
            } else { //at least one text field is empty
                textClientErrorMessage.setText("All fields must be filled");
            }
        }
    }


    protected static boolean checkCVV(String CVV){
        if(CVV.matches("[0-9]") && CVV.length() == 3){
            return true;
        }
        return false;
    }

    protected static boolean checkCCExp(String ccExp){
        if(ccExp.matches("[0-9][/]") && ccExp.length() == 5){
            return true;
        }
        return false;
    }

    protected static boolean checkCCNumber(String ccNum){
        if(ccNum.matches("[0-9]") && ccNum.length() == 16) {
            return true;
        }
        return false;
    }

    protected boolean inputValidation(String firstName, String lastName, String email, String addrNum,
                                      String addrStreet, String ccNum, String CVV, String ccExp, String password){
        if(CookRegister.checkName(firstName) == false) {
            textClientErrorMessage.setText("First name contains invalid characters");
            return false;
        }
        else{
            if(CookRegister.checkName(lastName) == false){
                textClientErrorMessage.setText("Last name contains invalid characters");
                return false;
            }
            else{
                if (CookRegister.checkEmail(email) == false){
                    textClientErrorMessage.setText("Email Address contains invalid characters");
                    return false;
                }
                else{
                    if(CookRegister.checkAddressNumber(addrNum) == false){
                        textClientErrorMessage.setText("Address number must contain only numbers");
                        return false;
                    }
                    else{
                        if(CookRegister.checkStreet(addrStreet) == false){
                            textClientErrorMessage.setText("Street name contains invalid characters");
                            return false;
                        }
                        else{
                            if(checkCCNumber(ccNum) == false){
                                textClientErrorMessage.setText("Credit card must be only numbers and be 16 characters long");
                                return false;
                            }
                            else{
                                if(checkCVV(CVV) == false){
                                    textClientErrorMessage.setText("CVV must be numbers and only 3 characters");
                                    return false;
                                }
                                else{
                                    if(checkCCExp(ccExp) == false){
                                        textClientErrorMessage.setText("Expiry date must be in format xx/xx");
                                        return false;
                                    }
                                    else{
                                        if(CookRegister.checkPassword(password) == false){
                                            textClientErrorMessage.setText("Password must be at least 8 characters long");
                                            return false;
                                        }
                                        else{
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

    /*@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize your view here for use view.findViewById("your view id")
        clientConsLayout = view.findViewById(R.id.clientConstraintLayout);
    }*/
