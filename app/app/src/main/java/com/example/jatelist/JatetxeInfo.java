package com.example.jatelist;

import android.graphics.Bitmap;
import android.net.Uri;

/* Clase que representa el objeto JatetxeInfo utilizado para mostrar la informacion de los restaurantes de la db remota*/
public class JatetxeInfo {

    /*  This class define Jatetxea object */
    private String nombre;
;
    private String valoracion;




    public JatetxeInfo(String nombre,String valoracion) {
        this.nombre = nombre;

        this.valoracion = valoracion;

    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



    public String getValoracion() {
        return valoracion;
    }

    public void setValoracion(String valoracion) {
        this.valoracion = valoracion;
    }


}