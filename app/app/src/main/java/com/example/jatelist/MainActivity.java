package com.example.jatelist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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
        db dbHelper = new db(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db != null) {
            // Hacer las operaciones que queramos sobre la base de datos
            ArrayList jatetxeList=dbHelper.getAllCotacts(user);

            for (int i = 0; i < jatetxeList.size(); i++) {
                ArrayList<String> a = (ArrayList<String>) jatetxeList.get(i);
                Log.i("data",a.get(0)+a.get(1)+a.get(2));
                Bitmap img=dbHelper.getImage(user,a.get(5));
                data.add(new Jatetxea(a.get(0), a.get(1), a.get(2), a.get(3), a.get(4),img));

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
}