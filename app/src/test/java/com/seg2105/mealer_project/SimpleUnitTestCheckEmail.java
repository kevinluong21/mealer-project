package com.seg2105.mealer_project;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleUnitTestCheckEmail {
    @Test
    public void testCheckEmail(){
        boolean expectedCorrectFormat = true;
        boolean actualCorrectFormat = CookRegister.checkEmail("test@email.com");
        assertEquals("checkEmail is not working correctly",expectedCorrectFormat,actualCorrectFormat);

        boolean expectedWrongFormat = false;
        boolean actualWrongFormat = CookRegister.checkEmail("testEmail.com");
        assertEquals("checkEmail is not working properly",expectedWrongFormat,actualWrongFormat);
    }
}
