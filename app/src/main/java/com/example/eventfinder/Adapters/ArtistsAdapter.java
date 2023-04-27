package com.example.eventfinder.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventfinder.DataClasses.ArtistResponse;
import com.example.eventfinder.Helpers.GeneralHelpers;
import com.example.eventfinder.R;
import com.example.eventfinder.SharedDataClasses.EventDetailSharedData;
import com.example.eventfinder.ViewModels.EventDetailsDataViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ArtistsListViewHolder>{


//    private EventDetailSharedData eventDetailSharedData;
    private ArrayList<ArtistResponse> artistResponses;
    Context context;

    public ArtistsAdapter(ArrayList artistResponses){
        this.artistResponses = artistResponses;
    }

    public void setArtistsData(ArrayList artistResponses, Context context){
        this.artistResponses = artistResponses;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArtistsAdapter.ArtistsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_tab_card, parent, false);
        return new ArtistsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistsAdapter.ArtistsListViewHolder holder, int position) {

        ArtistResponse artistResponse = artistResponses.get(position);
        Picasso.get().load(artistResponse.getImage()).into(holder.artistImage);

        Picasso.get().load(artistResponse.getAlbums().get(0)).into(holder.albumImage1);
        Picasso.get().load(artistResponse.getAlbums().get(1)).into(holder.albumImage2);
        Picasso.get().load(artistResponse.getAlbums().get(2)).into(holder.albumImage3);

        holder.artistName.setSelected(true);
        holder.artistName.setText(artistResponse.getName());
        holder.artistFollowersCount.setText(GeneralHelpers.getFollowerInMorK(artistResponse.getFollowers()));
        holder.popularityScore.setText(artistResponse.getPopularity());
        holder.progressIndicator.setProgress(Integer.parseInt(artistResponse.getPopularity()));

        holder.spotifyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openURL = new Intent(Intent.ACTION_VIEW);
                openURL.setData(Uri.parse(artistResponse.getUrl()));
                context.startActivity(openURL);
            }
        });

    }

    @Override
    public int getItemCount() {
        return artistResponses==null? 0 : artistResponses.size();
    }

    public class ArtistsListViewHolder extends RecyclerView.ViewHolder{

        public TextView artistName;
        public TextView artistFollowersCount;
        public TextView spotifyLink;
        public TextView popularityScore;
        public ShapeableImageView artistImage;
        public ShapeableImageView albumImage1;
        public ShapeableImageView albumImage2;
        public ShapeableImageView albumImage3;

        public CircularProgressIndicator progressIndicator;
        public ArtistsListViewHolder(@NonNull View itemView) {
            super(itemView);

            artistName = itemView.findViewById(R.id.artistName_TV);
            artistFollowersCount = itemView.findViewById(R.id.artistFollowersCount);
            spotifyLink = itemView.findViewById(R.id.spotifyLink);
            popularityScore = itemView.findViewById(R.id.popularityScore);
            artistImage = itemView.findViewById(R.id.artistImage);

            albumImage1 = itemView.findViewById(R.id.albumImage1);
            albumImage2 = itemView.findViewById(R.id.albumImage2);
            albumImage3 = itemView.findViewById(R.id.albumImage3);

            progressIndicator = itemView.findViewById(R.id.progressBar_Popularity);
        }
    }
}
