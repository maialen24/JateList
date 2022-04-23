package com.example.jatelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    /* This class implements main activity, show restaurants list */

    private RecyclerView rvJatetxeak;
    private GridLayoutManager glm;
    private JatetxeaAdapter adapter;
    private ArrayList<Jatetxea> data=new ArrayList<Jatetxea>();
    private String user;
    private boolean update=true;
    private int CODIGO_GALERIA=4;
    private  Bitmap[] images;
    private Bitmap imageBitmap;
    private ArrayList<JatetxeInfo> allrestaurants=new ArrayList<JatetxeInfo>();
    private RecyclerView rvJatetxeakInfo;
    private GridLayoutManager glmInfo;
    private JatetxeInfoAdapter adapterInfo;
    private Boolean eginda=false;
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState!= null) {
            user = savedInstanceState.getString("user");
        }

        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("user");
        }

        // establecer a false, when restaurant clicked is true, otherwise (add button) false
        update=false;

        //get all restaurants register of the user
       // getUserRestaurants();
        db dbHelper = new db(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db != null) {
            // Hacer las operaciones que queramos sobre la base de datos
            ArrayList jatetxeList=dbHelper.getAllCotacts(user);

            for (int i = 0; i < jatetxeList.size(); i++) {
                ArrayList<String> a = (ArrayList<String>) jatetxeList.get(i);
                Log.i("data",a.get(0)+a.get(1)+a.get(2));
                //Bitmap img=dbHelper.getImage(user,a.get(5));
                //Bitmap img=null;
                //images[i]=
           /*     getimage(user,a.get(0));
                while(eginda){

                }*/
                downloadImage(user,a.get(0));
                /*
                if (images[i]!=null){
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
                    laimg = BitmapFactory.decodeStream(imageStream);
                }*/

                data.add(new Jatetxea(a.get(0), a.get(1), a.get(2), a.get(3), a.get(4),imageBitmap));

            }

        }
        getRestaurants();

        //initialize list
        tabYourList();
        TabLayout tablayout=(TabLayout) findViewById(R.id.tab);
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getText();
                int i= tablayout.getSelectedTabPosition();

                if (i==0){
                    tabYourList();

                }else{
                    tabSearch();

                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        // add button on click method, go to edit activity (update=false)
        ImageButton add= (ImageButton)findViewById((R.id.addButton));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info","ADD JATETXE");
                update=false;
                Intent i = new Intent (MainActivity.this, EditActivity.class);
                i.putExtra("user",user);
                i.putExtra("update",update);
                startActivity(i);

            }
        });


        // exit button on click method, return to login activity
        Button exit= (Button)findViewById((R.id.exitButton));
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),v.getContext().getString(R.string.toastOut),Toast.LENGTH_SHORT).show();
                Log.i("info","GO TO LOG IN ACTIVITY");
                Intent i = new Intent (MainActivity.this, login.class);

                startActivity(i);


            }
        });

    }

    private void tabYourList(){
        rvJatetxeak = (RecyclerView) findViewById(R.id.rv_jatetxeak);

        glm = new GridLayoutManager(MainActivity.this, 1);
        rvJatetxeak.setLayoutManager(glm);
        adapter = new JatetxeaAdapter(data,user);
        rvJatetxeak.setAdapter(adapter);
    }

    private void tabSearch(){
        rvJatetxeakInfo = (RecyclerView) findViewById(R.id.rv_jatetxeak);

        glmInfo = new GridLayoutManager(MainActivity.this, 1);
        rvJatetxeakInfo.setLayoutManager(glmInfo);
        adapterInfo = new JatetxeInfoAdapter(allrestaurants,user);
        rvJatetxeakInfo.setAdapter(adapterInfo);
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("user",user );
        savedInstanceState.putBoolean("update", update);


    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        user = savedInstanceState.getString("user");
        update = savedInstanceState.getBoolean("update");
     ;
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i("CONTROL","main ON PAUSE");

    }
    @Override
    protected void onStart() {

        super.onStart();
        Log.i("CONTROL"," main ON START");


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("CONTROL","main ON RESUME");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("CONTROL","main ON STOP");

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("CONTROL","main ON DESTROY");
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_GALERIA && resultCode == RESULT_OK) {
            Uri imagenSeleccionada = data.getData();
            ImageView foto = (ImageView) findViewById(R.id.foto);
            foto.setImageURI(imagenSeleccionada);
            //gorde image uri in db

        }
    }*/


    public void getRestaurants(){
        final Boolean[] emaitza = {false};
        Data.Builder data = new Data.Builder();

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
                            String emaitzaizena = workInfo.getOutputData().getString("izena");
                            String emaitzavaloracion = workInfo.getOutputData().getString("valoracion");
                            if (emaitza!=null) {
                                String[]  izenaList=emaitzaizena.split(",");
                                String[]  valoracionList=emaitzavaloracion.split(",");
                                for (int i=0;i<izenaList.length;i++){
                                    allrestaurants.add(new JatetxeInfo(izenaList[i],valoracionList[i]));
                                }
                            }
                        }
                    }
                });
        WorkManager.getInstance(this).enqueue(otwr);
    }

    public void getimage(String user, String izena){
        final Boolean[] emaitza = {false};
        Data.Builder data = new Data.Builder();
        eginda=false;
        data.putString("user",user);
        data.putString("izena",izena);
        data.putString("funcion","get");

        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(argazkiakPHPconnect.class).setInputData(data.build()).build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo)
                    {
                        //Si se puede iniciar sesión porque devulve true se cambiará la actividad cerrando en la que se encuentra. Si la devolución es null o no es true se mostrará un toast en la interfaz actual.
                        if(workInfo != null && workInfo.getState().isFinished())
                        {

                            Log.i("f","terminado");

                            String emaitza = workInfo.getOutputData().getString("foto");
                            //String tipo = workInfo.getOutputData().getString("tipo");
                            if (emaitza!=null) {
                                imageBitmap = toBitmap(emaitza);

                                eginda=true;

                              /*  if (emaitza.contains("content")){

                                        String path=getPathFromURI(Uri.parse(emaitza));
                                      //  imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), getgaleryImage(emaitza));

                                    if (path != null) {
                                        File f = new File(path);
                                        Uri selectedImageUri = Uri.fromFile(f);
                                        try {
                                            Bitmap vBitmap = MediaStore.Images.Media.getBitmap( MainActivity.this.getContentResolver(), selectedImageUri); // get bitmap
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }else{
                                    imageBitmap = toBitmap(emaitza);
                                    //images[images.length+1]=elBitmap;
                                }*/

                            }
                        }
                    }
                });
        WorkManager.getInstance(this).enqueue(otwr);

    }

    public Bitmap toBitmap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.URL_SAFE);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            Log.i("foto",e.getMessage());
            return null;
        }
    }



    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }


    public void downloadImage(String user,String izena){
        storageReference = FirebaseStorage.getInstance().getReference("images/");
        StorageReference pathReference = storageReference.child("space.jpg");
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageView elImageView= findViewById(R.id.imageView);
               // Glide.with(getApplicationContext()).load(uri).into(elImageView);
                BitmapDrawable drawable = (BitmapDrawable) elImageView.getDrawable();
                imageBitmap = drawable.getBitmap();
            }
        });

   /*     storageReference = FirebaseStorage.getInstance().getReference("images/"+user+izena.replace(" ","")+".jpg");

       // storageReference.getStream();
        try {
            File localfile=File.createTempFile("tempfile",".jpeg");
            Task<Uri> u=storageReference.getDownloadUrl();
            storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    imageBitmap=BitmapFactory.decodeFile(localfile.getAbsolutePath());
                }
            });{

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

*/
    }
}