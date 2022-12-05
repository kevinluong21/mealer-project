package com.seg2105.mealer_project;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;
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
    private int keyCounter = 0; //purchaseRequest key


    private int soldMeals;
    private int customerRating;

//    private LinkedList <Meal> meals;
//    private LinkedList <Meal> offeredMeals;
    private HashMap<String, Meal> meals;
    private HashMap<String, Meal> offeredMeals;
    private HashMap<String, MealRequest> purchaseRequest;

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

//        offeredMeals = new LinkedList<Meal>();
//        meals = new LinkedList<Meal>();

        this.meals = new HashMap<String, Meal>();
        this.offeredMeals = new HashMap<String, Meal>();
        this.purchaseRequest = new HashMap<String, MealRequest>();

    }

    //gets account status
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

    public HashMap<String, Meal> getMeals() {
        return meals;
    }

    public HashMap<String, Meal> getOfferedMeals() {
        return offeredMeals;
    }

    public HashMap<String, MealRequest> getPurchaseRequest() {
        return purchaseRequest;
    }

    public static void rejectRequest(){

    }

    public static void acceptRequest(){

    }

    public static void deleteMeal(){

    }

    public static void offerMeal(){

    }
    public void receiveRequest(MealRequest mealReq){
        purchaseRequest.put(Integer.toString(keyCounter), mealReq);
        keyCounter++;
    }
}