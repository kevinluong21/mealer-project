package com.seg2105.mealer_project;

import java.io.Serializable;

public abstract class Person implements Serializable {
    protected String firstName;
    protected String lastName;
    protected String emailAddress; //acts as the primary key
    protected String accountPassword;
    //    protected String address;
    protected String profilePhoto; //source of the photo
    protected String accountStatus; //online, offline

    public String getFirstName() { //getter methods required for database serialization (if it is not public, the variable will NOT be stored in the database)
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public String getAccountPassword() {
        return this.accountPassword;
    }
}
