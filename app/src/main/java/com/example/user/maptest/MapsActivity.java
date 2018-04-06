package com.example.user.maptest;

//import android.net.Network;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response;
//import com.google.android.gms.common.api.Response;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double latitude= -34;
    double longitude = 151;
    public static String mapCoordinates;
    JSONObject json;
    private static final String apiKey  = "AIzaSyCSlU8jDPUiQV4coSgPPHeZ6x63Te_KB-4";
    EditText text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        text=(EditText) findViewById(R.id.UserInput);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void displayWeather(View v){
            String temp = text.getText().toString();
            //will need to edit url to reflect user input
            RequestQueue mRequestQueue;
            // Instantiate the cache
            Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
            // Set up the network to use HttpURLConnection as the HTTP client.
            Network network = new BasicNetwork(new HurlStack());
            // Instantiate the RequestQueue with the cache and network.
            mRequestQueue = new RequestQueue(cache, network);
            // Start the queue
            mRequestQueue.start();
            String url = formatURL(temp);

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (com.android.volley.Request.Method.GET, url, (JSONObject) null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            json = new JSONObject();
                            JSONObject json2 = new JSONObject();
                            JSONArray test = new JSONArray();
                            try{
                                 test = response.getJSONArray("results");
                                 json = test.getJSONObject(0);
                                 json2 = json.getJSONObject("geometry").getJSONObject("location");
                                mapCoordinates = test.toString();
                                //Log.i("response", ""+json2.getDouble("lat"));
                                latitude = json2.getDouble("lat");
                                longitude = json2.getDouble("lng");
                            }catch(JSONException e){            Log.i("IT GETS HERE", "This is a bit of a failure");}



                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
            // Add the request to the RequestQueue.
            mRequestQueue.add(jsObjRequest);


            //latitude= Integer.parseInt(temp);

            LatLng sydney = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }

    public String formatURL(String s){
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        url += s.replace(" ", "+");
        url+="&key="+apiKey;
        //return "https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key="+apiKey;//default return
        return url;
    }
}
