package com.example.jatelist;

import android.graphics.Bitmap;
import android.net.Uri;

public class Jatetxea {

        /*  This class define Jatetxea object */
        private String nombre;
        private String ubicacion;
        private String valoracion;
        private String comentarios;
        private String tlf;
        private Bitmap image;



        public Jatetxea(String nombre, String ubicacion,String valoracion, String comentarios,String tlf_number, Bitmap image) {
            this.nombre = nombre;
            this.ubicacion = ubicacion;
            this.valoracion = valoracion;
            this.comentarios= comentarios;
            this.tlf=tlf_number;
            this.image=image;
        }

    public Bitmap getImage() { return image; }

    public void setImage(Bitmap image) { this.image = image; }

    public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getUbicacion() {
            return ubicacion;
        }

        public void setUbicacion(String ubicacion) {
            this.ubicacion = ubicacion;
        }

        public String getValoracion() {
            return valoracion;
        }

        public void setValoracion(String valoracion) {
            this.valoracion = valoracion;
        }

        public String getComentarios() {
            return comentarios;
        }

        public void setComentarios(String comentarios) {
            this.comentarios = comentarios;
        }

        public String getTlf() {
            return tlf;
        }

        public void setTlf(String ptlf) {
            this.tlf = ptlf;
        }

}
