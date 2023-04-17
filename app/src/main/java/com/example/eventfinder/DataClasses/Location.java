package com.example.eventfinder.DataClasses;

import androidx.annotation.NonNull;

import org.json.JSONObject;

public class Location {
    private double latitude;
    private double longitude;

    public Location(String loc) {
        String[] locParts = loc.split(",");
        this.latitude = Double.parseDouble(locParts[0]);
        this.longitude = Double.parseDouble(locParts[1]);
    }

    public Location(JSONObject loc){
        try {
            this.latitude = loc.getDouble("lat");
            this.longitude = loc.getDouble("lng");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s, %s", this.latitude, this.longitude);
    }
}
