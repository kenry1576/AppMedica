package com.nativo.juan.citaprevia.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.nativo.juan.citaprevia.model.Specialities;

/**
 * Created by juan on 4/1/18.
 */

public class SpecialitiesAdapter extends ArrayAdapter<Specialities> {

    private Context esContext;
    private List<Specialities> esItems;

    public SpecialitiesAdapter(@NonNull Context context, @NonNull List<Specialities> items) {
        super(context, 0, items);
        esContext = context;
        esItems = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        Specialities specialities = esItems.get(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(esContext);
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        } else {
            view = convertView;
        }

        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(specialities.getName());

        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
