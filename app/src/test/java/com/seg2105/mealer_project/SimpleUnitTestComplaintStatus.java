package com.seg2105.mealer_project;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class SimpleUnitTestComplaintStatus {

    private Complaint testComplaint;

    @Before
    public void createComplaint(){
        testComplaint = new Complaint("test description","client@email.com","cook@gmail.com","testID");
    }
    @Test
    public void complaintTesting(){
        boolean actualStatus = testComplaint.getComplaintStatus();
        boolean expectedStatus = false;
        assertEquals("Complaint Status is not working",expectedStatus,actualStatus);

        testComplaint.setComplaintStatus();

        boolean actualSetComplaint = testComplaint.getComplaintStatus();
        boolean expectedSetComplaint = true;
        assertEquals("setComplaintStatus is not working",expectedSetComplaint,actualSetComplaint);
    }
}
