package com.seg2105.mealer_project;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

public class Meal implements Serializable {
    private String cookFirstName;
    private String cookLastName;
    private String cookEmail; //email of cook that offers this meal
    private Address cookAddress; //address of cook that offers this meal
    private String name;
    private String mealType;
    private String cuisineType;
//    private String listOfIngredients; //needs to be changed if we choose linked list
//    private String listOfAllergens;
//    private LinkedList<String> ingredients;
//    private LinkedList<String> allergens;
    private HashMap<String, String> ingredients;
    private HashMap<String, String> allergens;
    private double price;
    private String description;
    private boolean offering; //the meal is being offered
    private double rating;
    private int numSold; //how many the cook has sold of this meal
    //private boolean isInOfferedList;

    //empty constructor is necessary for firebase
    public Meal() {

    }

    public Meal(Cook cook, String name, String mealType, String cuisineType, String listOfIngredients, String listOfAllergens, double price, String description, boolean offering){
        this.cookFirstName = cook.getFirstName();
        this.cookLastName = cook.getLastName();
        this.cookEmail = cook.getEmailAddress();
        this.cookAddress = cook.getAddress();
        this.name = name;
        this.mealType = mealType;
        this.cuisineType = cuisineType;
        this.ingredients = new HashMap<String, String>();
        String[] temp = listOfIngredients.split(","); //split string by comma and store in temp string array

        for (int i = 0; i < temp.length; i++) { //push each ingredient onto ingredients linked list
            String trimmed = temp[i].trim();
            this.ingredients.put(Integer.toString(i) + "_key", trimmed);
        }

        this.allergens = new HashMap<String, String>();
        temp = listOfAllergens.split(","); //split string by comma and store in temp string array

        for (int i = 0; i < temp.length; i++) {
            String trimmed = temp[i].trim();
            this.allergens.put(Integer.toString(i) + "_key", trimmed);
        }

        this.price = price;
        this.description = description;
        this.offering = offering;
        this.rating = 0; //0 by default
        this.numSold = 0; //0 by default
    }

    public String getCookEmail() {
        return cookEmail;
    }

    public String getCookFirstName() {
        return cookFirstName;
    }

    public String getCookLastName() {
        return cookLastName;
    }
    public Address getCookAddress() {
        return this.cookAddress;
    }

    public String getName() {
        return name;
    }

    public String getMealType() {
        return mealType;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public boolean isOffering(){
        return offering;
    }

    public void setOffering(boolean newOffer){offering = newOffer;}

    public HashMap<String, String> getIngredients() {
        return ingredients;
    }

    public HashMap<String, String> getAllergens() {
        return allergens;
    }

    public double getPrice() {
        return price;
    }

    public String displayPrice() { //formats price for string display
        String price = Double.toString(this.price);
        String[] priceSplit = price.split("\\.");

        if (priceSplit[1].length() >= 2) {
            price = priceSplit[0] + "." + priceSplit[1].substring(0, 2); //cut off the rest of the price at 2 decimal places
        }
        else if (priceSplit[1].length() == 1) {
            price = priceSplit[0] + "." + priceSplit[1] + "0";
        }
        return price;
    }

    public String getDescription() {
        return description;
    }

    public double getRating() {
        return this.rating;
    }

    public String displayRating() { //formats rating for string display
        String rating = Double.toString(this.rating);
        String[] ratingSplit = rating.split("\\.");

        if (ratingSplit[1].length() >= 2) {
            rating = ratingSplit[0] + "." + ratingSplit[1].substring(0, 1); //cut off the rest of the rating at 1 decimal place
        }
        else if (ratingSplit[1].length() == 0) {
            rating = ratingSplit[0] + "." + ratingSplit[1] + "0";
        }
        return rating;
    }

    public int getNumSold() {
        return this.numSold;
    }

    public void incrementNumSold() {
        numSold++;
    }
}