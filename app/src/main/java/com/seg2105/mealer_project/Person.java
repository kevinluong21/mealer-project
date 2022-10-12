package com.seg2105.mealer_project;

public abstract class Person {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String accountPassword;
//    private String address;
    private String profilePhoto; //source of the photo
    private String accountStatus; //online, offline

    public boolean checkName(String name) {
        name = name.trim();
        if (!name.isEmpty()) { //check if String is empty
            if (name.matches("[a-zA-Z]+")) { //check if all characters in String are alphabets
                return true;
            }
        }
        return false;
    }

    public boolean checkEmailAddress(String emailAddress) { //rework
        if (!emailAddress.isEmpty()) { //check if String is empty

            String[] splitEmailAddress = emailAddress.split("@"); //split email address to check domain
            String email = splitEmailAddress[0];
            String domain = splitEmailAddress[1];

            if (domain.contains(".com")) { //check if domain contains @ and .com
                return true;
            }
        }
        return false;
    }
}
