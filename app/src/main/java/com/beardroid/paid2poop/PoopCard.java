package com.beardroid.paid2poop;

/**
 * Created by Max on 11/13/2014.
 */
public class PoopCard {
    protected String amount;
    protected String date;
    protected String time;
    protected String rating; //will eventually be gone
    private double amountDbl;
    private long id;


    public PoopCard() {

    }

    public PoopCard(double amount, String date, String time, String rating) {

    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getId() {
        String idStr = "" + id;
        return idStr;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmountDbl() {
        return amountDbl;
    }

    public void setAmountDbl(double amountDbl) {
        this.amountDbl = amountDbl;
    }

}
