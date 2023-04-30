package com.example.eventfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.eventfinder.Adapters.MainFragmentsAdapter;
import com.example.eventfinder.Fragments.FavoritesFragment;
import com.example.eventfinder.Fragments.SearchParentFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                1);

        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        tabLayout = findViewById(R.id.tabLayout_mainActivity);
        viewPager2 = findViewById(R.id.viewPager_mainActivity);

        MainFragmentsAdapter mainFragmentsAdapter = new MainFragmentsAdapter(getSupportFragmentManager(), getLifecycle());
        mainFragmentsAdapter.addFragment(new SearchParentFragment());
        mainFragmentsAdapter.addFragment(new FavoritesFragment());
        viewPager2.setAdapter(mainFragmentsAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, ((tab, position) -> {
            switch(position){
                case 0:
                    tab.setText("Search");
                    break;
                case 1:
                    tab.setText("Favorites");
                    break;
            }
        })
        ).attach();

    }
}