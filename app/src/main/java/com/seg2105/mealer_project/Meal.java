package com.seg2105.mealer_project;

import java.io.Serializable;
import java.util.LinkedList;

public class Meal implements Serializable {

    private String name;
    private String mealType;
    private String cuisineType;
    private String listOfIngredients; //needs to be changed if we choose linked list
    private String listOfAllergens;
    private LinkedList<String> ingredients;
    private LinkedList<String> allergens;
    private double price;
    private String description;
    private boolean isInMenu;
    //private boolean isInOfferedList;

    //empty constructor is necessary for firebase
    public Meal(){

    }

    public Meal(String name, String mealType, String cuisineType, String listOfInredients, String listOfAllergens, double price, String description, boolean offeredInMenu){

        this.name = name;
        this.mealType = mealType;
        this.cuisineType = cuisineType;
        this.listOfAllergens = listOfAllergens;
        this.listOfIngredients = listOfInredients;
        /*ingredients = new LinkedList<String>();
        for(int i = 0; i<listOfInredients.length; i++){
            ingredients.add(listOfInredients[i]);
        }
        allergens = new LinkedList<String>();
        for(int j = 0; j<listOfAllergens.length; j++){
            allergens.add(listOfInredients[j]);
        }*/


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

    public String getListOfIngredients() {
        return listOfIngredients;
    }

    public String listOfAllergens() {
        return listOfAllergens;
    }
    public boolean isPresentInMenu(){
        return isInMenu;
    }


    public LinkedList<String> getIngredients() {
        return ingredients;
    }

    public LinkedList<String> getAllergens() {
        return allergens;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}