package com.example.eventfinder.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.eventfinder.Adapters.ArtistsAdapter;
import com.example.eventfinder.Adapters.SearchEventListAdapter;
import com.example.eventfinder.DataClasses.ArtistResponse;
import com.example.eventfinder.FavoritesFragment;
import com.example.eventfinder.Helpers.ServerAccessHelper;
import com.example.eventfinder.Interfaces.VolleyCallBack;
import com.example.eventfinder.R;
import com.example.eventfinder.ViewModels.EventDetailsDataViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArtistTabFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistTabFragment extends Fragment {

    private EventDetailsDataViewModel eventDetailsDataViewModel;
    private RecyclerView recyclerView;
    private ArtistsAdapter artistsAdapter;

    private ServerAccessHelper serverAccessHelper;
    private RequestQueue requestQueue;
    private ProgressBar artistProgressBar;
    private ArrayList<ArtistResponse> artistsArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        requestQueue = Volley.newRequestQueue(getActivity());
        serverAccessHelper = new ServerAccessHelper(requestQueue);

        return inflater.inflate(R.layout.fragment_artist_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        artistProgressBar = view.findViewById(R.id.progressBar_artistTab);
        recyclerView = view.findViewById(R.id.artistTab_recyclerView);

        artistProgressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        eventDetailsDataViewModel = new ViewModelProvider(getActivity()).get(EventDetailsDataViewModel.class);
//                new ViewModelProvider(getParentFragmentManager().getFragments().get(0)).get(EventDetailsDataViewModel.class);
        Log.d("ARTIST TAB VIEWMODEL" , String.valueOf(eventDetailsDataViewModel.getArtistsCount()));
        eventDetailsDataViewModel.getArtistLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> musicians) {
                Log.d("ARTIST TAB VIEWMODEL ONCHANGE" , String.valueOf(eventDetailsDataViewModel.getArtistsCount()));
//                artistsAdapter.setArtistsData(artistResponses, view.getContext());

                getArtists(musicians);
            }
        });


        requestQueue.addRequestEventListener(new RequestQueue.RequestEventListener() {
            @Override
            public void onRequestEvent(Request<?> request, int event) {
                if(event == RequestQueue.RequestEvent.REQUEST_FINISHED && serverAccessHelper.numOfRequestsMade == 0){

                    artistsAdapter.setArtistsData(artistsArrayList, view.getContext());
//                    eventDetailSharedData.setArtistsList(artistsArrayList);
//                    eventDetailsDataViewModel.setArtistsList(artistsArrayList);
//                    Log.d("[UPDATING VIEWMODEL]", artistsArrayList.get(0).getName());
                    artistProgressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);


                }
            }
        });

        artistsAdapter = new ArtistsAdapter(eventDetailsDataViewModel.getArtists());
        recyclerView.setAdapter(artistsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

    }

    private void getArtists(ArrayList<String> artists) {

        artistsArrayList = new ArrayList<>();
        for (String artist : artists) {
            serverAccessHelper.getArtistDetails(artist, new VolleyCallBack<ArtistResponse>() {
                @Override
                public void onSuccess(ArtistResponse response) {

                    serverAccessHelper.numOfRequestsMade--;
                    artistsArrayList.add(response);
                }
            });
        }
    }
}