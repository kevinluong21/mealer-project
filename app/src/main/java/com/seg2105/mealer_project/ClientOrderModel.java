//this class is intended for displaying the ingredients and allergens of a meal
//code implemented from https://www.geeksforgeeks.org/cardview-using-recyclerview-in-android-with-example/

package com.seg2105.mealer_project;

public class ClientOrderModel {
    private String mealName;
    private Address address;
    private String progress;
    private int imageSrc;

    public ClientOrderModel(MealRequest mealRequest) {
        this.mealName = mealRequest.getMeal().getName();
        this.address = mealRequest.getMeal().getCookAddress();
        this.progress = mealRequest.getProgress();
        this.imageSrc = R.drawable.maindish_icon;
    }

    public String getMealName() {
        return mealName;
    }

    public Address getAddress() {
        return address;
    }

    public String getProgress() {
        return progress;
    }

    public int getImageSrc() {
        return imageSrc;
    }
}
