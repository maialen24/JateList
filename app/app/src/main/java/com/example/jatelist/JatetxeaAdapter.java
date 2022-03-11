package com.example.jatelist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class JatetxeaAdapter extends RecyclerView.Adapter {
    RecyclerView.Adapter<JatetxeaAdapter.JatetxeaViewHolder>{

        private ArrayList<Jatetxea> data;

    public JatetxeaAdapter(ArrayList<Jatetxea> data) {
            this.data = data;
        }

        @Override
        public JatetxeaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new JatetxeaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_musica, parent, false));
        }

        @Override
        public void onBindViewHolder(JatetxeaViewHolder holder, int position) {
            Jatetxea jatetxea = data.get(position);
            holder.imgMusica.setImageResource(jatetxea.getUbicacion());
            holder.tvNombre.setText(jatetxea.getNombre());
            holder.tvArtista.setText(jatetxea.getValoracion());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class JatetxeaViewHolder extends RecyclerView.ViewHolder{

            ImageView imgMusica;
            TextView tvNombre;
            TextView tvArtista;

            public JatetxeaViewHolder(View itemView) {
                super(itemView);
                imgMusica = (ImageView) itemView.findViewById(R.id.img_musica);
                tvNombre = (TextView) itemView.findViewById(R.id.tv_nombre);
                tvArtista = (TextView) itemView.findViewById(R.id.tv_artista);
            }
        }

}

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
