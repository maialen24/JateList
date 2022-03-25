package com.example.jatelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class EditActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Boolean update = true;
    private db dbHelper = new db(this);
    private SQLiteDatabase db;
    private String user;
    private MapView map;
    String nameJatetxe;
    double lat;
    double lo;

    private static final  String MAPVIEW_BUNDLE_KEY="MapViewBundleKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db=dbHelper.getWritableDatabase();

        Bundle mapViewBundle=null;
        if (savedInstanceState!= null) {
            // cambioOrientacion= savedInstanceState.getInt(“contador");
            user = savedInstanceState.getString("user");
            update=savedInstanceState.getBoolean("update");
            mapViewBundle=savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
            lat=savedInstanceState.getDouble("latitude");
            lo=savedInstanceState.getDouble("longitud");
            nameJatetxe=savedInstanceState.getString("jatetxeName");


        }



        setContentView(R.layout.activity_edit);




        dbHelper = new db(this);
        db = dbHelper.getWritableDatabase();

        EditText izena=(EditText) findViewById(R.id.izena);
        EditText ubi=(EditText) findViewById(R.id.ubi);
       // EditText valoracion=(EditText) findViewById(R.id.valoracion);
        EditText comments=(EditText) findViewById(R.id.comments);
        EditText tlf_number=(EditText) findViewById(R.id.phoneNumber);
        RatingBar valoracion = (RatingBar) findViewById(R.id.rating);


        KeyListener izenalistener = izena.getKeyListener();
        KeyListener ubiListener = ubi.getKeyListener();
        KeyListener tlfListener = tlf_number.getKeyListener();
        //KeyListener valListener = valoracion.getKeyListener();
        KeyListener comListener = comments.getKeyListener();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("user");
            update = extras.getBoolean("update");
            if(update){
                izena.setText(extras.getString("izena"));
                ubi.setText(extras.getString("ubi"));
                valoracion.setRating(Float.parseFloat(extras.getString("valoracion")));
                //valoracion.setText(extras.getString("valoracion"));
                comments.setText(extras.getString("comentarios"));
                tlf_number.setText(extras.getString("tlf_number"));

            }

        }


        nameJatetxe=izena.getText().toString();

        Geocoder geocoder = new Geocoder(this);

        List<Address> address = null;

        try {
            address =  geocoder.getFromLocationName(ubi.getText().toString(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        lat= address.get(0).getLatitude();
        lo= address.get(0).getLongitude();

        map = (MapView) findViewById(R.id.mapview);
        map.onCreate(mapViewBundle);
        map.getMapAsync(  this);
        map.onResume();


        ImageButton editButton=(ImageButton) findViewById(R.id.editButton);
        if (!update){
            valoracion.setIsIndicator(false);
            editButton.performClick();
        }else{

            disableEditText(izena);
            disableEditText(ubi);
            disableEditText(tlf_number);
            valoracion.setIsIndicator(true);
            disableEditText(comments);
        }


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info","ENABLE EDITABLE");

                enableEditText(izena,izenalistener);
                enableEditText(ubi,ubiListener);
                valoracion.setIsIndicator(false);
                enableEditText(tlf_number,tlfListener);
                //enableEditText(valoracion,valListener);
                enableEditText(comments,comListener);

            }
        });

        ImageButton bueltatu= (ImageButton) findViewById(R.id.backButton);
        bueltatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info","GO TO MAIN ACTIVITY");
                Intent i = new Intent (EditActivity.this, MainActivity.class);
                i.putExtra("user",user);
                i.putExtra("update",update);
                startActivity(i);


            }
        });

        ImageButton ezabatu= (ImageButton) findViewById(R.id.removeButton);
        ezabatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info","REMOVE JATETXEA");

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Delete");
                builder.setMessage(v.getContext().getString(R.string.remove_mssg));

                builder.setPositiveButton(v.getContext().getString(R.string.si), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Jatetxea ezabatu
                        dbHelper.deleteJatetxea(izena.getText().toString(),user);
                        //pulsar button bueltatu
                        bueltatu.performClick();

                    }
                });

                builder.setNegativeButton(v.getContext().getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();


            }
        });




        ImageButton gorde= (ImageButton) findViewById(R.id.saveButton);
        gorde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MIRAR SI YA EXISTE EN LA BASE DE DATOS O NO Y GUARDAR EN LA BASE DE DATOS
                Log.i("info","SAVE RESTAURANT AND DISABLE EDITABLE");
                Boolean succes;
                if (update){
                    succes=dbHelper.updateJatetxe(izena.getText().toString(),ubi.getText().toString(),String.valueOf(valoracion.getRating()),comments.getText().toString(),tlf_number.getText().toString(),user);
                }else{
                   succes=dbHelper.insertJatetxe(izena.getText().toString(),ubi.getText().toString(),String.valueOf(valoracion.getRating()),comments.getText().toString(),tlf_number.getText().toString(),user);
                }

                disableEditText(izena);
                disableEditText(ubi);
                //disableEditText(valoracion);
                valoracion.setIsIndicator(true);
                disableEditText(comments);
                disableEditText(tlf_number);
                if (succes){
                    Toast.makeText(getApplicationContext(),v.getContext().getString(R.string.toast),Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton call= (ImageButton) findViewById(R.id.callButton);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MIRAR SI YA EXISTE EN LA BASE DE DATOS O NO Y GUARDAR EN LA BASE DE DATOS
                Log.i("info","MARK RESTAURANT NUMBER");
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tlf_number.getText()));
                startActivity(i);


            }
        });



    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        //editText.setBackgroundColor(Color.TRANSPARENT);
    }

    private void enableEditText(EditText editText, KeyListener listener) {
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        editText.setEnabled(true);
        editText.setCursorVisible(true);
        editText.setKeyListener(listener);

    }


    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //map.onSaveInstanceState(savedInstanceState);
        Bundle mapViewBundle =savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle==null){
            mapViewBundle=new Bundle();
            savedInstanceState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        map.onSaveInstanceState(mapViewBundle);
        savedInstanceState.putString("user",user );
        savedInstanceState.putBoolean("update", update);
        savedInstanceState.putDouble("latitude",lat);
        savedInstanceState.putDouble("longitud",lo);
        savedInstanceState.putString("jatetxeName",nameJatetxe);



    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        user = savedInstanceState.getString("user");
        update = savedInstanceState.getBoolean("update");
        lat=savedInstanceState.getDouble("latitude");
        lo=savedInstanceState.getDouble("longitud");
        nameJatetxe=savedInstanceState.getString("jatetxeName");
        ;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng jatetxea = new LatLng(lat, lo);
        googleMap.addMarker(new MarkerOptions()
                .position(jatetxea)
                .title(nameJatetxe));
        // [START_EXCLUDE silent]
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jatetxea, 15));

        //googleMap.setMinZoomPreference(6.0f);
       // googleMap.setMaxZoomPreference(14.0f);

    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }
    @Override
    protected void onStart() {
        super.onStart();
        map.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        map.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }




}