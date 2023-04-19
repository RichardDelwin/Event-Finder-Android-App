package com.example.eventfinder.Interfaces;

import com.example.eventfinder.DataClasses.SearchResponse;

import java.util.ArrayList;

public interface VolleyCallBackArray<T> {

    void onSuccess(T[] t);

    void onSuccess(SearchResponse[] searchResponses);
}
