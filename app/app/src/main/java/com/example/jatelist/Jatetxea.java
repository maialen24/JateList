package com.example.jatelist;

public class Jatetxea {


        private String nombre;
        private String ubicacion;
        private String valoracion;
        private String comentarios;



        public Jatetxea(String nombre, String ubicacion,String valoracion, String comentarios) {
            this.nombre = nombre;
            this.ubicacion = ubicacion;
            this.valoracion = valoracion;
            this.comentarios= comentarios;
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

}
