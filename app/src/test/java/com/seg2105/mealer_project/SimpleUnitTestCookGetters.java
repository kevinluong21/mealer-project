package com.seg2105.mealer_project;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class SimpleUnitTestCookGetters {
    private Cook testCook;
    @Before
    public void createCook(){
        Address address = new Address("1","Street");
        testCook = new Cook("John","Doe","JohnDoe@gmail.com","password123","Brief description",address,"void cheque");
    }
    @Test
    public void testCookGetters(){
        Boolean expectedStatus = true;
        Boolean actualStatus = testCook.getAccountStatus();
        assertEquals("getAccountStatus not working",expectedStatus,actualStatus);

        String expectedRole = ("Cook");
        String actualRole = testCook.getRole();
        assertEquals("getRole not working",expectedRole,actualRole);

        int expectedSoldMeals = 0;
        int actualSoldMeals = testCook.getSoldMeals();
        assertEquals("getSoldMeals not working properly",expectedSoldMeals,actualSoldMeals);

        Boolean expectedSuspension = false;
        Boolean actualSuspension = testCook.getPermSuspension();
        assertEquals("getPermSuspension not working properly",expectedSuspension,actualSuspension);

    }
}
