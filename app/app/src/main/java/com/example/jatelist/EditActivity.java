package com.example.jatelist;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.method.KeyListener;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EditActivity extends AppCompatActivity implements OnMapReadyCallback,ActivityCompat.OnRequestPermissionsResultCallback
         {
    private static final int CAMERA_REQUEST = 2;

     // instance for firebase storage and StorageReference
     FirebaseStorage storage;
     StorageReference storageReference;

     private static final int LOCATION_PERMISSION_ID = 1001;
    /* This class implements edit activity that is going to be used to add or update restaurants and to show more info about them */
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;


             private boolean locationPermissionGranted;
    private Boolean update = true;
    private db dbHelper = new db(this);
    private SQLiteDatabase db;
    private String user;
    private MapView map;
    private GoogleMap googleMap;
    String nameJatetxe;
    double lat=0;
    double lo=0;
    double userlat=0;
    double userlo=0;
    int CODIGO_GALERIA=4;
    String izena="";
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback actualizar;


    private static final  String MAPVIEW_BUNDLE_KEY="MapViewBundleKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new utils().permisosCamara(EditActivity.this, EditActivity.this);
        new utils().pedirpermisosLocalizar(EditActivity.this,EditActivity.this);
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

        //storage = FirebaseStorage.getInstance();
        //storageReference = storage.getReference();

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
                if(address.size()>0){
                    lat = address.get(0).getLatitude();
                    lo = address.get(0).getLongitude();
                }

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
              //  send();

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
                        delete(izena.getText().toString());
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

        // on click boton FOTO
        ImageButton foto= (ImageButton) findViewById(R.id.fotoButton);
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info","ADD FOTO JATETXEA");

                //alerta FOTO
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("IMAGE");
                builder.setMessage(v.getContext().getString(R.string.fotomssg));

                builder.setPositiveButton(v.getContext().getString(R.string.takePhoto), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Jatetxea argazkia atera
                        Intent camaraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        //camaraIntent.putExtra("user",user);
                        //camaraIntent.putExtra("izena",izena.getText().toString());
                        startActivityForResult(camaraIntent,CAMERA_REQUEST);




                    }
                });
                builder.setNeutralButton(v.getContext().getString(R.string.selectPhoto), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Jatetxea argazkia aukeratu
                        Intent elIntentGal = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        elIntentGal.putExtra("user",user);
                        elIntentGal.putExtra("izena",izena.getText().toString());

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
                    update(izena.getText().toString(),String.valueOf(valoracion.getRating()));
                }else{
                    //insert(izena.getText().toString(),ubi.getText().toString(),String.valueOf(valoracion.getRating()),comments.getText().toString(),tlf_number.getText().toString(),user);
                   succes=dbHelper.insertJatetxe(izena.getText().toString(),ubi.getText().toString(),String.valueOf(valoracion.getRating()),comments.getText().toString(),tlf_number.getText().toString(),user);
                   insert(izena.getText().toString(),String.valueOf(valoracion.getRating()));
                   sendtoNovedadesTopic(izena.getText().toString());
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

        this.googleMap=googleMap;
        if (update & lat!=0 & lo!=0) {
            LatLng jatetxea = new LatLng(lat, lo);
            googleMap.addMarker(new MarkerOptions()
                    .position(jatetxea)
                    .title(nameJatetxe));
            // [START_EXCLUDE silent]
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jatetxea, 15));
           // getlocation();

            //getUserLocation();
            getLocationRequest();
            Log.i("map", String.valueOf(userlat));
            Log.i("map", String.valueOf(userlo));
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


