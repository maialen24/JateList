package com.example.jatelist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private String user;

    public JatetxeaAdapter(ArrayList < Jatetxea > data,String username) {
        this.data = data;
        select= new boolean[80000];
        user=username;
    }

    @Override
    public JatetxeaViewHolder onCreateViewHolder (ViewGroup parent,int viewType){
        View elLayoutDeCadaItem= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jatetxea,parent, false);
        JatetxeaViewHolder evh = new JatetxeaViewHolder(elLayoutDeCadaItem,user);
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
        String user;
        public boolean[] select;

        public JatetxeaViewHolder(View itemView,String username) {
            super(itemView);
            tvUbicacion = (TextView) itemView.findViewById(R.id.tv_ubicacion);
            tvNombre = (TextView) itemView.findViewById(R.id.tv_nombre);
            tvValoracion = (TextView) itemView.findViewById(R.id.tv_valoracion);
            user=username;



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("info","CLICK ON JATETXEA");
                    Intent i = new Intent (view.getContext(), EditActivity.class);
                    i.putExtra("izena",tvNombre.getText().toString());
                    i.putExtra("ubi",tvUbicacion.getText().toString());
                    i.putExtra("valoracion",tvValoracion.getText().toString());
                    i.putExtra("user",user);
                    i.putExtra("update",true);

                    view.getContext().startActivity(i);

                }
            });

        }
    }

}


