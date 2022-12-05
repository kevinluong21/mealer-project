package com.seg2105.mealer_project;

public class MealRequest {

    public Meal meal;
    public Cook cook;
    public Client client;
    public boolean requestFinished;
    public boolean accepted;

    public MealRequest(Meal meal, Client client, Cook cook){
        this.meal = meal;
        this.client = client;
        this.cook = cook;
        this.requestFinished = false;
        this.accepted = false;
    }

    public Client getClient(){return this.client;}

    public Cook getCook(){return this.cook;}

    public Meal getMeal(){return this.meal;}

    public void acceptRequest(){
        this.accepted = true;
        this.requestFinished = true;
    }
    public void declineRequest(){
        this.requestFinished = true;
    }

}