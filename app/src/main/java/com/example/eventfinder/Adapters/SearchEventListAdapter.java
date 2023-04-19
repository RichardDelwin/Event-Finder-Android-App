package com.example.eventfinder.Adapters;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventfinder.DataClasses.SearchResponse;
import com.example.eventfinder.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchEventListAdapter extends RecyclerView.Adapter<SearchEventListAdapter.SearchEventListViewHolder> {

    private List<SearchResponse> searchResponses;

    @NonNull
    @Override
    public SearchEventListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new SearchEventListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchEventListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return searchResponses.size();
    }

    public void setData(SearchResponse[] newResponses){

        searchResponses = Arrays.asList(newResponses);
    }
    public class SearchEventListViewHolder extends RecyclerView.ViewHolder{

        public TextView eventName;
        public TextView eventVenue;
        public TextView eventCategory;
        public ShapeableImageView eventImage;
        public TextView eventDate;
        public TextView eventTime;
        public ImageView eventFavButton;

        public SearchEventListViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.eventName);
            eventVenue = itemView.findViewById(R.id.evntVenue);
            eventCategory = itemView.findViewById(R.id.eventCategory);
            eventImage = itemView.findViewById(R.id.eventImage);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventTime = itemView.findViewById(R.id.eventTime);
            eventFavButton = itemView.findViewById(R.id.eventFavButton);

        }
    }

}
