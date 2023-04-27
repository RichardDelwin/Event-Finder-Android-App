package com.example.eventfinder.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventfinder.DataClasses.ArtistResponse;
import com.example.eventfinder.SharedDataClasses.EventDetailSharedData;

import java.util.ArrayList;

public class EventDetailsDataViewModel extends ViewModel {

    private MutableLiveData<ArrayList<ArtistResponse>> artists = new MutableLiveData<>();

    public ArrayList<ArtistResponse> getArtists() {
        return artists.getValue();
    }


    public void addArtist(ArtistResponse object) {
        ArrayList<ArtistResponse> temp = artists.getValue();

        if (temp == null) {
            temp = new ArrayList<>();
        }
        temp.add(object);

        setArtistsList(temp);
    }

    public LiveData<ArrayList<ArtistResponse>> getArtistLiveData(){
        return artists;
    }
    public void setArtistsList(ArrayList<ArtistResponse> artistsList) {
        artists.setValue(artistsList);
    }

    public int getArtistsCount() {
        ArrayList<ArtistResponse> temp = artists.getValue();
        if (temp == null) {
            return 0;
        }
        return temp.size();
    }
}
