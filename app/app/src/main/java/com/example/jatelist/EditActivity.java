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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class EditActivity extends AppCompatActivity implements OnMapReadyCallback,ActivityCompat.OnRequestPermissionsResultCallback
         {
    private static final int CAMERA_REQUEST = 2;

     // instance for firebase storage and StorageReference
     private FirebaseStorage storage;
     private StorageReference storageReference;

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
    private FusedLocationProviderClient fusedLocationProviderClient;




    private static final  String MAPVIEW_BUNDLE_KEY="MapViewBundleKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new utils().camaraPermission(EditActivity.this, EditActivity.this);
        new utils().locationPermission(EditActivity.this,EditActivity.this);
        super.onCreate(savedInstanceState);

        db=dbHelper.getWritableDatabase();

        Bundle mapViewBundle=null;

        if (savedInstanceState!= null) {
            // cambioOrientacion= savedInstanceState.getInt(???contador");
            user = savedInstanceState.getString("user");
            update=savedInstanceState.getBoolean("update");
            mapViewBundle=savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
            lat=savedInstanceState.getDouble("latitude");
            lo=savedInstanceState.getDouble("longitud");
            nameJatetxe=savedInstanceState.getString("jatetxeName");

        }

        setContentView(R.layout.activity_edit);
        new utils().camaraPermission(EditActivity.this, EditActivity.this);
        new utils().locationPermission(EditActivity.this,EditActivity.this);

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

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
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
                       // Intent elIntentGal=new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                  //insert en db local
                   succes=dbHelper.insertJatetxe(izena.getText().toString(),ubi.getText().toString(),String.valueOf(valoracion.getRating()),comments.getText().toString(),tlf_number.getText().toString(),user);
                   //insert en db remota
                   insert(izena.getText().toString(),String.valueOf(valoracion.getRating()));
                   //enviar mensaje FCM a subscriptores new
                   sendtoNovedadesTopic(izena.getText().toString());
                }
                //deshabilitar la ediccion
                disableEditText(izena);
                disableEditText(ubi);
                //disableEditText(valoracion);
                valoracion.setIsIndicator(true);
                disableEditText(comments);
                disableEditText(tlf_number);
                onMapReady(googleMap);
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

    /* Metodo que llama al gestor que se ocupa de actualizar los datos de los restaurantes en la db remota */
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
                    //Si se puede iniciar sesi??n porque devulve true se cambiar?? la actividad cerrando en la que se encuentra. Si la devoluci??n es null o no es true se mostrar?? un toast en la interfaz actual.
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
/* Metodo que a??ade la tarea de insertar un nuevo restaurante en la db remota */
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
                    //Si se puede iniciar sesi??n porque devulve true se cambiar?? la actividad cerrando en la que se encuentra. Si la devoluci??n es null o no es true se mostrar?? un toast en la interfaz actual.
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
             /* Metodo que crea la tarea de borrar un restaurante de la db remota */
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
                         //Si se puede iniciar sesi??n porque devulve true se cambiar?? la actividad cerrando en la que se encuentra. Si la devoluci??n es null o no es true se mostrar?? un toast en la interfaz actual.
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

             /* Se gestionan las fotos elegidas o sacadas por el usuario */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        /* Si la foto se escoge desde galeria se guarda el path de la img */
        if (requestCode == CODIGO_GALERIA && resultCode == RESULT_OK) {
            Uri imagenSeleccionada = data.getData();

            uploadImage(imagenSeleccionada);
            Bitmap img= null;
            try {
              //  InputStream inputStream=getContentResolver().openInputStream(data.getData());
                //img=BitmapFactory.decodeStream(inputStream);
                img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagenSeleccionada);
               // String base64img=encodeGalery(img);
                insertargazkia(user,nameJatetxe,getPath(imagenSeleccionada));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        /* Si la img se captura desde la camara se guarda el string base64 del bitmap */
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap img = (Bitmap) data.getExtras().get("data");
           // uploadcameraImage(img);
            insertargazkia(user,nameJatetxe,encode(img));
        }

    }
             /* Convierte el bitmap en un string base64*/
    private String encode(Bitmap img){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b , Base64.URL_SAFE);
        return encodedImage;
    }
/*
     private String encodeGalery(Bitmap img){

         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         img.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
         byte[] b = baos.toByteArray();
         String encodedImage = Base64.encodeToString(b , Base64.URL_SAFE);
         return encodedImage;
     }*/

             /* Metodo que encola la tarea de almacenar la foto en la db remota */
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
                        //Si se puede iniciar sesi??n porque devulve true se cambiar?? la actividad cerrando en la que se encuentra. Si la devoluci??n es null o no es true se mostrar?? un toast en la interfaz actual.
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

/*
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
    }*/


             private void startInstalledAppDetailsActivity() {
                 Intent i = new Intent();
                 i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                 i.addCategory(Intent.CATEGORY_DEFAULT);
                 i.setData(Uri.parse("package:" + getPackageName()));
                 i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 startActivity(i);
             }

             /* Cuando un usuario a??ade un restaurante se ejecuta este metodo para enviar la notificacion a los subscriptores del topico new */
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

     // UploadImage method to firebase storage
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



             /* Metodo para conseguir el real path de una URI de imagen de galeria */
     public String getPath(Uri uri) {
         String[] projection = { MediaStore.Images.Media.DATA };
         Cursor cursor = managedQuery(uri, projection, null, null, null);
         startManagingCursor(cursor);
         int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
         cursor.moveToFirst();
         return cursor.getString(column_index);
     }





}
