package com.seg2105.mealer_project;

public class Address {

    protected String streetNum;
    protected String streetName;

    public Address(String streetNum, String streetName){
        this.streetNum = streetNum;
        this.streetName = streetName;
    }

    protected String getStreetName(){return this.streetName;}

    protected String getStreetNum(){return this.streetNum;}
}
