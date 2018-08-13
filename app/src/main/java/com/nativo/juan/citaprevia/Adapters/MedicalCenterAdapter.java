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

import com.nativo.juan.citaprevia.model.MedicalCenter;

/**
 * Created by juan on 4/1/18.
 */

public class MedicalCenterAdapter extends ArrayAdapter<MedicalCenter> {

    private Context mcaContext;
    private List<MedicalCenter> mcaItems;

    public MedicalCenterAdapter(@NonNull Context context, @NonNull List<MedicalCenter> items) {
        super(context, 0, items);
        mcaContext = context;
        mcaItems = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        MedicalCenter medicalCenter = mcaItems.get(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mcaContext);
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        } else {
            view = convertView;
        }

        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(medicalCenter.getName());

        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
