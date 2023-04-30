package com.example.eventfinder.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
import com.example.eventfinder.DataClasses.ArtistResponse;
import com.example.eventfinder.Helpers.ServerAccessHelper;
import com.example.eventfinder.Interfaces.VolleyCallBack;
import com.example.eventfinder.Interfaces.VolleyCallBackArtist;
import com.example.eventfinder.R;
import com.example.eventfinder.ViewModels.EventDetailsDataViewModel;

import java.util.ArrayList;
import java.util.Arrays;

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
    private ArtistResponse[] artistArrayListOrdered;

    private CardView artistMusicUnavailable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestQueue = Volley.newRequestQueue(getActivity());
        serverAccessHelper = new ServerAccessHelper(requestQueue);
        artistProgressBar = view.findViewById(R.id.progressBar_artistTab);
        recyclerView = view.findViewById(R.id.artistTab_recyclerView);
        artistMusicUnavailable  = view.findViewById(R.id.artistMusicUnavailable);

        artistMusicUnavailable.setVisibility(View.GONE);
        artistProgressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);


        eventDetailsDataViewModel = new ViewModelProvider(getActivity()).get(EventDetailsDataViewModel.class);
//                new ViewModelProvider(getParentFragmentManager().getFragments().get(0)).get(EventDetailsDataViewModel.class);
//        Log.d("ARTIST TAB VIEWMODEL" , String.valueOf(eventDetailsDataViewModel.getArtistsCount()));
        eventDetailsDataViewModel.getArtistLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> musicians) {
                Log.d("ARTIST TAB VIEWMODEL ONCHANGE" , String.valueOf(eventDetailsDataViewModel.getArtistsCount()));
                if(musicians.size() == 0){
                    artistProgressBar.setVisibility(View.GONE);
                    artistMusicUnavailable.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
//                artistsAdapter.setArtistsData(artistResponses, view.getContext());
                artistArrayListOrdered = new ArtistResponse[musicians.size()];
                getArtists(musicians);
            }
        });


        requestQueue.addRequestEventListener(new RequestQueue.RequestEventListener() {
            @Override
            public void onRequestEvent(Request<?> request, int event) {
                if(event == RequestQueue.RequestEvent.REQUEST_FINISHED && serverAccessHelper.numOfRequestsMade == 0){

                    artistsArrayList = new ArrayList<>(Arrays.asList(artistArrayListOrdered));
                    if(artistsArrayList.size()==0){
                        artistMusicUnavailable.setVisibility(View.VISIBLE);
                    }else {
                        artistsAdapter.setArtistsData(artistsArrayList, view.getContext());
                        recyclerView.setVisibility(View.VISIBLE);
                    }
//                    eventDetailSharedData.setArtistsList(artistsArrayList);
//                    eventDetailsDataViewModel.setArtistsList(artistsArrayList);
//                    Log.d("[UPDATING VIEWMODEL]", artistsArrayList.get(0).getName());
                    artistProgressBar.setVisibility(View.GONE);
                }
            }
        });

        artistsAdapter = new ArtistsAdapter(eventDetailsDataViewModel.getArtists());
        recyclerView.setAdapter(artistsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

    }

    private void getArtists(ArrayList<String> artists) {

        int index= 0;
        for (String artist : artists) {
            serverAccessHelper.getArtistDetails(artist, index, new VolleyCallBackArtist<ArtistResponse>() {
                @Override
                public void onSuccess(ArtistResponse response, int index) {

                    serverAccessHelper.numOfRequestsMade--;
//                    artistsArrayList.add(response);
                    artistArrayListOrdered[index] = response;
                }
            });
            index++;
        }
    }
}