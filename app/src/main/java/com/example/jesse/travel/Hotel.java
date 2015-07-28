package com.example.jesse.travel;

/**
 * stores hotel info
 */
public class Hotel {

    private String name;
    private String description;
    private String address;
    private float rating;
    private double lowPrice;

    public Hotel(String nm, String des, String addr, float rate, double low){
        this.name = nm;
        this.description = des;
        this.address = addr;
        this.rating = rate;
        this.lowPrice = low;

    }

    public Hotel(String name, Float rating){
        this.name = name;
        this.rating = rating;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public String getAddress(){
        return this.address;
    }

    public double getLowPrice(){
        return this.lowPrice;
    }

    public float getRating(){
        return this.rating;
    }
}
