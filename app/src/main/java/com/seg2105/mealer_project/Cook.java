package com.seg2105.mealer_project;

import java.util.LinkedList;
import java.util.Queue;

public class Cook extends Person{

    private String description;
    private String address;
    private String voidCheque;

    private int soldMeals;
    private int customerRating;

    private LinkedList <Meal> meals;
    private LinkedList <Meal> offeredMeals;
    private Queue <MealRequest> purchaseRequest;


    public Cook (String firstName, String lastName, String emailAddress, String accountPassword, String description, String address, String voidCheque){

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



}