package com.seg2105.mealer_project;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Cook extends Person implements Serializable {

    private String description;
    private Address address;
    private String voidCheque; //refers to the image in the firebase cloud storage
    private boolean accountActive;
    private String suspensionEndDate;
    private boolean permSuspension;
    private int soldMeals;
    //changed to a double
    private double customerRating;
    private int numberOfRatings;
    private double totalRating;

    private HashMap<String, Meal> meals;
    private HashMap<String, Meal> offeredMeals;
    private HashMap<String, MealRequest> purchaseRequests;

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
        this.accountActive = true;//active status
        this.permSuspension = false;//default active

        this.soldMeals = 0;
        this.customerRating = 0;
        this.numberOfRatings = 0;
        this.totalRating = 0;

        this.meals = new HashMap<String, Meal>();
        this.offeredMeals = new HashMap<String, Meal>();
        this.purchaseRequests = new HashMap<String, MealRequest>();

    }

    public boolean getAccountStatus(){
        return this.accountActive;
    }

    //sets value for account status
    public void setAccountStatus(boolean status){
        this.accountActive = status;
    }

    public String getSuspensionEndDate(){
        return this.suspensionEndDate;
    }

    public void setSuspensionEndDate(String endDate){
        this.suspensionEndDate = endDate;
    }

    public boolean getPermSuspension(){
        return this.permSuspension;
    }

    public void setPermSuspension(boolean permSuspension){
        this.permSuspension = permSuspension;
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

    public int getSoldMeals(){return this.soldMeals;}

    public double getCustomerRating() {return customerRating;}

    public int getNumberOfRatings(){return numberOfRatings;}

    public HashMap<String, Meal> getMeals() {
        return meals;
    }

    public HashMap<String, Meal> getOfferedMeals() {
        return offeredMeals;
    }

    public HashMap<String, MealRequest> getPurchaseRequests() {
        return purchaseRequests;
    }

    public void incrementSoldMeals() {
        soldMeals++;
    }

    public void incrementNumberOfRatings(){numberOfRatings++;}

    public double getTotalRating(){return totalRating;}

    public void incrementTotalRating(double n){this.totalRating = totalRating+n;}//add total rating

    public void calculateRating(){customerRating = round(totalRating/(numberOfRatings), 1);}

    private double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}