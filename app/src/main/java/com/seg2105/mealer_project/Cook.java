package com.seg2105.mealer_project;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class Cook extends Person implements Serializable {

    private String description;
    private Address address;
    private String voidCheque;

    private int soldMeals;
    private int customerRating;

    private LinkedList <Meal> meals;
    private LinkedList <Meal> offeredMeals;
    private Queue <MealRequest> purchaseRequest;

    public Cook() { //empty constructor is needed for database!

    }

    public Cook (String firstName, String lastName, String emailAddress, String accountPassword, String description, Address address, String voidCheque){
        this.role = "Cook";
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.accountPassword = accountPassword;
        this.address = address;
        this.description = description;
        this.voidCheque = voidCheque;

        this.soldMeals = 0;
        this.customerRating = 0;
    }

    public Address getAddress() { //getter methods required for database serialization
        return this.address;
    }

    public String getDescription() {
        return this.description;
    }

    public String getVoidCheque() {
        return this.voidCheque;
    }

    public static void addMeal(){

    }

    public static void rejectRequest(){

    }

    public static void acceptRequest(){

    }

    public static void deleteMeal(){

    }

    public static void offerMeal(){

    }

    public static void removeMealOffer(){

    }



}