package com.nativo.juan.citaprevia.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nativo.juan.citaprevia.R;
import com.nativo.juan.citaprevia.model.ArsModel;

import java.util.ArrayList;

/**
 * Created by juan on 5/6/18.
 */

public class ArsAdapter extends RecyclerView.Adapter<ArsAdapter.ViewHolder>  {
    private ArrayList<ArsModel> android;

    public ArsAdapter(ArrayList<ArsModel> ars)
    {
        this.android = ars;
    }

    @Override
    public ArsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArsAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tv_name.setText(android.get(i).getName());
        viewHolder.tv_version.setText(android.get(i).getVer());
        viewHolder.tv_api_level.setText(android.get(i).getApi());
    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_version,tv_api_level;
        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView)view.findViewById(R.id.ars_name);
            tv_version = (TextView)view.findViewById(R.id.ars_version);
            tv_api_level = (TextView)view.findViewById(R.id.ars_api_level);

        }
    }
}
