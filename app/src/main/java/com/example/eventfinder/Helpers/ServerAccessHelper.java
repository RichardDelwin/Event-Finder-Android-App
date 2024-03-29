package com.example.eventfinder.Helpers;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.eventfinder.DataClasses.EventDetailsResponse;
import com.example.eventfinder.DataClasses.Location;
import com.example.eventfinder.DataClasses.SearchObject;
import com.example.eventfinder.DataClasses.SearchResponse;
import com.example.eventfinder.DataClasses.ArtistResponse;
import com.example.eventfinder.DataClasses.VenueResponse;
import com.example.eventfinder.Interfaces.VolleyCallBack;
import com.example.eventfinder.Interfaces.VolleyCallBackArray;
import com.example.eventfinder.Interfaces.VolleyCallBackArtist;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ServerAccessHelper {
    final private String serverUrl = "https://myloth-hw8-backend-icno4892.wl.r.appspot.com/";
    final private String ipInfoUrl = "https://ipinfo.io/?token=7754ebeef5da73";
    final private String gMaps = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyBMp7FiafypEXvkS4Hrhya4-hhDh3nnlr4&address=%s";
    SearchResponse[] searchResponses;

    private RequestQueue requestQueue;
    public int numOfRequestsMade;
    private Gson gson;
    public ServerAccessHelper(RequestQueue queue){
        this.requestQueue = queue;
        this.gson = new Gson();
        this.numOfRequestsMade = 0;
    }

    public SearchResponse[] search(SearchObject searchObject, VolleyCallBackArray volleyCallBack){

        String destUrl = serverUrl + "search?" + searchObject.getAsUrlParams();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, destUrl, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            searchResponses = gson.fromJson(response.toString(), SearchResponse[].class);
                            volleyCallBack.onSuccess(searchResponses);
                            Log.d("REQUEST", response.toString());
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            searchResponses = new SearchResponse[0];
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
        return searchResponses;
    }

    public void autoDetectLocation(VolleyCallBack callBack){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ipInfoUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("REQUEST", response.toString());
                            String loc = response.get("loc").toString();
                            Location location = new Location(loc);
                            Log.d("RESPONSE LOCATION", location.toString());
                            callBack.onSuccess(location);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            searchResponses = new SearchResponse[0];
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    public void geoCode(String address, VolleyCallBack callBack){

        String address_enc = "";

        try {
            address_enc = URLEncoder.encode(address, java.nio.charset.StandardCharsets.UTF_8.toString());
        }
        catch(Exception e){
            e.printStackTrace();
        }

        String destUrl = String.format(gMaps, address_enc);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, destUrl, null,
                response -> {

                    try {
                          Log.d("REQUEST LOCATION", response.toString());
                          if(response.get("status").toString().matches("OK")){

                              JSONObject loc = response.getJSONArray("results")
                                      .getJSONObject(0)
                                      .getJSONObject("geometry")
                                      .getJSONObject("location");


                              Location location = new Location(loc);
                              Log.d("RESPONSE LOCATION", location.toString());
                              callBack.onSuccess(location);

                          }else{
                              Log.d("REQUEST ERROR", response.toString());
                          }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        searchResponses = new SearchResponse[0];
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    public void getSuggestions(CharSequence text, VolleyCallBack callBack) {

        String destUrl = serverUrl + "autocomplete?keyword=%s";
        try {
            destUrl = String.format(destUrl, URLEncoder.encode(text.toString(), StandardCharsets.UTF_8.toString()));
        }catch (Exception e){
            e.printStackTrace();
        }

        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, destUrl, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("REQUEST[SUGGESTIONS]", response.toString());
                            ArrayList<String> suggestions = gson.fromJson(response.toString(), new TypeToken<ArrayList<String>>(){}.getType());
                            Log.d("RESPONSE[SUGGESTIONS]", suggestions.toString());

                            callBack.onSuccess(suggestions);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                }
        );

        requestQueue.add(jsonArrayRequest);


    }

    public void getEventsDetails(String id, VolleyCallBack volleyCallBack) {

        String destUrl= "";
        try {
            destUrl = this.serverUrl + "eventSearch?id=" + URLEncoder.encode(id, java.nio.charset.StandardCharsets.UTF_8.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, destUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    EventDetailsResponse eventDetailsResponse = new EventDetailsResponse(response);
                    volleyCallBack.onSuccess(eventDetailsResponse);
                    Log.d("REQUEST [EVENT DETAILS]", response.toString());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
//        numOfRequestsMade++;
        requestQueue.add(jsonObjectRequest);
    }

    public void getArtistDetails(String name, int index, VolleyCallBackArtist volleyCallBack){

        String destUrl= "";
        try {
            destUrl = this.serverUrl + "spotify?artist=" + URLEncoder.encode(name, java.nio.charset.StandardCharsets.UTF_8.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        String finalDestUrl = destUrl;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, destUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArtistResponse artistResponse = gson.fromJson(response.toString(), ArtistResponse.class);
                    volleyCallBack.onSuccess(artistResponse, index);
                    Log.d("REQUEST [ARTIST DETAILS]", response.toString());
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.d("REQUEST FAIL [ARTIST DETAILS]", finalDestUrl);
                }
            }
        },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();

                }
            });

        numOfRequestsMade++;
        requestQueue.add(jsonObjectRequest);
    }

    public void getVenueDetails(String id, VolleyCallBack volleyCallBack) {

        String destUrl= "";
        try {
            destUrl = this.serverUrl + "venueSearch?id=" + URLEncoder.encode(id, java.nio.charset.StandardCharsets.UTF_8.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, destUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    VenueResponse venueResponse = gson.fromJson(response.toString(), VenueResponse.class);
                    volleyCallBack.onSuccess(venueResponse);
                    Log.d("REQUEST [VENUE]", response.toString());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });
//        numOfRequestsMade++;
        requestQueue.add(jsonObjectRequest);
    }
}
