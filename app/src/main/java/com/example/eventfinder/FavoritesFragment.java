package com.example.eventfinder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventfinder.Adapters.FavoritesAdapter;
import com.example.eventfinder.Adapters.SearchEventListAdapter;
import com.example.eventfinder.DataClasses.SearchResponse;
import com.example.eventfinder.Helpers.SharedPreferencesAccessHelper;
import com.example.eventfinder.ViewModels.FavItemsViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {

    private FavoritesAdapter favoritesAdapter;
    private RecyclerView recyclerView;
    private TextView noFavsTV;
    private ProgressBar progressBar;
    private SharedPreferencesAccessHelper sharedPreferencesAccessHelper;
    private View rootView;
    private FavItemsViewModel favItemsViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView =  inflater.inflate(R.layout.fragment_favorites, container, false);

        favItemsViewModel = new ViewModelProvider(requireActivity()).get(FavItemsViewModel.class);

        favItemsViewModel.isFavListEmpty.observe(getViewLifecycleOwner(), myArrayList -> {
            if (myArrayList) {
                Toast.makeText(getActivity(), "VIew Model", Toast.LENGTH_SHORT).show();
                recyclerView.setVisibility(View.GONE);
                noFavsTV.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
        rootView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {

                // Refresh the fragment as needed
//                Toast.makeText(view.getContext(), "Attached", Toast.LENGTH_SHORT).show();
                recyclerView.setVisibility(View.GONE);
                noFavsTV.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                populateFavList();
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                // Do nothing
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Toast.makeText(getActivity(), "Favs", Toast.LENGTH_SHORT).show();

        favoritesAdapter = new FavoritesAdapter(view.getContext(), favItemsViewModel);
        sharedPreferencesAccessHelper = new SharedPreferencesAccessHelper(view.getContext());

        recyclerView = view.findViewById(R.id.favRecyclerView);
        recyclerView.setAdapter(favoritesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        noFavsTV = view.findViewById(R.id.noFavsMessage);
        progressBar = view.findViewById(R.id.progressBar_Fav);

        recyclerView.setVisibility(View.GONE);
        noFavsTV.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        populateFavList();

    }


    private void populateFavList() {

        Log.d("SHARED PREF", "Checking Shred Prefs");
        ArrayList<SearchResponse> searchResponses= sharedPreferencesAccessHelper.getItemsFromTable();

        Log.d("SHARED PREF RESPONSE", "SIZE = "+searchResponses.size());
        favoritesAdapter.setData(searchResponses);
//        favoritesAdapter.notifyDataSetChanged();

        if(searchResponses.size() == 0){
            progressBar.setVisibility(View.GONE);
            noFavsTV.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}