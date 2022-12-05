package com.seg2105.mealer_project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Client extends Person implements Serializable {

    //instance variables
    private Address address;
    private CreditCard creditCardInfo;
    private HashMap<String, Meal> likedMeals;
    private ArrayList<MealRequest> requestedMeals;

    public Client() { //empty constructor is needed for database!

    }

    public Client(String firstName, String lastName, String emailAddress, String accountPassword, Address address, CreditCard creditCardInfo) {
        this.role = "Client";
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.accountPassword = accountPassword;
        this.address = address;
        this.creditCardInfo = creditCardInfo;
        this.likedMeals = new HashMap<String, Meal>();
        this.requestedMeals = new ArrayList<MealRequest>();
    }

    public Address getAddress() { //getter methods required for database serialization
        return address;
    }

    public CreditCard getCreditCardInfo() {
        return this.creditCardInfo;
    }

    public HashMap<String, Meal> getLikedMeals() {
        return this.likedMeals;
    }

    /*
     * @return String[]
     * returns the results for meal type search
     * */
    public String[] searchMealType(String s){
        return null;
    }

    /*
     * @return String{[]
     * returns the results for meal name search
     * */
    public String[] searchMealName(String s){
        return null;
    }

    /*
     * @return String[]
     * returns the results for cuisine type search
     * */
    public String searchCuisineType(String s){
        return null;
    }

    /*
     * search meal
     * */
    public void requestMeal(MealRequest mealReq){
        requestedMeals.add(mealReq);
        //selects a meal
    }

    /*
     * display meal state
     * */
    public void mealState(){
        //returns the state of the meal
    }

    /*
     * rate the meal
     * */
    public void rateMeal(int rating){
        //rate with int rating
    }

    /*
     * complaint submission
     * */
    public void submitComplaint(String complaint){
        //submits a complaint
    }

    /*
     * prints all past previousPurchases formatted
     * */
    public void viewPreviousPurchases(String[] previousPurchases) {
        int counter = 1;
        for (int i = 0; i < previousPurchases.length - 1; i++) {
            System.out.println(counter + "." + previousPurchases[i]);
            counter++;
        }
    }

}