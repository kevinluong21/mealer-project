package com.seg2105.mealer_project;

public class Complaint {

    private Client issuingClient;
    private Cook respondentCook;
    private String description;

    private Meal meal; //the meal it relates to

    private boolean isAdressed; //new,completed;

    public Complaint(String description, Client client, Cook cook){
        this.description=description;
        this.respondentCook=cook;
        this.issuingClient = client;
        this.isAdressed = false;
    }

    public Cook getCook(){
        return this.respondentCook;
    }

    public Client getClient(){
        return this.issuingClient;
    }

    public String getDescription(){
        return this.description;
    }

    public void setComplaintStatus(){
        this.isAdressed = true;
    }

}
