package com.example.eventfinder.Helpers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GeneralHelpers {

    public static String getFormattedDate(String localDate){
        LocalDate date = LocalDate.parse(localDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public static String getFormattedTime(String localTime) {
        LocalTime time = LocalTime.parse(localTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        return time.format(formatter);
    }
}