public boolean update(String izena, String rating){
    final Boolean[] emaitza = {false};
    Data.Builder data = new Data.Builder();

    data.putString("izena",izena);
    data.putString("rating",rating);
    data.putString("user",user);
    data.putFloat("ratingNumber",Float.parseFloat(rating));
    data.putString("funcion","update");


    data.putString("funcion","update");

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
public Boolean insert(String izena,String rating){
    final Boolean[] emaitza = {false};
    Data.Builder data = new Data.Builder();
    data.putString("funcion","insert");
    data.putString("izena",izena);
    data.putString("valoracion",rating);
    data.putString("user",user);
    data.putFloat("ratingNumber",Float.parseFloat(rating));


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

     public Boolean delete(String izena){
         final Boolean[] emaitza = {false};
         Data.Builder data = new Data.Builder();
         data.putString("funcion","delete");
         data.putString("izena",izena);
         data.putString("user",user);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_GALERIA && resultCode == RESULT_OK) {
            Uri imagenSeleccionada = data.getData();
            uploadImage(imagenSeleccionada);
            Bitmap img= null;
            try {
                img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagenSeleccionada);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Bitmap img = (Bitmap) data.getExtras().get("data");
            //guardar foto en server
            String img64=getEncodedString(img);

           // insertargazkia(user,nameJatetxe,imagenSeleccionada.toString());

        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap img = (Bitmap) data.getExtras().get("data");
            insertargazkia(user,nameJatetxe,getEncodedString(img));
            uploadcameraImage(img);
        }

    }
    private String getEncodedString(Bitmap foto){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.JPEG, 45, stream);
        byte[] fototransformada = stream.toByteArray();
        String fotoen64 = Base64.encodeToString(fototransformada,Base64.URL_SAFE);
        Log.i("proba",fotoen64);
        return fotoen64;
    }

    public void insertargazkia(String user, String izena, String foto){
        final Boolean[] emaitza = {false};
        Data.Builder data = new Data.Builder();

        data.putString("user",user);
        data.putString("izena",izena);
        data.putString("foto", foto);

       // data.putByteArray("foto",foto);
        data.putString("funcion","insert");

        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(argazkiakPHPconnect.class).setInputData(data.build()).build();
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
                                    Log.i("argazkia ","sartu");

                                }

                            }
                        }
                    }
                });
        WorkManager.getInstance(this).enqueue(otwr);
    }




    //cuando cambie la loc enviar mensaje
    public void send(){
        ServicioFirebase firebase=new ServicioFirebase();
        String token=firebase.generarToken();
        String mssg="";
        Log.i("Mensaje", mssg);
        Data.Builder data = new Data.Builder();
        //Se introducen los datos necesarios a pasar a ConexionPHP
        data.putString("mssg",mssg);
        data.putString("token",token);
        data.putString("jatetxe",nameJatetxe);

        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(sendMessagePHPconnect.class)
                .setInputData(data.build())
                .build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo)
                    {

                        if(workInfo != null && workInfo.getState().isFinished())
                        {
                            String result = workInfo.getOutputData().getString("result");
                            Log.i("mssg", result);
                        }
                    }
                });
        WorkManager.getInstance(this).enqueue(otwr);
    }

/*
             private Boolean permissionsGranted() {
                 return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
             }*/
/*
             @Override
             public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
                 super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                 if (requestCode == 123) {
                     if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                         // Permission granted.
                         //getlocation();
                     } else {
                         // User refused to grant permission. You can add AlertDialog here
                         Toast.makeText(this, "You didn't give permission to access device location", Toast.LENGTH_LONG).show();
                         startInstalledAppDetailsActivity();
                     }
                 }
             }*/

             private void startInstalledAppDetailsActivity() {
                 Intent i = new Intent();
                 i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                 i.addCategory(Intent.CATEGORY_DEFAULT);
                 i.setData(Uri.parse("package:" + getPackageName()));
                 i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 startActivity(i);
             }
/*
    private void getlocation(){
        FusedLocationProviderClient proveedordelocalizacion =
                LocationServices.getFusedLocationProviderClient(this);

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        userlo=location.getLongitude();
                        userlat=location.getLatitude();

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("map","error");
                    }
                });
    }

    */

