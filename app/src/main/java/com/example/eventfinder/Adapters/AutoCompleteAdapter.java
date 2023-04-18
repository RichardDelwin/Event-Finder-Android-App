package com.example.eventfinder.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter {

    ArrayList<String> suggestions;

//    public AutoCompleteAdapter(@NonNull Context context, int resource, @NonNull List objects) {
//        super(context, resource, objects);
//    }

    public AutoCompleteAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        suggestions = new ArrayList<>();
    }

    public void setArrayList(ArrayList<String> arrList){
        suggestions = arrList;
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return suggestions.get(position);
    }
}
