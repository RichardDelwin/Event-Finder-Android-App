package com.example.eventfinder.DataClasses;

import java.io.Serializable;

public class FormData implements Serializable {
    protected String keyword;
    protected String category;
    protected String distance;
    protected String location;
    protected boolean isAutoDetect;

    public FormData(String keyword, String category, String distance, String location, boolean isAutoDetect){

        this.keyword = keyword;
        this.category = category;
        this.distance = distance;
        this.location = location;
        this.isAutoDetect = isAutoDetect;

    }

    public String getKeyword() {
        return keyword;
    }

    public String getCategory() {
        return category;
    }

    public String getDistance() {
        return distance;
    }

    public String getLocation() {
        return location;
    }

    public boolean getIsAutoDetect() {
        return isAutoDetect;
    }

}
