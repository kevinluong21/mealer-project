package com.seg2105.mealer_project;

import java.io.Serializable;

public class Address implements Serializable {

    protected String streetNum;
    protected String streetName;

    public Address(String streetNum, String streetName){
        this.streetNum = streetNum;
        this.streetName = streetName;
    }

    public String getStreetName(){return this.streetName;}

    public String getStreetNum(){return this.streetNum;}
}
