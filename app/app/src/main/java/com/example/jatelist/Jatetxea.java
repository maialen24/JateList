package com.example.jatelist;

public class Jatetxea {


        private String nombre;
        private String ubicacion;
        private int valoracion;

        public Jatetxea(){

        }

        public Jatetxea(String nombre, String ubicacion, int valoracion) {
            this.nombre = nombre;
            this.ubicacion = ubicacion;
            this.valoracion = valoracion;
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

        public int getValoracion() {
            return valoracion;
        }

        public void setValoracion(int valoracion) {
            this.valoracion = valoracion;
        }

}
