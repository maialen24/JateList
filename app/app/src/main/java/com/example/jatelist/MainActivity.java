package com.example.jatelist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    private RecyclerView rvJatetxeak;
    private GridLayoutManager glm;
    private JatetxeaAdapter adapter;
    private ArrayList<Jatetxea> data=new ArrayList<Jatetxea>();
    private String user;
    private boolean update=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState!= null) {
            // cambioOrientacion= savedInstanceState.getInt(“contador");
            user = savedInstanceState.getString("user");

        }


        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("user");

            //The key argument here must match that used in the other activity
        }
       // data.add(new Jatetxea("kaixo","jatetxe","berria"));

        update=false;

        db dbHelper = new db(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db != null) {
            // Hacer las operaciones que queramos sobre la base de datos
            ArrayList jatetxeList=dbHelper.getAllCotacts(user);

            for (int i = 0; i < jatetxeList.size(); i++) {
                ArrayList<String> a = (ArrayList<String>) jatetxeList.get(i);
                Log.i("data",a.get(0)+a.get(1)+a.get(2));
                data.add(new Jatetxea(a.get(0), a.get(1), a.get(2), a.get(3)));

            }

        }


        rvJatetxeak = (RecyclerView) findViewById(R.id.rv_jatetxeak);

        glm = new GridLayoutManager(this, 2);
        rvJatetxeak.setLayoutManager(glm);
        adapter = new JatetxeaAdapter(data,user);
        rvJatetxeak.setAdapter(adapter);


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

    public String getUser(){
        return user;
    }


}