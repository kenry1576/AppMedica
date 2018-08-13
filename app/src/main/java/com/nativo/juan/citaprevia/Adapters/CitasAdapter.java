package com.nativo.juan.citaprevia.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nativo.juan.citaprevia.R;

import java.util.ArrayList;
import java.util.List;

import com.nativo.juan.citaprevia.model.CitaDisplayList;

/**
 * Created by juan on 3/31/18.
 */

public class CitasAdapter extends RecyclerView.Adapter<CitasAdapter.ViewHolder> {

    private List<CitaDisplayList> mItems;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(CitaDisplayList clickedCita);
        void onCancelCita(CitaDisplayList cancelCita);
    }

    public CitasAdapter(Context context, List<CitaDisplayList> mItems) {
        this.mItems = mItems;
        this.context = context;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void swapItems(List<CitaDisplayList> citas) {
        if (citas == null) {
            mItems = new ArrayList<>(0);
        } else {
            mItems = citas;
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cita_lista_itemes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CitaDisplayList caCitas = mItems.get(position);

        View statusIndicator = holder.statusIndicator;

        // estado: se colorea indicador según el estado
        switch (caCitas.getCdlStatus()) {
            case "Activa":
                // mostrar botón
                holder.cancelButton.setVisibility(View.VISIBLE);
                statusIndicator.setBackgroundResource(R.color.activeStatus);
                break;
            case "Cumplida":
                // ocultar botón
                holder.cancelButton.setVisibility(View.GONE);
                statusIndicator.setBackgroundResource(R.color.completedStatus);
                break;
            case "Cancelada":
                // ocultar botón
                holder.cancelButton.setVisibility(View.GONE);
                statusIndicator.setBackgroundResource(R.color.cancelledStatus);
                break;
        }

        holder.date.setText(caCitas.getDateAndTimeForList());
        holder.service.setText(caCitas.getCdlSpecialities().getName());
        holder.doctor.setText(caCitas.getDoctors().getName());
        holder.medicalCenter.setText(caCitas.getDoctors().getMedicalCenter().getName());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView date;
        public TextView service;
        public TextView doctor;
        public TextView medicalCenter;
        public Button cancelButton;
        public View statusIndicator;

        public ViewHolder(View itemView) {
            super(itemView);

            statusIndicator = itemView.findViewById(R.id.indicator_appointment_status);
            date = (TextView) itemView.findViewById(R.id.text_appointment_date);
            service = (TextView) itemView.findViewById(R.id.text_medical_service);
            doctor = (TextView) itemView.findViewById(R.id.text_doctor_name);
            medicalCenter = (TextView) itemView.findViewById(R.id.text_medical_center);
            cancelButton = (Button) itemView.findViewById(R.id.button_cancel_appointment);

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onCancelCita(mItems.get(position));
                    }
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(mItems.get(position));
            }
        }
    }

}