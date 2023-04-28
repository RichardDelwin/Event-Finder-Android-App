package com.example.eventfinder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventfinder.DataClasses.SearchResponse;
import com.example.eventfinder.Helpers.GeneralHelpers;
import com.example.eventfinder.Helpers.SharedPreferencesAccessHelper;
import com.example.eventfinder.Interfaces.NewActivityCallBack;
import com.example.eventfinder.R;
import com.example.eventfinder.ViewModels.FavItemsViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>{

    private ArrayList<SearchResponse> favSearchResponses;
    private FavItemsViewModel favItemsViewModel;
    private NewActivityCallBack newActivityCallBack;
    private SharedPreferencesAccessHelper sharedPreferencesAccessHelper;

    public FavoritesAdapter(Context context, FavItemsViewModel favItemsViewModel, NewActivityCallBack callBack){
        favSearchResponses = new ArrayList<SearchResponse>();
        sharedPreferencesAccessHelper = new SharedPreferencesAccessHelper(context);
        this.favItemsViewModel = favItemsViewModel;
        this.newActivityCallBack = callBack;
    }


    @NonNull
    @Override
    public FavoritesAdapter.FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.FavoritesViewHolder holder, int position) {

        SearchResponse event = favSearchResponses.get(position);

        Picasso.get().load(event.getIcon()).into(holder.eventImage);

        holder.eventFavButton.setImageResource(R.mipmap.heart_filled_hdpi);
        holder.eventName.setText(event.getName());
        holder.eventVenue.setText(event.getVenue());
        holder.eventCategory.setText(event.getClassifications());
        holder.eventDate.setText(GeneralHelpers.getFormattedDate(event.getLocalDate()));
        holder.eventTime.setText(GeneralHelpers.getFormattedTime(event.getLocalTime()));

        holder.eventFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.eventFavButton.setImageResource(R.mipmap.heart_outline_hdpi);
                sharedPreferencesAccessHelper.unHeartThis(event);
                favSearchResponses.remove(event);
                if(favSearchResponses.size() == 0){
                    favItemsViewModel.getFavListEmptyStatus().setValue(Boolean.TRUE);
                }
                notifyDataSetChanged();

            }
        });

        holder.eventCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newActivityCallBack.onButtonClick(event.getId(), event.getName(), event);
            }
        });


    }

    @Override
    public int getItemCount() {
        return favSearchResponses.size();
    }

    public void setData(ArrayList<SearchResponse> newResponses){

        this.favSearchResponses = newResponses;
        notifyDataSetChanged();
    }

    public class FavoritesViewHolder extends RecyclerView.ViewHolder{

        public TextView eventName;
        public TextView eventVenue;
        public TextView eventCategory;
        public ShapeableImageView eventImage;
        public TextView eventDate;
        public TextView eventTime;
        public ImageView eventFavButton;
        public CardView eventCard;
        public FavoritesViewHolder(@NonNull View itemView) {
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

            eventCard = itemView.findViewById(R.id.event_searchCard);
        }
    }
}
