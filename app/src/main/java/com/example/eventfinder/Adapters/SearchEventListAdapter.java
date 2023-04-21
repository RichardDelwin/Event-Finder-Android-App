package com.example.eventfinder.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventfinder.DataClasses.SearchResponse;
import com.example.eventfinder.Helpers.GeneralHelpers;
import com.example.eventfinder.Helpers.SharedPreferencesAccessHelper;
import com.example.eventfinder.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchEventListAdapter extends RecyclerView.Adapter<SearchEventListAdapter.SearchEventListViewHolder> {

    private List<SearchResponse> searchResponses;
    private SharedPreferencesAccessHelper sharedPreferencesAccessHelper;
    boolean isFav = false;
    public SearchEventListAdapter(Context context){
        searchResponses = new ArrayList<SearchResponse>();
        sharedPreferencesAccessHelper = new SharedPreferencesAccessHelper(context);
    }
    @NonNull
    @Override
    public SearchEventListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new SearchEventListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchEventListViewHolder holder, int position) {

        SearchResponse event = searchResponses.get(position);

        Picasso.get().load(event.getIcon()).into(holder.eventImage);
//        Picasso.get().load(R.mipmap.heart_outline_hdpi).into(holder.eventFavButton);

        if(sharedPreferencesAccessHelper.idExists(event.getId())) {
            holder.eventFavButton.setImageResource(R.mipmap.heart_filled_hdpi);
        }else{
            holder.eventFavButton.setImageResource(R.mipmap.heart_outline_hdpi);
        }
        holder.eventName.setText(event.getName());
        holder.eventVenue.setText(event.getVenue());
        holder.eventCategory.setText(event.getClassifications());
        holder.eventDate.setText(GeneralHelpers.getFormattedDate(event.getLocalDate()));
        holder.eventTime.setText(GeneralHelpers.getFormattedTime(event.getLocalTime()));

        holder.eventFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFav){
                    isFav = false;
                    holder.eventFavButton.setImageResource(R.mipmap.heart_outline_hdpi);
                    sharedPreferencesAccessHelper.unHeartThis(event);

                }else{
                    isFav = true;
                    holder.eventFavButton.setImageResource(R.mipmap.heart_filled_hdpi);
                    sharedPreferencesAccessHelper.heartThis(event);
                }
            }
        });
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
            eventName.setSelected(true);
            eventVenue = itemView.findViewById(R.id.evntVenue);
            eventVenue.setSelected(true);
            eventCategory = itemView.findViewById(R.id.eventCategory);
            eventCategory.setSelected(true);
            eventImage = itemView.findViewById(R.id.eventImage);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventDate.setSelected(true);
            eventTime = itemView.findViewById(R.id.eventTime);
            eventTime.setSelected(true);
            eventFavButton = itemView.findViewById(R.id.eventFavButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Clicked", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}
