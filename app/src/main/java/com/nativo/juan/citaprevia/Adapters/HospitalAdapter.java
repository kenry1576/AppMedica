package com.nativo.juan.citaprevia.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nativo.juan.citaprevia.R;

import java.util.ArrayList;

/**
 * Created by juan on 5/15/18.
 */

public class HospitalAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> nombreHospital;
    private ArrayList<String> ratingText;
    private ArrayList<String> abiertoAhora;

    public HospitalAdapter (Context context, ArrayList<String> nombreHospital, ArrayList<String> ratingText, ArrayList<String> abiertoAhora)
    {
        this.context = context;
        this.nombreHospital = nombreHospital;
        this.ratingText = ratingText;
        this.abiertoAhora = abiertoAhora;
    }

    @Override
    public int getCount(){
        return nombreHospital.size();
    }

    @Override
    public Object getItem(int i){
        return nombreHospital.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 3232;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        if(view == null)
            view = view.inflate(context, R.layout.hospital_result_view, null);
        TextView hospital = view.findViewById(R.id.tv_hospital);
        TextView rating = view.findViewById(R.id.tv_rating);
        TextView abierto = view.findViewById(R.id.tv_abierto);

        hospital.setText(nombreHospital.get(i));
        rating.setText(ratingText.get(i));
        abierto.setText("Disponible: " + abiertoAhora.get(i));
        return view;
    }
}
