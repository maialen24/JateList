package com.example.jatelist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
        holder.rbValoracion.setRating(Float.parseFloat(jatetxea.getValoracion()));
        holder.comentarios= jatetxea.getComentarios();
        holder.tlf_number=jatetxea.getTlf();
        Bitmap img=jatetxea.getImage();

        if(img!=null){
            holder.ivfoto.setImageBitmap(jatetxea.getImage());
        }
    }

    @Override
    public int getItemCount () {
        return data.size();
    }


    class JatetxeaViewHolder extends RecyclerView.ViewHolder{

        RatingBar rbValoracion;
        TextView tvUbicacion;
        TextView tvNombre;
        TextView tvValoracion;
        String user;
        String comentarios;
        String tlf_number;
        ImageView ivfoto;

        public boolean[] select;

        public JatetxeaViewHolder(View itemView,String username) {
            super(itemView);
            tvUbicacion = (TextView) itemView.findViewById(R.id.tv_ubicacion);
            tvNombre = (TextView) itemView.findViewById(R.id.tv_nombre);
            rbValoracion = (RatingBar) itemView.findViewById(R.id.rating);
            user=username;
            ivfoto = (ImageView) itemView.findViewById(R.id.foto);



            /* pass info to activity when on click*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("info","CLICK ON JATETXEA");
                    Intent i = new Intent (view.getContext(), EditActivity.class);
                    i.putExtra("izena",tvNombre.getText().toString());
                    i.putExtra("ubi",tvUbicacion.getText().toString());
                    //i.putExtra("valoracion",tvValoracion.getText().toString());
                    i.putExtra("valoracion",String.valueOf(rbValoracion.getRating()));
                    i.putExtra("user",user);
                    i.putExtra("update",true);
                    i.putExtra("comentarios",comentarios);
                    i.putExtra("tlf_number",tlf_number);

                    view.getContext().startActivity(i);

                }
            });

        }
    }

}


