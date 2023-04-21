package com.example.eventfinder.Helpers;

//https://stackoverflow.com/questions/3624280/how-to-use-sharedpreferences-in-android-to-store-fetch-and-edit-values

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.eventfinder.DataClasses.SearchResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreferencesAccessHelper {

    private SharedPreferences sharedPreferences;
    private Gson gson;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private String FAV_TABLE_NAME = "FAV_TABLE";
    private static final String SHARED_PREFERENCES_NAME = "Event_Finder_SP";

    public SharedPreferencesAccessHelper(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        gson = new Gson();
    }


    public void heartThis(SearchResponse searchResponse){
        String id = searchResponse.getId();
        Log.d("HEART", "ID = "+id);
        if(!idExists(id)){
            putBoolean(id, true);
            putItemIntoTable(searchResponse);
        }
    }

    public void unHeartThis(SearchResponse searchResponse){
        String id = searchResponse.getId();
        Log.d("UNHEART", "ID = "+id);

        if(idExists(id)){
            removeBoolean(id);
            removeItemFromTable(searchResponse);
        }
    }

    private void putItemIntoTable(SearchResponse favSearchResponse) {

        ArrayList<SearchResponse> searchResponseArrayList = getItemsFromTable();
        searchResponseArrayList.add(favSearchResponse);
        sharedPreferencesEditor.putString(FAV_TABLE_NAME, gson.toJson(searchResponseArrayList));
        sharedPreferencesEditor.apply();
    }

    public ArrayList<SearchResponse> getItemsFromTable() {
        Type type = new TypeToken<ArrayList<SearchResponse>>() {}.getType();
        ArrayList<SearchResponse> searchResponse  = gson.fromJson(sharedPreferences.getString(FAV_TABLE_NAME, "[]"), type);
        return searchResponse;
    }

    private void removeItemFromTable(SearchResponse searchResponse){
        ArrayList<SearchResponse> searchResponseArrayList = getItemsFromTable();
        searchResponseArrayList.remove(searchResponse);
        sharedPreferencesEditor.putString(FAV_TABLE_NAME, gson.toJson(searchResponseArrayList));
        sharedPreferencesEditor.apply();
    }

    private void putBoolean(String key, boolean value) {
        sharedPreferencesEditor.putBoolean(key, value);
        sharedPreferencesEditor.apply();
    }

    private void removeBoolean(String key){
        sharedPreferencesEditor.remove(key);
        sharedPreferencesEditor.apply();
    }

    public boolean idExists(String id){
        boolean exists = sharedPreferences.contains(id);
//        Log.d("SH PREF IDEXISTS", String.valueOf(exists));
        return exists;
    }

}
