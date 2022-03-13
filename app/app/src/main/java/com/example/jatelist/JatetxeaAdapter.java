package com.example.jatelist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class JatetxeaAdapter extends RecyclerView.Adapter<JatetxeaAdapter.JatetxeaViewHolder>{


    private ArrayList<Jatetxea> data=new ArrayList<Jatetxea>();
    private static boolean[] select;

    public JatetxeaAdapter(ArrayList < Jatetxea > data) {
        this.data = data;
        select= new boolean[80000];
    }

    @Override
    public JatetxeaViewHolder onCreateViewHolder (ViewGroup parent,int viewType){
        View elLayoutDeCadaItem= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jatetxea,parent, false);
        JatetxeaViewHolder evh = new JatetxeaViewHolder(elLayoutDeCadaItem);
        evh.select = select;
        return evh;

    }

    @Override
    public void onBindViewHolder (JatetxeaViewHolder holder,int position){
        Jatetxea jatetxea = data.get(position);
        holder.tvUbicacion.setText(jatetxea.getUbicacion());
        holder.tvNombre.setText(jatetxea.getNombre());
        holder.tvValoracion.setText(String.valueOf(jatetxea.getValoracion()));
    }

    @Override
    public int getItemCount () {
        return data.size();
    }


    class JatetxeaViewHolder extends RecyclerView.ViewHolder{

        TextView tvUbicacion;
        TextView tvNombre;
        TextView tvValoracion;
        public boolean[] select;

        public JatetxeaViewHolder(View itemView) {
            super(itemView);
            tvUbicacion = (TextView) itemView.findViewById(R.id.tv_ubicacion);
            tvNombre = (TextView) itemView.findViewById(R.id.tv_nombre);
            tvValoracion = (TextView) itemView.findViewById(R.id.tv_valoracion);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (select[getAdapterPosition()]==true){
                        select[getAdapterPosition()]=false;

                    }
                    else{

                    }
                }
            });

        }
    }

}


