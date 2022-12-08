package com.seg2105.mealer_project;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class SimpleUnitTestOfferingMeal {
    private Meal testMeal;
    private Cook testCook;
    @Before
    public void createMeal(){
        Address address = new Address("1","Street");
        testCook = new Cook("John","Doe","JohnDoe@gmail.com","password123","Brief description",address,"void cheque");
        testMeal = new Meal(testCook,"Burger","Main Dish","American","cheese, beef, ketchup, etc","lactose",15.99,"A classic cheese burger with toppings",false);
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
