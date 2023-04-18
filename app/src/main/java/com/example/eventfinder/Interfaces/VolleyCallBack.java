package com.example.eventfinder.Interfaces;
//https://stackoverflow.com/questions/49342841/android-wait-for-volley-response-for-continue

import com.example.eventfinder.DataClasses.Location;

import java.util.ArrayList;

public interface VolleyCallBack<T> {
    void onSuccess(T t);
}
