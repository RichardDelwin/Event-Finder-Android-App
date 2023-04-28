package com.example.eventfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventfinder.Adapters.EventDetailsTabAdapter;
import com.example.eventfinder.DataClasses.ArtistResponse;
import com.example.eventfinder.DataClasses.SearchResponse;
import com.example.eventfinder.Fragments.ArtistTabFragment;
import com.example.eventfinder.Fragments.EventTabFragment;
import com.example.eventfinder.Fragments.FavoritesFragment;
import com.example.eventfinder.Fragments.SearchParentFragment;
import com.example.eventfinder.Fragments.VenueTabFragment;
import com.example.eventfinder.Helpers.SharedPreferencesAccessHelper;
import com.example.eventfinder.ViewModels.EventDetailsDataViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class EventDetailsActivity extends AppCompatActivity {

    private EventDetailsDataViewModel eventDetailsDataViewModel;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    private ImageView facebook;
    private ImageView twitter;
    private ImageView heartImage;
    private TextView eventName_Toolbar;
    private FrameLayout backButton;
    private SharedPreferencesAccessHelper sharedPreferencesAccessHelper;
    String eventId;
    String eventName;
    SearchResponse event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        facebook = findViewById(R.id.facebook_button);
        twitter = findViewById(R.id.twitter_button);
        heartImage = findViewById(R.id.heart_button);

        eventDetailsDataViewModel = new ViewModelProvider(this).get(EventDetailsDataViewModel.class);



        Bundle bundle = getIntent().getExtras();
        eventName = bundle.getString("eventName");
        eventId = bundle.getString("eventId");
        event = (SearchResponse) bundle.getSerializable("event");
        sharedPreferencesAccessHelper = new SharedPreferencesAccessHelper(this);

        eventDetailsDataViewModel.getTicketMasterLive().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String data) {

                facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent openURL = new Intent(Intent.ACTION_VIEW);
                        try {
                            openURL.setData(Uri.parse("https://www.facebook.com/sharer/sharer.php?u="+data+"&src=sdkpreparse"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        startActivity(openURL);
                    }
                });

                twitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent openURL = new Intent(Intent.ACTION_VIEW);
                        try {
                            openURL.setData(Uri.parse("http://twitter.com/share?text="+ URLEncoder.encode("Check "+ eventName, StandardCharsets.UTF_8.toString())+"&url="+data));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        startActivity(openURL);
                    }
                });
            }
        });


        resolveHeartStatus(eventId);
        heartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sharedPreferencesAccessHelper.idExists(eventId)){
                    sharedPreferencesAccessHelper.unHeartThis(event);
                    Snackbar.make(view, event.getName()+" removed from favorites", Snackbar.LENGTH_SHORT).show();
                }else{
                    sharedPreferencesAccessHelper.heartThis(event);
                    Snackbar.make(view, event.getName()+" added to favorites", Snackbar.LENGTH_SHORT).show();
                }
                resolveHeartStatus(eventId);
            }
        });

        tabLayout = findViewById(R.id.tabLayout_eventDetails);
        viewPager2 = findViewById(R.id.viewPager2_eventDetails);

        eventName_Toolbar = findViewById(R.id.eventName_toolBar);
        eventName_Toolbar.setText(eventName);
        eventName_Toolbar.setSelected(true);

        backButton = findViewById(R.id.backButton_eventDetails);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        EventDetailsTabAdapter EventDetailsTabAdapter = new EventDetailsTabAdapter(getSupportFragmentManager(), getLifecycle());
        EventDetailsTabAdapter.addFragment(new EventTabFragment());
        EventDetailsTabAdapter.addFragment(new ArtistTabFragment());
        EventDetailsTabAdapter.addFragment(new VenueTabFragment());
        viewPager2.setAdapter(EventDetailsTabAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, ((tab, position) -> {
            switch(position){
                case 0:
                    tab.setIcon(R.drawable.info_icon);
                    tab.setText("Details");
                    break;

                case 1:
                    tab.setText("Artist");
                    tab.setIcon(R.mipmap.artist_icon);

                    break;

                case 2:
                    tab.setText("Venue");
                    tab.setIcon(R.drawable.venue_icon);
                    break;
            }
        })
        ).attach();

    }

    private void resolveHeartStatus(String id){
        if(sharedPreferencesAccessHelper.idExists(id)){
            heartImage.setImageResource(R.mipmap.heart_filled_hdpi);
        }else{
            heartImage.setImageResource(R.mipmap.heart_outline_hdpi);
        }
    }
}