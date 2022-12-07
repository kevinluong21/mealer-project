package com.seg2105.mealer_project;

import java.io.Serializable;

public class MealRequest implements Serializable {

    private Meal meal;
    private String cookEmail;
    private String clientEmail;
    private String clientName;
    private boolean active;
    private boolean accepted;
    private boolean completed;
    private String progress;

    String[] progressStates = {"Awaiting Response...", "Your Order Is Being Prepared...", "Your Order Is Ready For Pickup", "Order Rejected"};

    public MealRequest() { //empty constructor for firebase

    }

    public MealRequest(Meal meal, Client client, Cook cook){
        this.meal = meal;
        this.clientEmail = client.getEmailAddress();
        this.clientName = client.getFirstName() + " " + client.getLastName();
        this.cookEmail = cook.getEmailAddress();
        this.active = true;
        this.accepted = false;
        this.completed = false;
        this.progress = progressStates[0];
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public String getClientName() {
        return clientName;
    }

    public String getCookEmail() {
        return cookEmail;
    }

    public Meal getMeal(){return this.meal;}

    public boolean isActive(){return this.active;}

    public boolean isCompleted() {
        return this.completed;
    }

    public String getProgress() {
        return progress;
    }

    public void acceptRequest() {
        this.meal.incrementNumSold();
        this.accepted = true;
        this.active = false;
        progress = progressStates[1];
    }

    public void completeRequest() {
        this.completed = true;
        progress = progressStates[2];
    }

    public void declineRequest(){
        this.active = false;
        progress = progressStates[3];
    }

}