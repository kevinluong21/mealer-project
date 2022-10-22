package com.seg2105.mealer_project;

import java.io.Serializable;

public class CreditCard implements Serializable {

    protected String expiryDate;
    protected String cardNumber;
    protected String CVV;

    public CreditCard() {

    }
    public CreditCard(String expiryDate, String cardNumber, String CVV){

        this.expiryDate = expiryDate;
        this.cardNumber = cardNumber;
        this.CVV = CVV;
    }

    public String getExpiryDate(){return this.expiryDate;}

    public String getCardNumber(){return this.cardNumber;}

    public String getCVV(){return this.CVV;}
}
