package com.seg2105.mealer_project;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

public class Meal implements Serializable {

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
    private boolean isInMenu;
    //private boolean isInOfferedList;

    //empty constructor is necessary for firebase
    public Meal() {

    }

    public Meal(String name, String mealType, String cuisineType, String listOfIngredients, String listOfAllergens, double price, String description, boolean offeredInMenu){

        this.name = name;
        this.mealType = mealType;
        this.cuisineType = cuisineType;
//        this.listOfAllergens = listOfAllergens;
//        this.listOfIngredients = listOfIngredients;

//        this.ingredients = new LinkedList<String>();
        this.ingredients = new HashMap<String, String>();
        String[] temp = listOfIngredients.split(","); //split string by comma and store in temp string array

        for (int i = 0; i < temp.length; i++) { //push each ingredient onto ingredients linked list
//            this.ingredients.push(temp[i]);
            this.ingredients.put(Integer.toString(i), temp[i]);
        }

//        this.allergens = new LinkedList<String>();
        this.allergens = new HashMap<String, String>();
        temp = listOfAllergens.split(","); //split string by comma and store in temp string array

        for (int i = 0; i < temp.length; i++) {
//            this.allergens.push(temp[i]); //push each allergen onto allergens linked list
            this.allergens.put(Integer.toString(i), temp[i]);
        }

        this.price = price;
        this.description = description;
        this.isInMenu = offeredInMenu;
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

//    public String getListOfIngredients() {
//        return listOfIngredients;
//    }
//
//    public String listOfAllergens() {
//        return listOfAllergens;
//    }
    public boolean isPresentInMenu(){
        return isInMenu;
    }


//    public LinkedList<String> getIngredients() {
//        return ingredients;
//    }
//
//    public LinkedList<String> getAllergens() {
//        return allergens;
//    }


    public HashMap<String, String> getIngredients() {
        return ingredients;
    }

    public HashMap<String, String> getAllergens() {
        return allergens;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}