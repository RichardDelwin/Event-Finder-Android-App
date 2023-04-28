package com.example.eventfinder.DataClasses;

public class VenueResponse {

    private String name;
    private String city;
    private String stateCode;
    private String state;
    private String address;
    private String postalCode;
    private String displayAddress;
    private String phone;
    private String openHours;
    private String generalRule;
    private String childRule;
    private VenueLocation venueLocation;

    // Getters and setters for all fields

    public static class VenueLocation {
        private double latitude;
        private double longitude;

        // Getters and setters for latitude and longitude
        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getStateCode() {
        return stateCode;
    }

    public String getState() {
        return state;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getDisplayAddress() {
        return displayAddress;
    }

    public String getPhone() {
        return phone;
    }

    public String getOpenHours() {
        return openHours;
    }

    public String getGeneralRule() {
        return generalRule;
    }

    public String getChildRule() {
        return childRule;
    }

    public VenueLocation getVenueLocation() {
        return venueLocation;
    }

}
