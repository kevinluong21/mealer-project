package com.seg2105.mealer_project;

public class Administrator extends Person{
    Object [] inbox; //Complaints
    Object [] dismissedComplaints;
    Object [] acceptedComplaints;

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String accountPassword;



    public Administrator (String firstName, String lastName, String emailAddress, String accountPassword){

        this.firstName = firstName;
        this. lastName = lastName;
        this.emailAddress = emailAddress;
        this.accountPassword = accountPassword;
    }






}
