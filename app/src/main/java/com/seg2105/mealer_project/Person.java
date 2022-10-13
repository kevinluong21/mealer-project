package com.seg2105.mealer_project;

public abstract class Person {
    protected String firstName;
    protected String lastName;
    protected String emailAddress;
    protected String accountPassword;
//    protected String address;
    protected String profilePhoto; //source of the photo
    protected String accountStatus; //online, offline

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
