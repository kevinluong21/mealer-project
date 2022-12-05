package com.seg2105.mealer_project;

import java.io.Serializable;

public class MealRequest implements Serializable {

    private Meal meal;
    private String cookEmail;
    private String clientEmail;
    private boolean requestFinished;
    private boolean accepted;

    public MealRequest(Meal meal, String clientEmail, String cookEmail){
        this.meal = meal;
        this.clientEmail = clientEmail;
        this.cookEmail = cookEmail;
        this.requestFinished = false;
        this.accepted = false;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public String getCookEmail() {
        return cookEmail;
    }

    public Meal getMeal(){return this.meal;}

    public void acceptRequest() {
        this.accepted = true;
        this.requestFinished = true;
    }
    public void declineRequest(){
        this.requestFinished = true;
    }

}