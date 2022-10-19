package com.seg2105.mealer_project;

public class CreditCard {

    protected int expiryDate;
    protected int cardNumber;
    protected int CVV;

    public CreditCard(int expiryDate, int cardNumber, int CVV){

        this.expiryDate = expiryDate;
        this.cardNumber = cardNumber;
        this.CVV = CVV;
    }

    protected int getExpiryDate(){return this.expiryDate;}

    protected int getCardNumber(){return this.cardNumber;}

    protected int getCVV(){return this.CVV;}
}
