package com.example.jatelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

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
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity implements OnMapReadyCallback {

    /* This class implements edit activity that is going to be used to add or update restaurants and to show more info about them */

    private Boolean update = true;
    private db dbHelper = new db(this);
    private SQLiteDatabase db;
    private String user;
    private MapView map;
    String nameJatetxe;
    double lat=0;
    double lo=0;
    int CODIGO_GALERIA=4;


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


        //crear instancia de la base de datos
        dbHelper = new db(this);
        db = dbHelper.getWritableDatabase();

        //instanciar los elementos del layout
        EditText izena=(EditText) findViewById(R.id.izena);
        EditText ubi=(EditText) findViewById(R.id.ubi);
       // EditText valoracion=(EditText) findViewById(R.id.valoracion);
        EditText comments=(EditText) findViewById(R.id.comments);
        EditText tlf_number=(EditText) findViewById(R.id.phoneNumber);
        RatingBar valoracion = (RatingBar) findViewById(R.id.rating);

        //guardar los listener
        KeyListener izenalistener = izena.getKeyListener();
        KeyListener ubiListener = ubi.getKeyListener();
        KeyListener tlfListener = tlf_number.getKeyListener();
        //KeyListener valListener = valoracion.getKeyListener();
        KeyListener comListener = comments.getKeyListener();

        //recuperar info de otras actividades
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


        //buscar la lat y long de la ubicacion del jatetxe
        nameJatetxe=izena.getText().toString();

        if (update) {
            Geocoder geocoder = new Geocoder(this);
            List<Address> address = new ArrayList<>();

            try {
                address = geocoder.getFromLocationName(ubi.getText().toString(), 1);
                lat = address.get(0).getLatitude();
                lo = address.get(0).getLongitude();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //crear mapa
        map = (MapView) findViewById(R.id.mapview);
        map.onCreate(mapViewBundle);
        map.getMapAsync(  this);
        map.onResume();

        //habilitar o deshabilitar dependiendo si es update o new restaurant
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

        //on click del boton edit (habilitar la ediccion)
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

        //on click boton bueltatu (volver a la main activity)
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

        // on click boton borrar (eliminar el restaurante de la db y volver a la main activity)
        ImageButton ezabatu= (ImageButton) findViewById(R.id.removeButton);
        ezabatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info","REMOVE JATETXEA");

                //alerta de seguridad para borrar
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

        // on click boton borrar (eliminar el restaurante de la db y volver a la main activity)
        ImageButton foto= (ImageButton) findViewById(R.id.fotoButton);
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info","ADD JATETXEA");

                //alerta de seguridad para borrar
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("IMAGE");
                builder.setMessage(v.getContext().getString(R.string.fotomssg));

                builder.setPositiveButton(v.getContext().getString(R.string.takePhoto), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Jatetxea argazkia atera
                        //dbHelper.deleteJatetxea(izena.getText().toString(),user);
                        //pulsar button bueltatu
                        //bueltatu.performClick();

                    }
                });
                builder.setNeutralButton(v.getContext().getString(R.string.selectPhoto), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Jatetxea argazkia aukeratu
                        Intent elIntentGal = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(elIntentGal, CODIGO_GALERIA);


                    }
                });


                builder.show();


            }
        });



        //on click boton guardar (se inserta o actualiza los datos de la db)
        ImageButton gorde= (ImageButton) findViewById(R.id.saveButton);
        gorde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MIRAR SI YA EXISTE EN LA BASE DE DATOS O NO Y GUARDAR EN LA BASE DE DATOS
                Log.i("info","SAVE RESTAURANT AND DISABLE EDITABLE");
                Boolean succes;
                if (update){
                    //update(izena.getText().toString(),ubi.getText().toString(),String.valueOf(valoracion.getRating()),comments.getText().toString(),tlf_number.getText().toString(),user);
                    succes=dbHelper.updateJatetxe(izena.getText().toString(),ubi.getText().toString(),String.valueOf(valoracion.getRating()),comments.getText().toString(),tlf_number.getText().toString(),user);
                }else{
                    //insert(izena.getText().toString(),ubi.getText().toString(),String.valueOf(valoracion.getRating()),comments.getText().toString(),tlf_number.getText().toString(),user);
                   succes=dbHelper.insertJatetxe(izena.getText().toString(),ubi.getText().toString(),String.valueOf(valoracion.getRating()),comments.getText().toString(),tlf_number.getText().toString(),user);
                }
                //deshabilitar la ediccion
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

        //click boton call, marca el numero para llamada
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

    //metodo laguntzaile para deshabilitar la edicion
    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        //editText.setBackgroundColor(Color.TRANSPARENT);
    }

    //metodo laguntzaile para habilitar la edicion
    private void enableEditText(EditText editText, KeyListener listener) {
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        editText.setEnabled(true);
        editText.setCursorVisible(true);
        editText.setKeyListener(listener);

    }

    //guardar info
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
    //conseguir info
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        user = savedInstanceState.getString("user");
        update = savedInstanceState.getBoolean("update");
        lat=savedInstanceState.getDouble("latitude");
        lo=savedInstanceState.getDouble("longitud");
        nameJatetxe=savedInstanceState.getString("jatetxeName");

        ;
    }

    //crear marker en el mapa
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (update & lat!=0 & lo!=0) {
            LatLng jatetxea = new LatLng(lat, lo);
            googleMap.addMarker(new MarkerOptions()
                    .position(jatetxea)
                    .title(nameJatetxe));
            // [START_EXCLUDE silent]
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jatetxea, 15));
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
        Log.i("CONTROL", "EDIT ON PAUSE");
    }
    @Override
    protected void onStart() {
        super.onStart();
        map.onStart();
        Log.i("CONTROL", "EDIT ON START");
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
        Log.i("CONTROL", "EDIT ON RESUME");
    }

    @Override
    protected void onStop() {
        super.onStop();
        map.onStop();
        Log.i("CONTROL", "EDIT ON STOP");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
        Log.i("CONTROL", "EDIT ON DESTROY");
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
        Log.i("CONTROL", "EDIT ON LOW MEMORY");
    }


