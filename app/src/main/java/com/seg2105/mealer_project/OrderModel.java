package com.seg2105.mealer_project;

import android.util.Log;

public class OrderModel {
    private String customerName;
    private String customerEmail;
    private String mealName;
    private String mealPrice;
    private int imgSrc;
    private Client client;
    private Meal meal;

    public OrderModel(MealRequest mealReq){
        meal = mealReq.getMeal();
        mealName = meal.getName();
        mealPrice = meal.displayPrice();
        customerEmail = MainActivity.keyToEmailAddress(mealReq.getClientEmail());
        customerName = mealReq.getClientName();
        imgSrc = R.drawable.maindish_icon;
    }

    public String getMealName(){return this.mealName;}
    public String getMealPrice(){return this.mealPrice;}
    public String getCustomerName(){return this.customerName;}
    public String getCustomerEmail(){return this.customerEmail;}
    public int getImgSrc(){return this.imgSrc;}
}
