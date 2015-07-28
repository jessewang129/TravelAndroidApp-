/**
 * This activity shows the top 5 hotels in the area
 * The information is collected from Expedia's API
 * and stored in a Hotel object
 */
package com.example.jesse.travel;

import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class HotelSearchActivity extends FragmentActivity{

    private List<Hotel> hotels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_search);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("Top 5 Hotels in "+MainActivity.getCity());
        fetchData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hotel_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchData() {
        Resources res = getResources();

        try {
            URL url = new URL(res.getString(R.string.base_url_hotel) + res.getString(R.string.hotelAPI_key) +
                    "&cid=55505&locale=en_US&currencyCode=CAD&destinationString="
                    + MainActivity.fillSpace(MainActivity.getCity()) + "&arrivalDate=" + getCurrentDate() + "&room1=1&_type=json");
            System.out.println(url);
            JSONObject json = MainActivity.retrieveJSONObject(url);
            setHotelInfo(json);

        }catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("url!");
        }catch (Exception e){
            System.out.println("fetch!");
        }

    }

    private void setHotelInfo(JSONObject json){
        int i;
        try {
            for(i=0;i<5;i++){
                JSONObject summary = json.getJSONObject("HotelListResponse").getJSONObject("HotelList").getJSONArray("HotelSummary").getJSONObject(i);
                String name = summary.getString("name");
                Float rating = Float.parseFloat(summary.getString("hotelRating"));
                String description = summary.getString("locationDescription");
                double lowPrice = summary.getDouble("lowRate");
                String address = summary.getString("address1")+", "+summary.getString("city");
                hotels.add(new Hotel(name, description, address, rating, lowPrice));
            }
            Collections.sort(hotels, new Comparator<Hotel>() {
                public int compare(Hotel h1, Hotel h2) {
                    return Math.round(h2.getRating()) - Math.round(h1.getRating());
                }
            });
            displayHotelInfo();
            setRating();

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("json!");

        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void displayHotelInfo(){
        List<TextView> tv = new ArrayList<>();
        TextView hotel1 = (TextView) findViewById(R.id.hotel1);
        TextView hotel2 = (TextView) findViewById(R.id.hotel2);
        TextView hotel3 = (TextView) findViewById(R.id.hotel3);
        TextView hotel4 = (TextView) findViewById(R.id.hotel4);
        TextView hotel5 = (TextView) findViewById(R.id.hotel5);

        tv.add(hotel1);
        tv.add(hotel2);
        tv.add(hotel3);
        tv.add(hotel4);
        tv.add(hotel5);

        int i = 0;
        for(TextView t: tv){

            t.setText("Name: " + hotels.get(i).getName() + "\nAddress: "+ hotels.get(i).getAddress()+
                    "\nDescription: "+ hotels.get(i).getDescription()+"\nStarting From: $"+
                    String.format("%.2f", hotels.get(i).getLowPrice())+
                    "\nHotel Rating:");
            i++;
        }


    }
    private void setRating(){
        List<RatingBar> rb = new ArrayList<>();
        RatingBar ratingBar1= (RatingBar) findViewById(R.id.ratingBar1);
        RatingBar ratingBar2= (RatingBar) findViewById(R.id.ratingBar2);
        RatingBar ratingBar3= (RatingBar) findViewById(R.id.ratingBar3);
        RatingBar ratingBar4= (RatingBar) findViewById(R.id.ratingBar4);
        RatingBar ratingBar5= (RatingBar) findViewById(R.id.ratingBar5);

        rb.add(ratingBar1);
        rb.add(ratingBar2);
        rb.add(ratingBar3);
        rb.add(ratingBar4);
        rb.add(ratingBar5);

        int i = 0;
        for(RatingBar r: rb){
            r.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            r.setRating(hotels.get(i).getRating());
            i++;
        }

    }

    private String getCurrentDate(){

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        return df.format(c.getTime());
    }

}
