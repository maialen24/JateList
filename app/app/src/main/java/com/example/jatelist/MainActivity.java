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
import java.io.FileNotFoundException;
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
        new utils().readPermission(MainActivity.this,MainActivity.this);

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

                getImage(user,a.get(0),i);

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

private Bitmap decode(String encodedimg){
    byte[] b = Base64.decode(encodedimg,Base64.URL_SAFE);
    Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length);
    return bitmap;

}


    private void getImage(String user,String izena, int index){
        eginda=false;
        final Boolean[] emaitza = {false};
        Data.Builder data = new Data.Builder();

        data.putString("funcion","get");
        data.putString("user",user);
        data.putString("izena",izena);

        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(argazkiakPHPconnect.class).setInputData(data.build()).build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo)
                    {
                        //Si se puede iniciar sesión porque devulve true se cambiará la actividad cerrando en la que se encuentra. Si la devolución es null o no es true se mostrará un toast en la interfaz actual.
                        if(workInfo != null && workInfo.getState().isFinished())
                        {
                            String emaitza = workInfo.getOutputData().getString("foto");
                            if (emaitza!=null) {

                                imageBitmap=decode(emaitza);
                                if(imageBitmap==null){
                                    try {
                                        Uri newUri = Uri.fromFile(new File(emaitza));
                                        imageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(newUri));
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }

                                    //imageBitmap =  BitmapFactory.decodeFile(emaitza);
                                }
                                updateData(user,imageBitmap,index);

                            }
                            eginda=true;
                        }
                    }
                });
        WorkManager.getInstance(this).enqueue(otwr);


    }



    private void updateData(String user, Bitmap img,int index){
        data.get(index).setImage(img);
        tabYourList();
    }
}
