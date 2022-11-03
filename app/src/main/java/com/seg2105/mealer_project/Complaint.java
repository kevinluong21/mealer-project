package com.seg2105.mealer_project;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class Complaint implements Serializable {

    //private Client issuingClient;
    //private Cook respondentCook;

    private String clientEmail;
    private String cookEmail;
    private String description;
    private String id;
    private static int complaintCounter;

    private Meal meal; //the meal it relates to

    private boolean isAddressed; //new,completed;

    public Complaint() { //empty constructor is needed for database!

    }

    public Complaint(String description, String clientEmail, String cookEmail, String id){
        this.description=description;
        this.cookEmail=cookEmail;
        this.clientEmail = clientEmail;
        this.isAddressed = false;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        complaintCounter++;
        this.id = id;

        //this.ID = ("Complaint number  " + complaintCounter + " filed on " + timestamp);

    }

    public String getId(){
        return this.id;
    }

    public String getCookEmail(){
        return this.cookEmail;
    }

    public String getClientEmail(){
        return this.clientEmail;
    }

    public String getDescription(){
        return this.description;
    }

    public void setComplaintStatus(){
        this.isAddressed = true;
    }

    public boolean getComplaintStatus() {return this.isAddressed;}

}
