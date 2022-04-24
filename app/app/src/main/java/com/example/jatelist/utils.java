package com.example.jatelist;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class utils {
    /* Gestiona permisso de localizacion */
    public void locationPermission(Context contexto, Activity actividad) {
        if (ContextCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(actividad, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else if (ContextCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(actividad, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    /* Gestiona permisos camara */
    public void camaraPermission(Context contexto, Activity actividad) {
        if (ContextCompat.checkSelfPermission(contexto, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(actividad, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    /* Gestiona permisos de lectura y escritura */
    public void readPermission(Context contexto, Activity actividad) {
        if (ContextCompat.checkSelfPermission(contexto, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(actividad, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(contexto, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(actividad, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

    }

    /* Gestiona permisos contactos */
    public void writeContactsPermission(Context contexto, Activity actividad) {
        if (ContextCompat.checkSelfPermission(contexto, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(actividad, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }
        if (ContextCompat.checkSelfPermission(contexto, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(actividad, new String[]{Manifest.permission.WRITE_CONTACTS}, 1);
        }

    }
}
