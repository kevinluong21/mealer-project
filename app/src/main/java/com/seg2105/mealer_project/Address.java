package com.seg2105.mealer_project;

public class Address {

    protected int streetNum;
    protected String streetName;

    public Address(int streetNum, String streetName){
        this.streetNum = streetNum;
        this.streetName = streetName;
    }

    protected String getStreetName(){return this.streetName;}

    protected int getStreetNum(){return this.streetNum;}
}
