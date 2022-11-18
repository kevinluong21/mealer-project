package com.seg2105.mealer_project;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleUnitTestMealInputVerification {
    @Test
    public void testMealInputs(){
        boolean expectedPrice = true;
        boolean actualPrice = MealActivity.checkPrice("12.97");
        assertEquals("check price is not verifying properly",expectedPrice,actualPrice);

        boolean expectedType = true;
        boolean actualType = MealActivity.checkMealType("Dessert");
        assertEquals("check meal type is not working properly",expectedType,actualType);
    }
}
