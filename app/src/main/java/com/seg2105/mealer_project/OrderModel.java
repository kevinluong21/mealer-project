package com.seg2105.mealer_project;

public class OrderModel {
    private String customerName;
    private String customerEmail;
    private String mealName;
    private String mealPrice;
    private int imgSrc;
    private Client client;
    private Meal meal;

    public OrderModel(MealRequest mealReq){
        this.client = mealReq.getClient();
        this.meal = mealReq.getMeal();

        this.mealName = meal.getName();
        this.mealPrice = meal.displayPrice();
        this.customerEmail = client.getEmailAddress();
        this.customerName = (client.getFirstName()+" "+client.getLastName());
        this.imgSrc = R.drawable.maindish_icon;
    }

    public String getMealName(){return this.mealName;}
    public String getMealPrice(){return this.mealPrice;}
    public String getCustomerName(){return this.customerName;}
    public String getCustomerEmail(){return this.customerEmail;}
    public int getImgSrc(){return this.imgSrc;}
}
