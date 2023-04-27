package com.example.eventfinder.DataClasses;

import java.util.ArrayList;

public class ArtistResponse {

    private String url;
    private String followers;
    private String id;
    private String image;
    private String name;
    private int popularity;
    private ArrayList<String> albums;

    public ArtistResponse(String url, String followers, String id, String image, String name, int popularity, ArrayList<String> albums) {
        this.url = url;
        this.followers = followers;
        this.id = id;
        this.image = image;
        this.name = name;
        this.popularity = popularity;
        this.albums = albums;
    }

    public String getUrl() {
        return url;
    }

    public String getFollowers() {
        return followers;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getPopularity() {
        return String.valueOf(popularity);
    }

    public ArrayList<String> getAlbums() {
        return albums;
    }

}
