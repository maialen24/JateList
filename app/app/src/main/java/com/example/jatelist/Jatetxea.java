package com.example.jatelist;

public class Jatetxea {

        /*  This class define Jatetxea object */
        private String nombre;
        private String ubicacion;
        private String valoracion;
        private String comentarios;
        private String tlf;



        public Jatetxea(String nombre, String ubicacion,String valoracion, String comentarios,String tlf_number) {
            this.nombre = nombre;
            this.ubicacion = ubicacion;
            this.valoracion = valoracion;
            this.comentarios= comentarios;
            this.tlf=tlf_number;
        }

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
