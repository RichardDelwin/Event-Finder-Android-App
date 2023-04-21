package com.example.eventfinder.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventfinder.DataClasses.SearchResponse;

import java.util.ArrayList;

public class FavItemsViewModel extends ViewModel {

    public MutableLiveData<Boolean> isFavListEmpty = new MutableLiveData<Boolean>();

    public MutableLiveData<Boolean> getFavListEmptyStatus(){
        return isFavListEmpty;
    }
}
