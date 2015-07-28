/**
 * Main interface of this app
 * the main function is to take a city name as an input from the user
 * and shows the weather information via Openweather.com API
 * At the bottom there are two buttons and lead to other interfaces
 * to show the city on Google Map and show hotel information
 */
package com.example.jesse.travel;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity{

    private static EditText city_input;

    private static final String weatherAPIKey = "d13e98aca58daefb91d2c4d2771961e8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button;
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {

            //when the search button is clicked
            public void onClick(View v) {
                city_input = (EditText) findViewById(R.id.city);

                try {
                    if (v.getId() == R.id.button) {

                        URL url = new URL(getWeatherURL());

                        JSONObject json = retrieveJSONObject(url);

                            City city = setCityInfo(json);
                            setTemperature(city);
                            setWeather(city);
                            setWeatherIcon(city);
                            setHumidity(city);

                            Button button_map = (Button) findViewById(R.id.button_map);
                            button_map.setOnClickListener(this);
                            button_map.setVisibility(View.VISIBLE);

                            Button button_hotel = (Button) findViewById(R.id.button_hotel);
                            button_hotel.setOnClickListener(this);
                            button_hotel.setVisibility(View.VISIBLE);

                    }
                } catch (MalformedURLException e) {
                    showMessage(v.getContext());
                }catch (NullPointerException e){
                    showMessage(v.getContext());
                }
                //direct to Google map
                if (v.getId() == R.id.button_map) {
                    Intent act = new Intent(v.getContext(), MapsActivity.class);
                    startActivity(act);
                }
                //direct to hotel page
                if (v.getId() == R.id.button_hotel) {
                    Intent act = new Intent(v.getContext(), HotelSearchActivity.class);
                    startActivity(act);

                }
            }
        });
    }
    //get weather info via JSON
    public static JSONObject retrieveJSONObject(URL url){

        try{

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty("x-api-key", weatherAPIKey);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String inputLine;
            while((inputLine=reader.readLine())!=null)
                json.append(inputLine).append("\n");
            reader.close();

            return new JSONObject(json.toString());
        } catch (JSONException e) {

            return null;
        } catch (IOException e) {

            return null;
        }
    }

    //store info in a City object
    public static City setCityInfo(JSONObject json){
        String name = city_input.getText().toString();
        try {
            double temperature = json.getJSONObject("main").getDouble("temp");
            double latitude = json.getJSONObject("coord").getDouble("lat");
            double longitude = json.getJSONObject("coord").getDouble("lon");
            String weather = json.getJSONArray("weather").getJSONObject(0).getString("description");
            int weatherId = json.getJSONArray("weather").getJSONObject(0).getInt("id");
            double humidity = json.getJSONObject("main").getDouble("humidity");

            return new City(name, temperature, latitude, longitude, weather, weatherId, humidity);

        } catch (JSONException e) {
            return null;
        }
    }

    private void setTemperature(City city){
        TextView temperature = (TextView) findViewById(R.id.temperature);
        temperature.setText("Temperature:\n" + city.getTemperature() + " degree C");
    }

    private void setHumidity(City city){
        TextView humidity = (TextView) findViewById(R.id.humidity);
        humidity.setText("Humidity: " + city.getHumidity() + " %");
    }
    private void setWeather(City city){
        TextView weather = (TextView)findViewById(R.id.weather);
        weather.setText(city.getWeather());
    }

    public static String getCity(){
        return city_input.getText().toString();
    }

    public static String getWeatherURL(){
        String filledStr = fillSpace(city_input.getText().toString());

        return "http://api.openweathermap.org/data/2.5/weather?q="+filledStr+"&units=metric";
    }

    //replace space in city names with %20
    // eg. New York becomes New%20York
    public static String fillSpace(String str){
        String s = str;
        for(int i=0; i<str.length();i++){
            if(s.charAt(i)==' '){
                s = s.substring(0,i)+"%20"+s.substring(i+1);
            }
        }
        return s;
    }
    
    //sets the weather icon according to weather id
    private void setWeatherIcon(City city){

        ImageView weatherIcon = (ImageView) findViewById(R.id.weatherIcon);

        String uri;
        if(city.getWeatherId()==800){
            uri = "@drawable/sunny";
        }
        else{
            switch(city.getWeatherId()/100){

                case 2:
                    uri = "@drawable/thunder";
                    break;
                case 3:
                case 5:
                    uri = "@drawable/rainy";
                    break;
                case 7:
                case 8:
                    uri = "@drawable/cloudy";
                    break;
                case 6:
                    uri = "@drawable/snowing";
                    break;
                default:
                    uri = "";
            }

        }
        int imageId = getResources().getIdentifier(uri, null, getPackageName());
        weatherIcon.setImageDrawable(getResources().getDrawable(imageId));
    }

    // Shows error message when a city is not found
    private void showMessage(Context context){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("City:"+ city_input.getText().toString()+" is Not Found");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}

