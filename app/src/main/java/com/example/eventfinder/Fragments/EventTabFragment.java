package com.example.eventfinder.Fragments;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.eventfinder.DataClasses.EventDetailsResponse;
import com.example.eventfinder.DataClasses.SearchResponse;
import com.example.eventfinder.Helpers.GeneralHelpers;
import com.example.eventfinder.Helpers.ServerAccessHelper;
import com.example.eventfinder.Interfaces.VolleyCallBack;
import com.example.eventfinder.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;

//https://www.tutorialkart.com/kotlin-android/android-open-url-in-browser-activity/

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventTabFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventTabFragment extends Fragment {

    private TextView artistRes;
    private TextView venueRes;
    private TextView dateRes;
    private TextView timeRes;
    private TextView genresRes;
    private TextView priceRes;
    private TextView ticketStatusRes;
    private TextView buyTicketsRes;

    private ImageView seatMap_imageView;
    private TextView artistTV;
    private TextView venueTV;
    private TextView dateTV;
    private TextView timeTV;
    private TextView genresTV;
    private TextView priceTV;
    private TextView ticketStatusTV;
    private TextView buyTicketsTV;

    private String eventId;

    private ScrollView eventTabScroll;

    private ProgressBar eventTab_progressBar;

    private ServerAccessHelper serverAccessHelper;
    private RequestQueue requestQueue;

    private HashMap<String, Integer> map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle bundle = getActivity().getIntent().getExtras();
        eventId = bundle.getString("eventId");

        requestQueue = Volley.newRequestQueue(getActivity());
        serverAccessHelper = new ServerAccessHelper(requestQueue);

        map = new HashMap<>();

        map.put("onsale", R.color.onsale);
        map.put("offsale", R.color.offsale);
        map.put("rescheduled", R.color.rescheduled);
        map.put("postponed", R.color.postponed);
        map.put("cancelled", R.color.cancelled);


        return inflater.inflate(R.layout.fragment_event_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        artistRes = view.findViewById(R.id.artistRes);
        venueRes = view.findViewById(R.id.venueRes_TV);
        dateRes = view.findViewById(R.id.dateRes_TV);
        timeRes = view.findViewById(R.id.timeRes_TV);
        genresRes = view.findViewById(R.id.genresRes_TV);
        priceRes = view.findViewById(R.id.priceRes_TV);
        ticketStatusRes = view.findViewById(R.id.ticketStatusRes_TV);
        buyTicketsRes = view.findViewById(R.id.buyTicketsRes_TV);
        seatMap_imageView = view.findViewById(R.id.seatMap);

        artistTV = view.findViewById(R.id.artist_TV);
        venueTV = view.findViewById(R.id.venue_TV);
        dateTV = view.findViewById(R.id.date_TV);
        timeTV = view.findViewById(R.id.time_TV);
        genresTV = view.findViewById(R.id.genres_TV);
        priceTV = view.findViewById(R.id.price_TV);
        ticketStatusTV = view.findViewById(R.id.ticketStatus_TV);
        buyTicketsTV = view.findViewById(R.id.buyTickets_TV);

        eventTabScroll = view.findViewById(R.id.eventTab_scrollView);
        eventTab_progressBar = view.findViewById(R.id.eventTab_progressBar);

        eventTabScroll.setVisibility(View.GONE);
        eventTab_progressBar.setVisibility(View.VISIBLE);

        artistRes.setSelected(true);
        venueRes.setSelected(true);
        dateRes.setSelected(true);
        timeRes.setSelected(true);
        genresRes.setSelected(true);
        priceRes.setSelected(true);
        ticketStatusRes.setSelected(true);
        buyTicketsRes.setSelected(true);


        Toast.makeText(getActivity(), "Making api call eventSearch", Toast.LENGTH_SHORT).show();
        serverAccessHelper.getEventsDetails(eventId, new VolleyCallBack<EventDetailsResponse>() {
            @Override
            public void onSuccess(EventDetailsResponse eventDetailsResponse) {

                artistRes.setText(GeneralHelpers.formatArtists(eventDetailsResponse.getAttractions()));
                venueRes.setText(eventDetailsResponse.getVenue());

                resolveVisibilty(dateTV, dateRes, GeneralHelpers.formatDateWithMonthName(eventDetailsResponse.getLocalDate()));
                resolveVisibilty(timeTV, timeRes, GeneralHelpers.getFormattedTime(eventDetailsResponse.getLocalTime()));
                resolveVisibilty(genresTV, genresRes, GeneralHelpers.formatArtists(eventDetailsResponse.getClassifications()));
                resolveVisibilty(priceTV, priceRes, GeneralHelpers.formatPriceRange(eventDetailsResponse.getPriceRanges_min(), eventDetailsResponse.getPriceRanges_max()));
                if(resolveVisibilty(ticketStatusTV, ticketStatusRes, eventDetailsResponse.getTicketStatusName())){
                    CardView cardview = view.findViewById(R.id.cardView_ticketStatus);
                    cardview.setCardBackgroundColor(getResources().getColor(resolveTicketStatusColor(eventDetailsResponse.getTicketStatus())));
                }

                if(resolveVisibilty(buyTicketsTV, buyTicketsRes, eventDetailsResponse.getBuyAt())) {

                    buyTicketsRes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent openURL = new Intent(Intent.ACTION_VIEW);
                            openURL.setData(Uri.parse(eventDetailsResponse.getBuyAt()));
                            startActivity(openURL);
                        }
                    });
                }
                Picasso.get().load(eventDetailsResponse.getSeatMap()).into(seatMap_imageView);


                eventTab_progressBar.setVisibility(View.GONE);
                eventTabScroll.setVisibility(View.VISIBLE);
            }

        });

    }


    private boolean resolveVisibilty(TextView nameView, TextView dataView, String data){

        if(data != null && data!=""){
            dataView.setText(data);
            return true;
        }else{
            nameView.setVisibility(View.GONE);
            dataView.setVisibility(View.GONE);
            return false;
        }

    }

    private int resolveTicketStatusColor(String key){

        return map.get(key);
    }
}