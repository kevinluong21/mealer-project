package com.seg2105.mealer_project;

import java.io.Serializable;

public class MealRequest implements Serializable {

    private Meal meal;
    private String cookEmail;
    private String clientEmail;
    private boolean active;
    private boolean accepted;

    public MealRequest() { //empty constructor for firebase

    }

    public MealRequest(Meal meal, String clientEmail, String cookEmail){
        this.meal = meal;
        this.clientEmail = clientEmail;
        this.cookEmail = cookEmail;
        this.active = true;
        this.accepted = false;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public String getCookEmail() {
        return cookEmail;
    }

    public Meal getMeal(){return this.meal;}

    public boolean isActive(){return this.active;}

    public void acceptRequest() {
        this.accepted = true;
        this.active = false;
    }
    public void declineRequest(){
        this.active = false;
    }

}