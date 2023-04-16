package com.example.eventfinder.Helpers;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eventfinder.DataClasses.SearchObject;
import com.example.eventfinder.DataClasses.SearchResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

public class ServerAccessHelper {
    final private String serverUrl = "https://myloth-hw8-backend-icno4892.wl.r.appspot.com/";//"http://localhost:3500/";//

    SearchResponse[] searchResponses;
    private RequestQueue requestQueue;

    public ServerAccessHelper(RequestQueue queue){
        this.requestQueue = queue;
    }

    public SearchResponse[] search(SearchObject searchObject, Activity activity){

        String destUrl = serverUrl + "search?" + searchObject.getAsUrlParams();



        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, destUrl, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        try {
                            searchResponses = gson.fromJson(response.toString(), SearchResponse[].class);
                            Log.d("REQUEST", response.toString());
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            searchResponses = new SearchResponse[0];
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
        return searchResponses;
    }

}
