package com.seg2105.mealer_project;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class SimpleUnitTestMealGetters {
    private Meal testMeal;
    private Cook testCook;
    @Before
    public void createTestMeal(){
        Address address = new Address("1","Street");
        testCook = new Cook("John","Doe","JohnDoe@gmail.com","password123","Brief description",address,"void cheque");
        testMeal = new Meal(testCook,"Burger","Main Dish","American","cheese, beef, ketchup, etc","lactose",15.99,"A classic cheese burger with toppings",false);
    }
    @Test
    public void testMealGetters(){
        String expectedName = "Burger";
        String actualName = testMeal.getName();
        assertEquals("Meal name getter not working",expectedName,actualName);

        double expectedPrice = 15.99;
        double actualPrice = testMeal.getPrice();
        assertEquals("Meal price getter not working",expectedPrice,actualPrice,0);

        String expectedMealType = "Main Dish";
        String actualMealType = testMeal.getMealType();
        assertEquals("Meal type getter not working",expectedMealType,actualMealType);
    }

}
