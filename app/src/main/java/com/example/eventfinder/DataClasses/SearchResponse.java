package com.example.eventfinder.DataClasses;

import java.util.Objects;

public class SearchResponse {

    private String name;
    private String id;
    private String icon;
    private String localDate;
    private String localTime;
    private String sorterKey;
    private String classifications;
    private String venue;

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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SearchResponse)) {
            return false;
        }
        SearchResponse other = (SearchResponse) obj;
        return Objects.equals(this.getId(), other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}

