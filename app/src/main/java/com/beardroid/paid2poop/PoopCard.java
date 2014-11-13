package com.beardroid.paid2poop;

/**
 * Created by Max on 11/13/2014.
 */
public class PoopCard {
    private double amount;
    private String date;
    private String time;
    private String rating;

    public PoopCard() {

    }

    public PoopCard(double amount, String date, String time, String rating) {

    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
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

}
