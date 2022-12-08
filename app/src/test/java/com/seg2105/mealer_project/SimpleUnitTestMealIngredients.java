package com.seg2105.mealer_project;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.HashMap;

public class SimpleUnitTestMealIngredients {
    private Meal testMeal;
    private Cook testCook;

    @Before
    public void createMeal(){
        Address address = new Address("1","Street");
        testCook = new Cook("John","Doe","JohnDoe@gmail.com","password123","Brief description",address,"void cheque");
        testMeal = new Meal(testCook,"Burger","Main Dish","American","cheese, beef, ketchup, lettuce","lactose",15.99,"A classic cheese burger with toppings",false);
    }
    @Test
    public void testIngredients(){
        HashMap<String, String> ingredients = testMeal.getIngredients();

        String expectedIngredient = "cheese";
        String actualIngredient = ingredients.get("0_key");
        assertEquals("HashMap key incorrect",expectedIngredient,actualIngredient);

        expectedIngredient = "beef";
        actualIngredient = ingredients.get("1_key");
        assertEquals("HashMap key incorrect",expectedIngredient,actualIngredient);

        expectedIngredient = "ketchup";
        actualIngredient = ingredients.get("2_key");
        assertEquals("HashMap key incorrect",expectedIngredient,actualIngredient);

        expectedIngredient = "lettuce";
        actualIngredient = ingredients.get("3_key");
        assertEquals("HashMap key incorrect",expectedIngredient,actualIngredient);
    }

}
