package com.seg2105.mealer_project;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class SimpleUnitTestOfferingMeal {
    private Meal testMeal;
    @Before
    public void createMeal(){
        testMeal = new Meal("Burger","Main Dish","American","cheese, beef, ketchup, etc","lactose",15.99,"A classic cheese burger with toppings",false);
    }

    @Test
    public void testOffering(){
        boolean expectedInitialOffering = false;
        boolean actualInitialOffering = testMeal.isOffering();
        assertEquals("isOffering function not working properly",expectedInitialOffering,actualInitialOffering);

        testMeal.setOffering(true);

        boolean expectedNewOffering = true;
        boolean actualNewOffering = testMeal.isOffering();
        assertEquals("setOffering is not working properly",expectedNewOffering,actualNewOffering);
    }

}
