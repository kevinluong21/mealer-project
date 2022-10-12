package com.seg2105.mealer_project;

public class Client extends Person{

    //instance variables
    private String address;
    private String creditCardInfo;

    /*
     * @return String[]
     * returns the results for meal type search
     * */
    public static String[] searchMealType(String s){
        return null;
    }

    /*
     * @return String{[]
     * returns the results for meal name search
     * */
    public static String[] searchMealName(String s){
        return null;
    }

    /*
     * @return String[]
     * returns the results for cuisine type search
     * */
    public static String searchCuisineType(String s){
        return null;
    }

    /*
     * search meal
     * */
    public static void selectMeal(){
        //selects a meal
    }

    /*
     * display meal state
     * */
    public static void mealState(){
        //returns the state of the meal
    }

    /*
     * rate the meal
     * */
    public static void rateMeal(int rating){
        //rate with int rating
    }

    /*
     * complaint submission
     * */
    public static void submitComplaint(String complaint){
        //submits a complaint
    }

    /*
     * prints all past previousPurchases formated
     * */
    public static void viewPreviousPurchases(String[] previousPurchases){
        int counter = 1;
        for (int i = 0; i < previousPurchases.length-1; i++){
            system.out.println(counter + "." + previousPurchases[i]);
            counter++;
        }
    }

}