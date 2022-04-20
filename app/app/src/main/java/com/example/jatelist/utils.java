package com.example.jatelist;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class utils {
    public void pedirpermisosLocalizar(Context contexto, Activity actividad) {
        if (ContextCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(actividad, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else if (ContextCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(actividad, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    public void permisosCamara(Context contexto, Activity actividad) {
    }
}
