package com.seg2105.mealer_project;

import java.io.Serializable;

public class Administrator extends Person implements Serializable {
    Object [] inbox; //Complaints
    Object [] dismissedComplaints;
    Object [] acceptedComplaints;

    public Administrator() { //empty constructor is needed for database!

    }
    public Administrator (String firstName, String lastName, String emailAddress, String accountPassword){
        this.firstName = firstName;
        this. lastName = lastName;
        this.emailAddress = emailAddress;
        this.accountPassword = accountPassword;
    }






}
