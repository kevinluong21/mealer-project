package com.seg2105.mealer_project;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class SimpleUnitTestCookSuspension {

    private Cook testCook;

    @Before
    public void createCook() {
        Address address = new Address("1", "Street");
        testCook = new Cook("John", "Doe", "JohnDoe@gmail.com", "password123", "Brief description", address, "void cheque");
    }
    @Test
    public void testSuspension(){
        testCook.setSuspensionEndDate("07/11");
        String expectedEndDate = ("07/11");
        String actualEndDate = testCook.getSuspensionEndDate();
        assertEquals("Set suspension end date not working properly",expectedEndDate,actualEndDate);

        boolean expectedInitialPermSuspension = false;
        boolean actualInitialPermSuspension = testCook.getPermSuspension();
        assertEquals("Cook account starting permanently suspended",expectedInitialPermSuspension,actualInitialPermSuspension);

        testCook.setPermSuspension(true);

        boolean expectedPermSuspension = true;
        boolean actualPermSuspension = testCook.getPermSuspension();
        assertEquals("Set permanent suspension not working properly",expectedPermSuspension,actualPermSuspension);
    }
}

