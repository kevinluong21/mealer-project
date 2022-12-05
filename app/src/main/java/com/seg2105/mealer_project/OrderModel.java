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
        MainActivity.checkUser(mealReq.getClientEmail(), new UserCallback<Administrator, Cook, Client>() {
            @Override
            public void onCallback(Administrator user1, Cook user2, Client user3) {
                if (user3 != null) {
                    client = user3;
                    meal = mealReq.getMeal();
                    mealName = meal.getName();
                    mealPrice = meal.displayPrice();
                    customerEmail = client.getEmailAddress();
                    customerName = (client.getFirstName()+" "+ client.getLastName());
                    imgSrc = R.drawable.maindish_icon;
                }
            }
        });
    }

    public String getMealName(){return this.mealName;}
    public String getMealPrice(){return this.mealPrice;}
    public String getCustomerName(){return this.customerName;}
    public String getCustomerEmail(){return this.customerEmail;}
    public int getImgSrc(){return this.imgSrc;}
}
