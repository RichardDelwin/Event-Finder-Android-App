package com.example.eventfinder.DataClasses;

public class SearchResponse {

    private String name;
    private String id;
    private String icon;
    private String localDate;
    private String localTime;
    private String sorterKey;
    private String classifications;
    private String venue;

//    public SearchResponse(String name, String id, String icon, String localDate, String localTime, String sorterKey, String classifications, String venue) {
//        this.name = name;
//        this.id = id;
//        this.icon = icon;
//        this.localDate = localDate;
//        this.localTime = localTime;
//        this.sorterKey = sorterKey;
//        this.classifications = classifications;
//        this.venue = venue;
//    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public String getLocalDate() {
        return localDate;
    }

    public String getLocalTime() {
        return localTime;
    }

    public String getSorterKey() {
        return sorterKey;
    }

    public String getClassifications() {
        return classifications;
    }

    public String getVenue() {
        return venue;
    }
}

