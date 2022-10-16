package com.seg2105.mealer_project;

import java.io.Serializable;

public class Client extends Person implements Serializable {

    //instance variables
    private String address;
    private String creditCardInfo;

    public Client() { //empty constructor is needed for database!

    }

    public Client(String firstName, String lastName, String emailAddress, String accountPassword, String address, String creditCardInfo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.accountPassword = accountPassword;
        this.address = address;
        this.creditCardInfo = creditCardInfo;
    }

    public String getAddress() { //getter methods required for database serialization
        return address;
    }

    public String getCreditCardInfo() {
        return this.creditCardInfo;
    }

    /*
     * @return String[]
     * returns the results for meal type search
     * */
    public static String[] searchMealType(String s){
        return null;
    }

    /*
     * @return String{[]
     * returns the results for meal name search
     * */
    public static String[] searchMealName(String s){
        return null;
    }

    /*
     * @return String[]
     * returns the results for cuisine type search
     * */
    public static String searchCuisineType(String s){
        return null;
    }

    /*
     * search meal
     * */
    public static void selectMeal(){
        //selects a meal
    }

    /*
     * display meal state
     * */
    public static void mealState(){
        //returns the state of the meal
    }

    /*
     * rate the meal
     * */
    public static void rateMeal(int rating){
        //rate with int rating
    }

    /*
     * complaint submission
     * */
    public static void submitComplaint(String complaint){
        //submits a complaint
    }

    /*
     * prints all past previousPurchases formated
     * */
    public static void viewPreviousPurchases(String[] previousPurchases){
        int counter = 1;
        for (int i = 0; i < previousPurchases.length-1; i++){
            System.out.println(counter + "." + previousPurchases[i]);
            counter++;
        }
    }

}