package com.example.eventfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout_mainActivity);
        viewPager2 = findViewById(R.id.viewPager_mainActivity);

        MainFragmentsAdapter mainFragmentsAdapter = new MainFragmentsAdapter(getSupportFragmentManager(), getLifecycle());
        mainFragmentsAdapter.addFragment(new SearchFragment());
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