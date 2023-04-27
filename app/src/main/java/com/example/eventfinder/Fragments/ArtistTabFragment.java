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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_artist_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        eventDetailsDataViewModel = new ViewModelProvider(getParentFragmentManager().getFragments().get(0)).get(EventDetailsDataViewModel.class);
        Log.d("ARTIST TAB VIEWMODEL" , String.valueOf(eventDetailsDataViewModel.getArtistsCount()));
        eventDetailsDataViewModel.getArtistLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<ArtistResponse>>() {
            @Override
            public void onChanged(ArrayList<ArtistResponse> artistResponses) {
                Log.d("ARTIST TAB VIEWMODEL ONCHANGE" , String.valueOf(eventDetailsDataViewModel.getArtistsCount()));
                artistsAdapter.setArtistsData(artistResponses, view.getContext());
            }
        });

        recyclerView = view.findViewById(R.id.artistTab_recyclerView);
        artistsAdapter = new ArtistsAdapter(eventDetailsDataViewModel.getArtists());
        recyclerView.setAdapter(artistsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

    }
}