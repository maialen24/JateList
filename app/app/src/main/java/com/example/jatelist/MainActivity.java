package com.example.jatelist;

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
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /* This class implements main activity, show restaurants list */


    private RecyclerView rvJatetxeak;
    private GridLayoutManager glm;
    private JatetxeaAdapter adapter;
    private ArrayList<Jatetxea> data=new ArrayList<Jatetxea>();
    private String user;
    private boolean update=true;
    private int CODIGO_GALERIA=4;

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
                Bitmap img=dbHelper.getImage(user,a.get(5));
                //Bitmap img=null;
                byte[] image = getimage(user,a.get(0));
                Bitmap laimg=null;
                if (image!=null){
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
                    laimg = BitmapFactory.decodeStream(imageStream);
                }

                data.add(new Jatetxea(a.get(0), a.get(1), a.get(2), a.get(3), a.get(4),laimg));

            }

        }

        //initialize list
        rvJatetxeak = (RecyclerView) findViewById(R.id.rv_jatetxeak);

        glm = new GridLayoutManager(this, 1);
        rvJatetxeak.setLayoutManager(glm);
        adapter = new JatetxeaAdapter(data,user);
        rvJatetxeak.setAdapter(adapter);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_GALERIA && resultCode == RESULT_OK) {
            Uri imagenSeleccionada = data.getData();
            ImageView foto = (ImageView) findViewById(R.id.foto);
            foto.setImageURI(imagenSeleccionada);
            //gorde image uri in db

        }
    }


    public void getUserRestaurants(){
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
                            String emaitza = workInfo.getOutputData().getString("arrayresultados");
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
    }

    public byte[] getimage(String user, String izena){
        final Boolean[] emaitza = {false};
        Data.Builder data = new Data.Builder();

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
        byte[] a=null;

        return a;
    }
}