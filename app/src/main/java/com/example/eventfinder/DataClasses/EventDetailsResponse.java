package com.example.eventfinder.DataClasses;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventDetailsResponse {
    private String id;
    private String name;
    private ArrayList<String> attractions;
    private ArrayList<String> attractionsMusic;
    private ArrayList<String> classifications;
    private int priceRanges_min;
    private int priceRanges_max;
    private String ticketStatus;
    private String ticketStatusName;
    private String ticketStatusColor;
    private String buyAt;
    private String seatMap;
    private String localDate;
    private String localTime;
    private String venue;
    private String venueId;

    public EventDetailsResponse(JSONObject jsonObject) {
        id = jsonObject.optString("id");
        name = jsonObject.optString("name");

        JSONArray attractionsArray = jsonObject.optJSONArray("attractions");
        if (attractionsArray != null) {
            attractions = new ArrayList<>();
            for (int i = 0; i < attractionsArray.length(); i++) {
                attractions.add(attractionsArray.optString(i));
            }
        }

        JSONArray attractionsMusicArray = jsonObject.optJSONArray("attractionsMusic");
        if (attractionsMusicArray != null) {
            attractionsMusic = new ArrayList<>();
            for (int i = 0; i < attractionsMusicArray.length(); i++) {
                attractionsMusic.add(attractionsMusicArray.optString(i));
            }
        }

        JSONArray classificationsArray = jsonObject.optJSONArray("classifications");
        if (classificationsArray != null) {
            classifications = new ArrayList<>();
            for (int i = 0; i < classificationsArray.length(); i++) {
                classifications.add(classificationsArray.optString(i));
            }
        }

        priceRanges_min = jsonObject.optInt("priceRanges_min");
        priceRanges_max = jsonObject.optInt("priceRanges_max");
        ticketStatus = jsonObject.optString("ticketStatus");
        ticketStatusName = jsonObject.optString("ticketStatusName");
        ticketStatusColor = jsonObject.optString("ticketStatusColor");
        buyAt = jsonObject.optString("buyAt");
        seatMap = jsonObject.optString("seatMap");
        localDate = jsonObject.optString("localDate");
        localTime = jsonObject.optString("localTime");
        venue = jsonObject.optString("venue");
        venueId = jsonObject.optString("venueId");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getAttractions() {
        return attractions;
    }

    public ArrayList<String> getAttractionsMusic() {
        return attractionsMusic;
    }

    public ArrayList<String> getClassifications() {
        return classifications;
    }

    public double getPriceRanges_min() {
        return priceRanges_min;
    }

    public double getPriceRanges_max() {
        return priceRanges_max;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public String getTicketStatusName() {
        return ticketStatusName;
    }

    public String getTicketStatusColor() {
        return ticketStatusColor;
    }

    public String getBuyAt() {
        return buyAt;
    }

    public String getSeatMap() {
        return seatMap;
    }

    public String getLocalDate() {
        return localDate;
    }

    public String getLocalTime() {
        return localTime;
    }

    public String getVenue() {
        return venue;
    }

    public String getVenueId() {
        return venueId;
    }

}
