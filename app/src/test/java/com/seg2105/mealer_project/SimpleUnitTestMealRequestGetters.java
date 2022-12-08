package com.seg2105.mealer_project;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class SimpleUnitTestMealRequestGetters {
    private MealRequest testMealReq;
    private Meal testMeal;
    private Cook testCook;
    private Client testClient;

    @Before
    public void creation(){
        Address address = new Address("1","Street");
        CreditCard cc = new CreditCard("09/23","1122334455667788","111");
        testCook = new Cook("John","Doe","JohnDoe@gmail.com","password123","Brief description",address,"void cheque");
        testMeal = new Meal(testCook,"Burger","Main Dish","American","cheese, beef, ketchup, lettuce","lactose",15.99,"A classic cheese burger with toppings",false);
        testClient = new Client("John","Doe","jd@gmail.com","password",address,cc);
        testMealReq = new MealRequest(testMeal,testClient,testCook);
    }
    @Test
    public void mealRequestGetters(){
        Meal expectedMeal = testMealReq.getMeal();
        Meal actualMeal = testMeal;
        assertEquals("Meal Getter not working properly",expectedMeal,actualMeal);

        String expectedEmail = testMealReq.getClientEmail();
        String actualEmail = ("jd@gmail.com");
        assertEquals("Email getter not working properly",expectedEmail,actualEmail);
    }
}
