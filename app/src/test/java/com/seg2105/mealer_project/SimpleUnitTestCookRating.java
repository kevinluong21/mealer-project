package com.seg2105.mealer_project;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleUnitTestCookRating {
    private Cook testCook;

    @Before
    public void createMeal() {
        Address address = new Address("1", "Street");
        testCook = new Cook("John", "Doe", "JohnDoe@gmail.com", "password123", "Brief description", address, "void cheque");
    }
    @Test
    public void rateCookTest(){
        testCook.incrementNumberOfRatings();
        testCook.incrementTotalRating(5.0);
        testCook.calculateRating();
        double actualRating = testCook.getCustomerRating();
        double expectedRating = 5.0;
        assertEquals("Rating a cook is not working properly",expectedRating,actualRating,0);
    }
}
