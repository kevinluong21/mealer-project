//this class is intended for displaying the meals on a cook's profile recyclerview
//code implemented from https://www.geeksforgeeks.org/cardview-using-recyclerview-in-android-with-example/

package com.seg2105.mealer_project;

public class MealListModel {
    private String mealName;
    private double price;
    private double rating;
    private int mealsSold; //number of this meal that were sold so far
    private int imageSrc;

    public MealListModel(String mealName, double price, double rating, int mealsSold) {
        this.mealName = mealName.substring(0, 1).toUpperCase() + mealName.substring(1).toLowerCase();
        this.price = price;
        this.rating = rating;
        this.mealsSold = mealsSold;
        this.imageSrc = R.drawable.maindish_icon;
    }

    public String getMealName() {
        return mealName;
    }

    public double getPrice() {
        return price;
    }

    public double getRating() {
        return rating;
    }

    public int getMealsSold() {
        return mealsSold;
    }

    public int getImageSrc() {
        return imageSrc;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setMealsSold(int mealsSold) {
        this.mealsSold = mealsSold;
    }

    public void setImageSrc(int drawableID) {
        this.imageSrc = drawableID;
    }
}