public boolean update(String izena, String ubi,String rating,String comments,String tlf,String user){
    final Boolean[] emaitza = {false};
    Data.Builder data = new Data.Builder();

    data.putString("user",user);

    data.putString("izena",izena);
    data.putString("ubi",ubi);
    data.putString("valoracion",rating);
    data.putString("tlf",tlf);
    data.putString("comentarios",comments);


    data.putString("funcion","get");

    OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(jatetxeakPHPconnect.class).setInputData(data.build()).build();
    WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
            .observe(this, new Observer<WorkInfo>() {
                @Override
                public void onChanged(WorkInfo workInfo)
                {
                    //Si se puede iniciar sesión porque devulve true se cambiará la actividad cerrando en la que se encuentra. Si la devolución es null o no es true se mostrará un toast en la interfaz actual.
                    if(workInfo != null && workInfo.getState().isFinished())
                    {
                        String emaitza = workInfo.getOutputData().getString("result");
                        if (emaitza!=null) {
                            if (emaitza.equals("true")) {
                                //GO TO MAIN ACTIVITY
                                Log.i("jatetxeak","lortu");

                            }

                        }
                    }
                }
            });
    WorkManager.getInstance(this).enqueue(otwr);
    return emaitza[0];
}
public Boolean insert(String izena, String ubi,String rating,String comments,String tlf,String user){
    final Boolean[] emaitza = {false};
    Data.Builder data = new Data.Builder();

    data.putString("user",user);

    data.putString("funcion","get");

    OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(jatetxeakPHPconnect.class).setInputData(data.build()).build();
    WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
            .observe(this, new Observer<WorkInfo>() {
                @Override
                public void onChanged(WorkInfo workInfo)
                {
                    //Si se puede iniciar sesión porque devulve true se cambiará la actividad cerrando en la que se encuentra. Si la devolución es null o no es true se mostrará un toast en la interfaz actual.
                    if(workInfo != null && workInfo.getState().isFinished())
                    {
                        String emaitza = workInfo.getOutputData().getString("result");
                        if (emaitza!=null) {
                            if (emaitza.equals("true")) {
                                //GO TO MAIN ACTIVITY
                                Log.i("jatetxeak","lortu");

                            }

                        }
                    }
                }
            });
    WorkManager.getInstance(this).enqueue(otwr);
    return emaitza[0];
}

}