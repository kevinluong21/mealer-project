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

    public String getFirstName() { //getter methods required for database
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


//    public boolean checkName(String name) {
//        name = name.trim();
//        if (!name.isEmpty()) { //check if String is empty
//            if (name.matches("[a-zA-Z]+")) { //check if all characters in String are alphabets
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean checkEmailAddress(String emailAddress) { //rework
//        if (!emailAddress.isEmpty()) { //check if String is empty
//            if (emailAddress.endsWith(".com") && emailAddress.contains("@")) { //check if domain contains @ and .com
//                return true;
//            }
//        }
//        return false;
//    }
}
