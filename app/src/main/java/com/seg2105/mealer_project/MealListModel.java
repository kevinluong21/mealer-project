//this class is intended for displaying the meals on a cook's profile recyclerview
//code implemented from https://www.geeksforgeeks.org/cardview-using-recyclerview-in-android-with-example/

package com.seg2105.mealer_project;

public class MealListModel {
    private String mealName;
    private String price;
    private String rating;
    private int mealsSold; //number of this meal that were sold so far
    private int imageSrc;

    public MealListModel(String mealName, String price, String rating, int mealsSold) {
        this.mealName = mealName.substring(0, 1).toUpperCase() + mealName.substring(1).toLowerCase();
        this.price = price;
        this.rating = rating;
        this.mealsSold = mealsSold;
        this.imageSrc = R.drawable.maindish_icon;
    }

    public String getMealName() {
        return mealName;
    }

    public String getPrice() {
        return price;
    }

    public String getRating() {
        return rating;
    }

    public int getMealsSold() {
        return mealsSold;
    }

    public int getImageSrc() {
        return imageSrc;
    }
}
