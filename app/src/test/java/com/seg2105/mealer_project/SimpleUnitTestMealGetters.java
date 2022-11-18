package com.seg2105.mealer_project;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class SimpleUnitTestMealGetters {
    private Meal testMeal;
    @Before
    public void createTestMeal(){
        testMeal = new Meal("Burger","Main Dish","American","cheese, beef, ketchup, etc","lactose",15.99,"A classic cheese burger with toppings",false);
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
