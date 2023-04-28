package com.example.eventfinder.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eventfinder.Adapters.AutoCompleteAdapter;
import com.example.eventfinder.DataClasses.FormData;
import com.example.eventfinder.DataClasses.Location;
import com.example.eventfinder.DataClasses.SearchObject;
import com.example.eventfinder.Helpers.ServerAccessHelper;
import com.example.eventfinder.Interfaces.VolleyCallBack;
import com.example.eventfinder.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SearchFragment extends Fragment {

    private AutoCompleteTextView autocomplete_keyword;
    private EditText distanceET;
    private EditText locationET;
    private Spinner categorySpinner;
    private Switch autoDetect;
    private AutoCompleteTextView autoCompleteTextView;
    private RequestQueue requestQueue;
    private ServerAccessHelper serverAccessHelper;
    AutoCompleteAdapter autoCompleteAdapter;
    Map<String, String> category_dictionary;

    private ProgressBar progressBar;

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

        progressBar = view.findViewById(R.id.search_progressBar);
        categorySpinner = view.findViewById(R.id.spinner_categories);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.categories_list, R.layout.spinner_style);
        adapter.setDropDownViewResource(R.layout.spinner_style);
        categorySpinner.setAdapter(adapter);

        autoCompleteAdapter = new AutoCompleteAdapter(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        autoCompleteTextView = view.findViewById(R.id.autocompleteTV);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                progressBar.setVisibility(View.VISIBLE);
                serverAccessHelper.getSuggestions(charSequence, new VolleyCallBack() {
                    @Override
                    public void onSuccess(Object o) {
                        ArrayList<String> suggestions = (ArrayList<String>) o;
                        autoCompleteAdapter.setArrayList(suggestions);
                        autoCompleteAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        autocomplete_keyword = view.findViewById(R.id.autocompleteTV);
        distanceET = view.findViewById(R.id.distance_edittext);
        locationET = view.findViewById(R.id.location_edittext);


        Button searchButton = view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = searchButtonClicked(view);
                if (bundle != null) {
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_searchFragment_to_searchResultsFragment, bundle);
                }
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
        autocomplete_keyword.setText("");
        distanceET.setText("10");
        locationET.setText("");
        autoDetect.setChecked(false);
        locationET.setVisibility(View.VISIBLE);
        categorySpinner.setSelection(0);
    }

    private boolean validateForm(){
        String keyword = autocomplete_keyword.getText().toString();
        String distance = distanceET.getText().toString();
        String location = locationET.getText().toString();
        boolean auto_detect_loc = autoDetect.isChecked();


        if(keyword.trim().length() == 0 || distance.trim().length() == 0 || (location.trim().length() == 0) && !auto_detect_loc) {
            Snackbar.make(getActivity().findViewById(R.id.viewPager_mainActivity), "Please fill all fields", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Bundle searchButtonClicked(View view) {

        if(validateForm()){

            Bundle bundle = new Bundle();

            String keyword = autocomplete_keyword.getText().toString();
            String category = categorySpinner.getSelectedItem().toString();
            category = category_dictionary.get(category);
            String distance = distanceET.getText().toString();
            String loc = locationET.getText().toString();
            boolean autodetect = autoDetect.isChecked();

            FormData formData  = new FormData(keyword, category, distance, loc, autodetect);

            bundle.putSerializable("formData", (Serializable) formData);
            return bundle;
//            resolveLocation_makeSearchApiCall();
        }
        return null;
    }

//    private void makeSearchApiCall(Location location){
//        Toast.makeText(getActivity(), "Search called", Toast.LENGTH_SHORT).show();
//
//        SearchObject searchObject = new SearchObject(autocomplete_keyword.getText().toString(), category,
//                distanceET.getText().toString(), location.getLatitude(),location.getLongitude());
//
//        serverAccessHelper.search(searchObject, getActivity());
//    }
//
//    private void resolveLocation_makeSearchApiCall() {
//
//        boolean auto_detect_loc = autoDetect.isChecked();
//        if (auto_detect_loc) {
//           serverAccessHelper.autoDetectLocation(new VolleyCallBack<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    makeSearchApiCall(location);
//                }
//            });
//
//        } else {
//            Log.d("PATH", "resolveLocation-else");
//            String locationText = locationET.getText().toString();
//            serverAccessHelper.geoCode(locationText, new VolleyCallBack<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    makeSearchApiCall(location);
//                }
//
//            });
//
//            Log.d("PATH", "resolveLocation-else[EXIT]");
//        }
//    }
}