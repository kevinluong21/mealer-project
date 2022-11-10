package com.seg2105.mealer_project;

import java.util.LinkedList;

public class Meal {

    private String name;
    private String mealType;
    private String cuisineType;
    private LinkedList<String> ingredients;
    private LinkedList<String> allergens;
    private double price;
    private String description;

    public String getName() {
        return name;
    }

    public String getMealType() {
        return mealType;
    }

    public String getCuisineType() {
        return cuisineType;
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