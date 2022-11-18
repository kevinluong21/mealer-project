//this class is intended for displaying the ingredients and allergens of a meal
//code implemented from https://www.geeksforgeeks.org/cardview-using-recyclerview-in-android-with-example/

package com.seg2105.mealer_project;

public class IngredientModel {
    private String ingredient;

    public IngredientModel(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
