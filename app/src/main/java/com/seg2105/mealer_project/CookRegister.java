package com.seg2105.mealer_project;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import android.widget.EditText;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CookRegister#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CookRegister extends Fragment implements View.OnClickListener {


    Button btnBackCookToRegistration;
    EditText editTextFirstName; //first name text field
    EditText editTextLastName; //last name text field
    EditText editTextEmailAddress; //email address text field
    EditText editTextAddressNumber; //address number text field
    EditText editTextAddressStreet; //address street text field
    EditText editTextDescription; //credit card text field
    EditText editTextPassword; //password text field
    TextView textCookErrorMessage; //error message text view
    Button buttonCookRegister; //register button
    DatabaseReference users = MainActivity.getUsers(); //get user database from MainActivity
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference imageStorage = storage.getReference();
    Bitmap voidChequeBitmap;


    //AP uploading the image
    Button btnSelectVoidCheque;
    ImageView imgViewPreviewSelectedImage;
    int SELECT_PICTURE = 200;
    private Uri filePath;

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
        btnBackCookToRegistration = (Button) rootView.findViewById(R.id.btnBackToRegistration);

        btnBackCookToRegistration.setOnClickListener(new View.OnClickListener() {
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
        editTextDescription = (EditText) rootView.findViewById(R.id.editTextDescription);
        editTextPassword = (EditText) rootView.findViewById(R.id.editTextPassword);
        textCookErrorMessage = (TextView) rootView.findViewById(R.id.textCookErrorMessage);
        buttonCookRegister = (Button) rootView.findViewById(R.id.buttonCookRegister);

        buttonCookRegister.setOnClickListener(this); //allows register button to be clicked


        imgViewPreviewSelectedImage = (ImageView) rootView.findViewById(R.id.imgViewCheque);

        btnSelectVoidCheque = (Button) rootView.findViewById(R.id.btnSelectVoidCheque);
        btnSelectVoidCheque.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                imageChooser();
            }

        });

        return rootView;
    }

    public void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Picture"),SELECT_PICTURE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                voidChequeBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imgViewPreviewSelectedImage.setImageBitmap(voidChequeBitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }

    public void uploadImage(String emailAddress, ImageCallback<String> imageCallback) {
        //upload the void cheque picture to the firebase cloud storage
        StorageReference voidCheque = imageStorage.child("voidCheque/" + emailAddress + ".jpg"); //creates a new reference in the firebase cloud storage
        //the email address will be used as the file name
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        voidChequeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //compress bitmap to be a jpg image
        byte[] voidChequeData = baos.toByteArray();

        UploadTask uploadTask = voidCheque.putBytes(voidChequeData); //the task to upload the image into the storage reference
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                String errorMessage = exception.getMessage();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                voidCheque.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() { //get the URL of the image in the database to store in the realtime database
                    @Override
                    public void onSuccess(Uri uri) {
                        imageCallback.onCallback(uri.toString());
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCookRegister:
                registerCook(v);
                break;
        }
    }


    public void registerCook(View v) { //still need to validate inputs
        String firstNameRaw = editTextFirstName.getText().toString().trim().toLowerCase(); //raw input from text field
        String lastNameRaw = editTextLastName.getText().toString().trim().toLowerCase(); //raw input from text field
        String emailAddressRaw = editTextEmailAddress.getText().toString().trim().toLowerCase(); //raw input from text field
        String emailAddress = MainActivity.emailAddressToKey(emailAddressRaw); //email address properly formatted to firebase key
        String addressNumber = editTextAddressNumber.getText().toString().trim();
        String addressStreet = editTextAddressStreet.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        textCookErrorMessage.setText("");

        if(inputValidation(firstNameRaw,lastNameRaw,emailAddressRaw,addressNumber,addressStreet,password) == true){

            if (!TextUtils.isEmpty(firstNameRaw) && !TextUtils.isEmpty(lastNameRaw) && !TextUtils.isEmpty(emailAddressRaw) && !TextUtils.isEmpty(addressNumber)
                    && !TextUtils.isEmpty(addressStreet) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(password)) {
                String firstName = firstNameRaw.substring(0, 1).toUpperCase() + firstNameRaw.substring(1); //basic capitalization of first letter of first name
                String lastName = lastNameRaw.substring(0, 1).toUpperCase() + lastNameRaw.substring(1); //basic capitalization of first letter of last name
                MainActivity.checkUser(emailAddress, new UserCallback<Administrator, Cook, Client>() {
                    @Override
                    public void onCallback(Administrator admin, Cook cook, Client client) {
                        if (admin == null && cook == null && client == null) { //no account exists yet with this email
                            Address address = new Address(addressNumber, addressStreet); //create a new address object
                            uploadImage(emailAddress, new ImageCallback<String>() { //retrieving an image URL is an async process so a method must be implemented to return the URL
                                //on success
                                @Override
                                public void onCallback(String imageURL) { //imageURL contains a String of the url pointing to the void cheque in the firebase cloud storage
                                    Cook newCook = new Cook(firstName, lastName, emailAddress, password, description, address, imageURL);
                                    users.child(emailAddress).setValue(newCook);
                                    MainActivity.currentUser = newCook;
                                    Toast.makeText(getActivity(), "Registered as " + firstName + " " + lastName, Toast.LENGTH_LONG).show();
                                    //button navigation
                                    Intent intent = new Intent(getActivity(), UserWelcome.class);
                                    startActivity(intent);
                                }
                            }); //upload image and set voidChequeURL to the image URL in the firebase cloud storage
                        } else { //account already exists with this email
                            textCookErrorMessage.setText("An account already exists with this email");
                        }
                    }
                });
            } else { //at least one text field is empty
                textCookErrorMessage.setText("All fields must be filled");
            }
        }
    }

    protected static boolean checkName(String name){
        name = name.trim();
        if (!name.isEmpty()) { //check if String is empty
            if (name.matches("[a-zA-Z\\-]*")) { //check if all characters in String are alphabets
                return true;
            }
        }
        return false;
    }

    protected static boolean checkEmail(String emailAddress){
        if (!emailAddress.isEmpty()) { //check if String is empty
            if (emailAddress.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return true;
            }
        }
        return false;
    }

    protected static boolean checkPassword(String password){
        //Password checked to be at least 8 characters
        if (password.length() >= 8){
            return true;
        }
        return false;
    }

    protected static boolean checkAddressNumber(String number){
        if(number.matches("[0-9]*")){
            return true;
        }
        return false;
    }

    protected static boolean checkStreet(String street){
        if(street.matches("[a-zA-Z-. ]*")){
           return true;
        }
        return false;
    }

    protected boolean inputValidation(String firstName, String lastName, String email, String addrNum, String addrStreet, String password){
        //used to validate all of the different expected strings
        if(checkName(firstName) == false) {
            textCookErrorMessage.setText("First name contains invalid characters");
            return false;
        }
        else{
            if(checkName(lastName) == false){
                textCookErrorMessage.setText("Last name contains invalid characters");
                return false;
            }
            else{
                if (checkEmail(email) == false){
                    textCookErrorMessage.setText("Email Address contains invalid characters");
                    return false;
                }
                else{
                    if(checkAddressNumber(addrNum) == false){
                        textCookErrorMessage.setText("Address number must contain only numbers");
                        return false;
                    }
                    else{
                        if(checkStreet(addrStreet) == false){
                            textCookErrorMessage.setText("Street name contains invalid characters");
                            return false;
                        }
                        else{
                            if(checkPassword(password) == false){
                                textCookErrorMessage.setText("Password must be at least 8 characters");
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