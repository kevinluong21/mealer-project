package com.seg2105.mealer_project;

import java.io.Serializable;

public class Address implements Serializable {

    protected String streetNum;
    protected String streetName;

    public Address() {

    }
    public Address(String streetNum, String streetName){
        this.streetNum = streetNum;
        this.streetName = streetName;
    }

    public String getStreetName(){return this.streetName;}

    public String getStreetNum(){return this.streetNum;}

    public String formatAddress() { //format the address as a String
        String addressToReturn;
        String streetNum = this.streetNum.trim();
        String streetName = "";
        String[] streetNameRaw = this.streetName.toLowerCase().split(" ");

        for (int i = 0; i < streetNameRaw.length; i++) {
            streetName = streetName + " " + streetNameRaw[i].substring(0, 1).toUpperCase() + streetNameRaw[i].substring(1);
        }
        addressToReturn = streetNum + " " + streetName;
        return addressToReturn;
    }
}
