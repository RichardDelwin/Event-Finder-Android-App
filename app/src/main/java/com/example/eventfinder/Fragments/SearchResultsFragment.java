package com.example.eventfinder.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.eventfinder.Adapters.SearchEventListAdapter;
import com.example.eventfinder.DataClasses.FormData;
import com.example.eventfinder.DataClasses.Location;
import com.example.eventfinder.DataClasses.SearchObject;
import com.example.eventfinder.DataClasses.SearchResponse;
import com.example.eventfinder.EventDetailsActivity;
import com.example.eventfinder.Helpers.ServerAccessHelper;
import com.example.eventfinder.Interfaces.NewActivityCallBack;
import com.example.eventfinder.Interfaces.VolleyCallBack;
import com.example.eventfinder.Interfaces.VolleyCallBackArray;
import com.example.eventfinder.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResultsFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultsFragment extends Fragment {

    private ServerAccessHelper serverAccessHelper;
    private RequestQueue requestQueue;
    private ProgressBar progressBar;
    private FormData formData;
    private CardView backCard;
    private SearchObject searchObject;
    private SearchEventListAdapter searchEventListAdapter;
    private CardView NoEventsFound;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        requestQueue = Volley.newRequestQueue(getActivity());
        serverAccessHelper = new ServerAccessHelper(requestQueue);

        return inflater.inflate(R.layout.fragment_search_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        formData = (FormData) bundle.getSerializable("formData");

        NoEventsFound = view.findViewById(R.id.NoEventsFoundCard);
        recyclerView = view.findViewById(R.id.eventListRecycler);
        searchEventListAdapter = new SearchEventListAdapter(view.getContext(), new NewActivityCallBack<String>(){
            @Override
            public void onButtonClick(String data, String eventName, SearchResponse response) {

                Intent intent = new Intent(view.getContext(), EventDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("eventId", data);
                bundle.putString("eventName", eventName);
                bundle.putSerializable("event", response);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(searchEventListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {

                searchEventListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onViewDetachedFromWindow(@NonNull View view) {

            }
        });

        backCard = view.findViewById(R.id.backCard);
        progressBar = view.findViewById(R.id.progressBar);
        backCard.setVisibility(View.GONE);


        backCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_searchResultsFragment_to_searchFragment);
            }
        });

        resolveLocation_SearchApiCall();
    }

    private void makeSearchCall(Location location){

        searchObject = new SearchObject(
                formData.getKeyword(),
                formData.getCategory(),
                formData.getDistance(),
                location.getLatitude(),
                location.getLongitude());

        serverAccessHelper.search(searchObject, new VolleyCallBackArray<SearchResponse>() {

            @Override
            public void onSuccess(SearchResponse[] searchResponses) {

                if(searchResponses.length == 0){
                    NoEventsFound.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    backCard.setVisibility(View.VISIBLE);
                }else {
                    searchEventListAdapter.setData(searchResponses);
                    searchEventListAdapter.notifyDataSetChanged();

                    recyclerView.setVisibility(View.VISIBLE);
                    NoEventsFound.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    backCard.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        searchEventListAdapter.notifyDataSetChanged();
    }

    private void resolveLocation_SearchApiCall() {

        boolean auto_detect_loc = formData.getIsAutoDetect();
        if (auto_detect_loc) {
            serverAccessHelper.autoDetectLocation(new VolleyCallBack<Location>() {
                @Override
                public void onSuccess(Location location) {
                    makeSearchCall(location);
                }
            });

        } else {
            Log.d("PATH", "resolveLocation-else");
            String locationText = formData.getLocation();
            serverAccessHelper.geoCode(locationText, new VolleyCallBack<Location>() {
                @Override
                public void onSuccess(Location location) {
                    makeSearchCall(location);
                }

            });

            Log.d("PATH", "resolveLocation-else[EXIT]");
        }
    }
}