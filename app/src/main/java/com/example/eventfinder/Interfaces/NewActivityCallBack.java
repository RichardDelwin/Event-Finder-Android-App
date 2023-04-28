package com.example.eventfinder.Interfaces;

import com.example.eventfinder.DataClasses.SearchResponse;

public interface NewActivityCallBack<T> {
    void onButtonClick(T data, T data2, SearchResponse response);
}
