package com.example.jatelist;

import android.content.Intent;
import android.graphics.Bitmap;
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

import java.util.ArrayList;

public class JatetxeInfoAdapter extends RecyclerView.Adapter<JatetxeInfoAdapter.JatetxeInfoViewHolder>{


    private ArrayList<JatetxeInfo> data=new ArrayList<JatetxeInfo>();
    private static boolean[] select;
    private String user;


    public JatetxeInfoAdapter(ArrayList < JatetxeInfo > data,String username) {
        this.data = data;
        select= new boolean[80000];
        user=username;
    }

    @Override
    public JatetxeInfoViewHolder onCreateViewHolder (ViewGroup parent,int viewType){
        View elLayoutDeCadaItem= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jatetxeinfo,parent, false);
        JatetxeInfoViewHolder evh = new JatetxeInfoViewHolder(elLayoutDeCadaItem,user);
        evh.select = select;
        return evh;

    }

    @Override
    public void onBindViewHolder (JatetxeInfoViewHolder holder,int position){
        JatetxeInfo jatetxeInfo = data.get(position);

        holder.tvNombre.setText(jatetxeInfo.getNombre());
        holder.rbValoracion.setRating(Float.parseFloat(jatetxeInfo.getValoracion()));




    }

    @Override
    public int getItemCount () {
        return data.size();
    }


    class JatetxeInfoViewHolder extends RecyclerView.ViewHolder{

        RatingBar rbValoracion;

        TextView tvNombre;

        String user;


        public boolean[] select;

        public JatetxeInfoViewHolder(View itemView,String username) {
            super(itemView);
            ;
            tvNombre = (TextView) itemView.findViewById(R.id.tv_nombre);
            rbValoracion = (RatingBar) itemView.findViewById(R.id.rating);
            user=username;




            /* pass info to activity when on click*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });

        }
    }

}