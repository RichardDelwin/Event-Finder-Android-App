package com.example.eventfinder.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.eventfinder.DataClasses.ArtistResponse;
import com.example.eventfinder.DataClasses.VenueResponse;
import com.example.eventfinder.Helpers.ServerAccessHelper;
import com.example.eventfinder.Interfaces.VolleyCallBack;
import com.example.eventfinder.R;
import com.example.eventfinder.ViewModels.EventDetailsDataViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VenueTabFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class VenueTabFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ServerAccessHelper serverAccessHelper;
    private EventDetailsDataViewModel eventDetailsDataViewModel;
    private RequestQueue requestQueue;

    private String venueId = null;

    private TextView venueNameTV;
    private TextView addressTV;
    private TextView cityStateTV;
    private TextView contactTV;
    private ScrollView scrollView;
    private ProgressBar progressBar;
    private TextView openHoursTV;
    private TextView generalRulesTV;
    private TextView childRulesTV;

    private boolean isMapUpdated = false;
    private boolean isMapReady = false;
    private boolean isApiResponseReady = false;
    private boolean isGeneralRuleStateCollapsed = true;
    private boolean isChildRuleStateCollapsed = true;

    private VenueResponse.VenueLocation latlng;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_venue_tab, container, false);

        requestQueue = Volley.newRequestQueue(getActivity());
        serverAccessHelper = new ServerAccessHelper(requestQueue);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        venueNameTV = view.findViewById(R.id.venueName_VenueTab_Res);
        venueNameTV.setSelected(true);
        addressTV = view.findViewById(R.id.address_VenueTab_Res);
        addressTV.setSelected(true);
        cityStateTV = view.findViewById(R.id.cityState_VenueTab_Res);
        cityStateTV.setSelected(true);
        contactTV = view.findViewById(R.id.contact_venueTab_Res);
        contactTV.setSelected(true);

        eventDetailsDataViewModel = new ViewModelProvider(getActivity()).get(EventDetailsDataViewModel.class);
        eventDetailsDataViewModel.getVenueIdLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String Id) {

                if(Id != null && Id!=venueId) {
                    venueId = Id;
                    getVenueDetails(Id);
                }
            }
        });

        venueId = eventDetailsDataViewModel.getVenueId();
        if(venueId!=null){
            getVenueDetails(venueId);
        }

        return view;
    }

    private void getVenueDetails(String id){
        serverAccessHelper.getVenueDetails(id, new VolleyCallBack<VenueResponse>() {
            @Override
            public void onSuccess(VenueResponse response) {

                venueNameTV.setText(response.getName());
                addressTV.setText(response.getAddress());
                cityStateTV.setText(response.getCity()+", "+response.getState());
                contactTV.setText(response.getPhone());
                latlng  = response.getVenueLocation();
                openHoursTV.setText(response.getOpenHours());
                generalRulesTV.setText(response.getGeneralRule());
                childRulesTV.setText(response.getChildRule());

                isApiResponseReady=true;
                updateMap();

                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollView = view.findViewById(R.id.venueTab_scrollView);
        scrollView.setVisibility(View.GONE);

        progressBar = view.findViewById(R.id.venueTab_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        openHoursTV = view.findViewById(R.id.openHoursTV);
        generalRulesTV = view.findViewById(R.id.generalRulesTV);
        childRulesTV = view.findViewById(R.id.childRulesTV);

        generalRulesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isGeneralRuleStateCollapsed) {
                    isGeneralRuleStateCollapsed = false;
                    generalRulesTV.setMaxLines(Integer.MAX_VALUE);
                }
                else{
                    isGeneralRuleStateCollapsed = true;
                    generalRulesTV.setMaxLines(3);
                }

            }
        });

        childRulesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isChildRuleStateCollapsed) {
                    isChildRuleStateCollapsed = false;
                    childRulesTV.setMaxLines(Integer.MAX_VALUE);
                }
                else{
                    isChildRuleStateCollapsed = true;
                    childRulesTV.setMaxLines(3);
                }

            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        isMapReady = true;
        updateMap();

    }

    private void updateMap(){
        if(isMapReady && isApiResponseReady && !isMapUpdated){

            isMapUpdated = true;
            LatLng eventLoc = new LatLng(latlng.getLatitude(), latlng.getLongitude());
            mMap.addMarker(new MarkerOptions().position(eventLoc));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLoc, 13.0f));
        }
    }
}