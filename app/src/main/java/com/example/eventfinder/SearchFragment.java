package com.example.eventfinder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.eventfinder.DataClasses.Location;
import com.example.eventfinder.DataClasses.SearchObject;
import com.example.eventfinder.Helpers.ServerAccessHelper;
import com.example.eventfinder.Interfaces.VolleyCallBack;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;


public class SearchFragment extends Fragment {

    private EditText keywordET;
    private EditText distanceET;
    private EditText locationET;
    private Spinner categorySpinner;
    private Switch autoDetect;
    private RequestQueue requestQueue;
    private ServerAccessHelper serverAccessHelper;
    Map<String, String> category_dictionary;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requestQueue = Volley.newRequestQueue(getActivity());
        serverAccessHelper = new ServerAccessHelper(requestQueue);
        category_dictionary = new HashMap<>();

        // Add the key-value pairs to the dictionary
        category_dictionary.put("All", "Default");
        category_dictionary.put("Music", "KZFzniwnSyZfZ7v7nJ");
        category_dictionary.put("Sports", "KZFzniwnSyZfZ7v7nE");
        category_dictionary.put("Arts & Theatre", "KZFzniwnSyZfZ7v7na");
        category_dictionary.put("Film", "KZFzniwnSyZfZ7v7nn");
        category_dictionary.put("Miscellaneous", "KZFzniwnSyZfZ7v7n1");

        return inflater.inflate(R.layout.fragment_search, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categorySpinner = view.findViewById(R.id.spinner_categories);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.categories_list, R.layout.spinner_style);
        adapter.setDropDownViewResource(R.layout.spinner_style);
        categorySpinner.setAdapter(adapter);

        keywordET = view.findViewById(R.id.keyword_edittext);
        distanceET = view.findViewById(R.id.distance_edittext);
        locationET = view.findViewById(R.id.location_edittext);


        Button searchButton = view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                searchButtonClicked(view);
            }
        });

        Button clearButton = view.findViewById(R.id.clear_button);
        clearButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                clearButtonClicked(view);
            }
        });

        autoDetect = view.findViewById(R.id.switch_autodetect);
        autoDetect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    locationET.setVisibility(View.GONE);
                }else{
                    locationET.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void clearButtonClicked(View view) {
        keywordET.setText("");
        distanceET.setText("10");
        locationET.setText("");
        autoDetect.setChecked(false);
        locationET.setVisibility(View.VISIBLE);
        categorySpinner.setSelection(0);
    }

    private boolean validateForm(){
        String keyword = keywordET.getText().toString();
        String distance = distanceET.getText().toString();
        String location = locationET.getText().toString();
        boolean auto_detect_loc = autoDetect.isChecked();


        if(keyword.trim().length() == 0 || distance.trim().length() == 0 || (location.trim().length() == 0) && !auto_detect_loc) {
            Snackbar.make(getActivity().findViewById(R.id.viewPager_mainActivity), "Please fill all fields", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void searchButtonClicked(View view) {

        if(validateForm()){

            resolveLocation_makeSearchApiCall();

        }
    }

    private void makeSearchApiCall(Location location){
        Toast.makeText(getActivity(), "Search called", Toast.LENGTH_SHORT).show();
        String category = categorySpinner.getSelectedItem().toString();

        category = category_dictionary.get(category);

        SearchObject searchObject = new SearchObject(keywordET.getText().toString(), category,
                distanceET.getText().toString(), location.getLatitude(),location.getLongitude());

        serverAccessHelper.search(searchObject, getActivity());
    }

    private void resolveLocation_makeSearchApiCall() {

        boolean auto_detect_loc = autoDetect.isChecked();
        if (auto_detect_loc) {
           serverAccessHelper.autoDetectLocation(new VolleyCallBack() {
                @Override
                public void onSuccess(Location location) {
                    makeSearchApiCall(location);
                }
            });

        } else {
            Log.d("PATH", "resolveLocation-else");
            String locationText = locationET.getText().toString();
            serverAccessHelper.geoCode(locationText, new VolleyCallBack() {
                @Override
                public void onSuccess(Location location) {
                    makeSearchApiCall(location);
                }
            });

            Log.d("PATH", "resolveLocation-else[EXIT]");
        }
    }
}