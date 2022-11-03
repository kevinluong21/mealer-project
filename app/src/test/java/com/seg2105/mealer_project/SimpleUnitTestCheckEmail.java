package com.seg2105.mealer_project;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleUnitTestCheckEmail {
    @Test
    public void testCheckEmail(){
        boolean expected = true;
        boolean actual = CookRegister.checkEmail("test@email.com");
        assertEquals("checkEmail is not working correctly",expected,actual);
    }
}
