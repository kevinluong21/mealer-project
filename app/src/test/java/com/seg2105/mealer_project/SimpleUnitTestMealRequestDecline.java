package com.seg2105.mealer_project;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleUnitTestMealRequestDecline {
    private MealRequest testMealReq;
    private Meal testMeal;
    private Cook testCook;
    private Client testClient;

    @Before
    public void creation() {
        Address address = new Address("1", "Street");
        CreditCard cc = new CreditCard("09/23", "1122334455667788", "111");
        testCook = new Cook("John", "Doe", "JohnDoe@gmail.com", "password123", "Brief description", address, "void cheque");
        testMeal = new Meal(testCook, "Burger", "Main Dish", "American", "cheese, beef, ketchup, lettuce", "lactose", 15.99, "A classic cheese burger with toppings", false);
        testClient = new Client("John", "Doe", "jd@gmail.com", "password", address, cc);
        testMealReq = new MealRequest(testMeal, testClient, testCook);
    }
    @Test
    public void declineRequest(){
        testMealReq.declineRequest();
        boolean actualActive = testMealReq.isActive();
        boolean expectedActive = false;
        assertEquals("accept meal request not working properly",expectedActive,actualActive);

        boolean actualComplete = testMealReq.isCompleted();
        boolean expectComplete = true;
        assertEquals("complete meal request not working",expectComplete,actualComplete);

    }
}
