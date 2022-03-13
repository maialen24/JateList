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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    private RecyclerView rvMusicas;
    private GridLayoutManager glm;
    private JatetxeaAdapter adapter;
    private ArrayList<Jatetxea> data=new ArrayList<Jatetxea>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data.add(new Jatetxea("kaixo","jatetxe","berria"));


        String user="";
        String password="";
        db dbHelper = new db(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db != null) {
            // Hacer las operaciones que queramos sobre la base de datos
            ArrayList jatetxeList=dbHelper.getAllCotacts();

            for (int i = 0; i < jatetxeList.size(); i++) {
                ArrayList<String> a = (ArrayList<String>) jatetxeList.get(0);
                data.add(new Jatetxea(a.get(0), a.get(1), a.get(2)));

            }

        }


        rvMusicas = (RecyclerView) findViewById(R.id.rv_jatetxeak);

        glm = new GridLayoutManager(this, 2);
        rvMusicas.setLayoutManager(glm);
        adapter = new JatetxeaAdapter(data);
        rvMusicas.setAdapter(adapter);


        ImageButton add= (ImageButton)findViewById((R.id.addButton));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info","ADD JATETXE");

            }
        });



        Button exit= (Button)findViewById((R.id.exitButton));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info","GO TO LOG IN ACTIVITY");
                Intent i = new Intent (MainActivity.this, login.class);
                startActivity(i);


            }
        });

    }



}