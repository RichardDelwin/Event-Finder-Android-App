package com.example.eventfinder.Helpers;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class GeneralHelpers {

    public static String getFormattedDate(String localDate){
        if(localDate==null)
            return "";
        LocalDate date = LocalDate.parse(localDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public static String formatDateWithMonthName(String localDate){

        if(localDate==null)
            return "";

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(localDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Format the date to the desired format
        String formattedDate = new SimpleDateFormat("MMM dd, yyyy").format(date);

        return formattedDate;
    }

    public static String getFormattedTime(String localTime) {
        if(localTime==null)
            return "";
        LocalTime time = LocalTime.parse(localTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        return time.format(formatter);
    }

    public static String formatArtists(ArrayList<String> artistList){
        return TextUtils.join(" | ", artistList);
    }

    public static boolean valueExists(String value){
        return value != null && value != "";
    }

    public static String formatPriceRange(double min_double, double max_double){

        String min = String.valueOf(min_double);
        String max = String.valueOf(max_double);

        if(valueExists(min) && valueExists(max)){
            return min + " - " + max + " (USD)";
        } else if (valueExists(min) && !valueExists(max)) {
            return min + " - " + min + " (USD)";
        } else if (!valueExists(min) && valueExists(max)) {
            return max + " - " + max + " (USD)";
        }else{
            return "";
        }
    }
}
