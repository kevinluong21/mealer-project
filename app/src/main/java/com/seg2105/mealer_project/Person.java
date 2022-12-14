package com.seg2105.mealer_project;

import java.io.Serializable;

public abstract class Person implements Serializable {
    protected String role;
    protected String firstName;
    protected String lastName;
    protected String emailAddress; //acts as the primary key
    protected String accountPassword;
    protected String profilePhoto; //source of the photo (not implemented in this deliverable)
    protected String accountStatus; //online, offline (not implemented in this deliverable)

    public String getRole() {
        return this.role;
    }

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
