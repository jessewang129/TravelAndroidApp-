package com.example.jesse.travel;

/**
 * stores city info
 */
public class City {
    private double temperature;
    private String name;
    private double latitude;
    private double longitude;
    private String weather;
    private int weatherId;
    private double humidity;

    public City(String nm, double temp, double lat, double lng, String w, int wid, double humid){
        this.name = nm;
        this.temperature = temp;
        this.latitude = lat;
        this.longitude = lng;
        this.weather = w;
        this.weatherId = wid;
        this.humidity = humid;
    }

    public String getName(){
        return this.name;
    }

    public double getTemperature(){
        return this.temperature;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public String getWeather(){
        return this.weather;
    }

    public int getWeatherId(){
        return this.weatherId;
    }

    public double getHumidity(){
        return this.humidity;
    }
}
