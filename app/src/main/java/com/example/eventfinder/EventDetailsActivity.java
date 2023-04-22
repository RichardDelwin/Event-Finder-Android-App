package com.example.eventfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.eventfinder.Adapters.EventDetailsTabAdapter;
import com.example.eventfinder.Fragments.EventTabFragment;
import com.example.eventfinder.Fragments.SearchParentFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class EventDetailsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        tabLayout = findViewById(R.id.tabLayout_eventDetails);
        viewPager2 = findViewById(R.id.viewPager2_eventDetails);

        EventDetailsTabAdapter EventDetailsTabAdapter = new EventDetailsTabAdapter(getSupportFragmentManager(), getLifecycle());
        EventDetailsTabAdapter.addFragment(new EventTabFragment());
        EventDetailsTabAdapter.addFragment(new FavoritesFragment());
        EventDetailsTabAdapter.addFragment(new FavoritesFragment());
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
}