/*

             public void getUserLocation(){

                     if (ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                         ActivityCompat.requestPermissions(EditActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                     }
                     else if (ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                         ActivityCompat.requestPermissions(EditActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                     }
                     if (ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                         fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                         fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>(){
                             @Override
                             public void onSuccess(Location location) {

                                 if (location != null) {
                                         userlat=location.getLatitude();
                                         userlo=location.getLongitude();
                                 }
                             }
                         });

                         LocationRequest peticion = LocationRequest.create();
                         peticion.setInterval(5000);
                         peticion.setFastestInterval(1000);
                         peticion.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                         actualizar = new LocationCallback() {
                             @Override
                             public void onLocationResult(LocationResult locationResult) {
                                 super.onLocationResult(locationResult);
                                 if(locationResult !=null) {
                                     userlat = locationResult.getLastLocation().getLatitude();
                                     userlo = locationResult.getLastLocation().getLongitude();
                                 }
                             }
                         };
                         fusedLocationProviderClient.requestLocationUpdates(peticion,actualizar, Looper.getMainLooper());
                     }
             }*/

     public void sendtoNovedadesTopic(String jatetxeIzena){
             ServicioFirebase firebase=new ServicioFirebase();
             String token=firebase.generarToken();
             String mssg="";
             Log.i("Mensaje", mssg);
             Data.Builder data = new Data.Builder();
             //Se introducen los datos necesarios a pasar a ConexionPHP
             //data.putString("mssg",mssg);
            data.putString("token",token);
             data.putString("jatetxe",jatetxeIzena);
             OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(sendMessagePHPconnect.class)
                     .setInputData(data.build())
                     .build();
             WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                     .observe(this, new Observer<WorkInfo>() {
                         @Override
                         public void onChanged(WorkInfo workInfo)
                         {
                             if(workInfo != null && workInfo.getState().isFinished())
                             {
                                 String result = workInfo.getOutputData().getString("result");
                                 Log.i("mssg", result);
                             }
                         }
                     });
             WorkManager.getInstance(this).enqueue(otwr);
         }


             private boolean comprobarPlayServices(){
                 GoogleApiAvailability api = GoogleApiAvailability.getInstance();
                 int code = api.isGooglePlayServicesAvailable(this);
                 if (code == ConnectionResult.SUCCESS) {
                     return true;
                 }
                 else {
                     if (api.isUserResolvableError(code)){
                         api.getErrorDialog(this, code, 58).show();
                     }
                     return false;
                 }
             }


             @SuppressLint("MissingPermission")
             private void getLocationRequest() {
                 FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
                 if (!checkPermission()) {
                     getLocationPermissions();
                     return;
                 }
                 client.getLastLocation()
                         .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                             @Override
                             public void onSuccess(Location location) {
                                 // Got last known location. In some rare situations this can be null.
                                 if (location != null) {
                                     // Logic to handle location object
                                     Log.e("TAG", "location = " + location);
                                 } else {
                                     Log.e("TAG", "not successful");
                                 }
                             }
                         });
             }

             private boolean checkPermission() {
                 return isGranted(ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)) &&
                         isGranted(ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION));
             }

             @TargetApi(Build.VERSION_CODES.M)
             private void getLocationPermissions() {
                 requestPermissions(new String[] {ACCESS_FINE_LOCATION},
                         PERMISSION_REQUEST_FINE_LOCATION);
             }

             @Override
             public void onRequestPermissionsResult(int code, @Nullable String permissions[], @Nullable int[] results) {
                 super.onRequestPermissionsResult(code, permissions, results);
                 switch (code) {
                     case PERMISSION_REQUEST_FINE_LOCATION:
                         if (isPermissionGranted(results)) {
                             getLocationRequest();
                         }
                 }
             }

             private boolean isPermissionGranted(int[] results) {
                 return results != null && results.length > 0 && isGranted(results[0]);
             }

             private boolean isGranted(int permission) {
                 return permission == PackageManager.PERMISSION_GRANTED;
             }

     // UploadImage method
     public void uploadImage(Uri uri){

         String izena=nameJatetxe.replace(" ","");
         String fileName=user+izena;
         storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);
         storageReference.putFile(uri)
                 .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                     @Override
                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                     }
                 }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {

             }
         });
     }

     public void uploadcameraImage(Bitmap bitmap){
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
         byte[] data = baos.toByteArray();

         String izena=nameJatetxe.replace(" ","");
         String fileName=user+izena;
         storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);

         storageReference.putBytes(data)
                 .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                     @Override
                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                     }
                 }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {

             }
         });
     }


}
