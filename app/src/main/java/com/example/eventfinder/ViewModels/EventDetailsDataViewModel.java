package com.example.eventfinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventfinder.DataClasses.ArtistResponse;
import com.example.eventfinder.SharedDataClasses.EventDetailSharedData;

import java.util.ArrayList;

public class EventDetailsDataViewModel extends ViewModel {

    private MutableLiveData<ArrayList<String>> artists = new MutableLiveData<>();
    private MutableLiveData<String> venueId = new MutableLiveData<>();

    private MutableLiveData<String> ticketMasterLink = new MutableLiveData<>();

    public void setTicketMaster(String data) {
        this.ticketMasterLink.setValue(data);
    }

    public MutableLiveData<String> getTicketMasterLive(){
        return this.ticketMasterLink;
    }

    public void setVenueId(String venueId) {
        this.venueId.setValue(venueId);
    }

    public String getVenueId(){
        return this.venueId.getValue();
    }

    public LiveData<String> getVenueIdLiveData(){
        return this.venueId;
    }

    public ArrayList<String> getArtists() {
        return artists.getValue();
    }


    public LiveData<ArrayList<String>> getArtistLiveData(){
        return artists;
    }
    public void setArtistsList(ArrayList<String> artistsList) {
        artists.setValue(artistsList);
    }

    public int getArtistsCount() {
        ArrayList<String> temp = artists.getValue();
        if (temp == null) {
            return 0;
        }
        return temp.size();
    }
}
