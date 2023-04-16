package com.example.eventfinder.DataClasses;

import java.net.URLEncoder;

public class SearchObject {

    protected String keyword;
    protected String category;
    protected int distance;
    protected double latitude;
    protected double longitude;

    public SearchObject(String keyword, String category, String distance, double latitude, double longitude){

        this.keyword = keyword;
        this.category = category;
        this.distance = Integer.parseInt(distance);
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public String getAsUrlParams(){

        String query = "";
        try {
            String keyword_enc = URLEncoder.encode(keyword, java.nio.charset.StandardCharsets.UTF_8.toString());
            String category_enc = URLEncoder.encode(category, java.nio.charset.StandardCharsets.UTF_8.toString());
            String distance_enc = URLEncoder.encode(String.valueOf(distance), java.nio.charset.StandardCharsets.UTF_8.toString());
            String latitude_enc = URLEncoder.encode(String.valueOf(latitude), java.nio.charset.StandardCharsets.UTF_8.toString());
            String longitude_enc = URLEncoder.encode(String.valueOf(longitude), java.nio.charset.StandardCharsets.UTF_8.toString());

            query = String.format("keyword=%s&category=%s&distance=%s&latitude=%s&longitude=%s",
                        keyword_enc.trim(), category_enc, distance_enc, latitude_enc, longitude_enc);
        }
        catch(Exception e){
        }


        return query;
    }

}

