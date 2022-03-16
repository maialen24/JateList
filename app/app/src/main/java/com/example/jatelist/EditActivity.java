package com.example.jatelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class EditActivity extends AppCompatActivity {

    private Boolean update = true;
    private db dbHelper = new db(this);
    private SQLiteDatabase db;
    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=dbHelper.getWritableDatabase();

        if (savedInstanceState!= null) {
            // cambioOrientacion= savedInstanceState.getInt(“contador");
            user = savedInstanceState.getString("user");
            update=savedInstanceState.getBoolean("update");

        }

        setContentView(R.layout.activity_edit);

        dbHelper = new db(this);
        db = dbHelper.getWritableDatabase();

        EditText izena=(EditText) findViewById(R.id.izena);
        EditText ubi=(EditText) findViewById(R.id.ubi);
        EditText valoracion=(EditText) findViewById(R.id.valoracion);
        EditText comments=(EditText) findViewById(R.id.comments);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("user");
            update = extras.getBoolean("update");
            if(update){
                izena.setText(extras.getString("izena"));
                ubi.setText(extras.getString("ubi"));
                valoracion.setText(extras.getString("valoracion"));
              //  comments.setText(extras.getString("comments"));
            }

        }










        ImageButton editButton=(ImageButton) findViewById(R.id.editButton);
        if (update){
            editButton.performClick();
        }else{
           // disableEditText(izena);
           // disableEditText(ubi);
           // disableEditText(valoracion);
            //disableEditText(comments);
        }

        //ImageButton editButton=(ImageButton) findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info","ENABLE EDITABLE");


                enableEditText(izena);
                enableEditText(ubi);
                enableEditText(valoracion);
                enableEditText(comments);

            }
        });

        ImageButton ezabatu= (ImageButton) findViewById(R.id.removeButton);
        ezabatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info","REMOVE JATETXEA");
                dbHelper.deleteJatetxea(izena.getText().toString(),user);

            }
        });


        ImageButton bueltatu= (ImageButton) findViewById(R.id.backButton);
        bueltatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info","GO TO MAIN ACTIVITY");
                Intent i = new Intent (EditActivity.this, MainActivity.class);
                startActivity(i);


            }
        });

        ImageButton gorde= (ImageButton) findViewById(R.id.saveButton);
        gorde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MIRAR SI YA EXISTE EN LA BASE DE DATOS O NO Y GUARDAR EN LA BASE DE DATOS
                Log.i("info","SAVE RESTAURANT AND DISABLE EDITABLE");

                if (update){
                    dbHelper.updateJatetxe(izena.getText().toString(),ubi.getText().toString(),valoracion.getText().toString(),comments.getText().toString(),user);
                }else{
                    dbHelper.insertJatetxe(izena.getText().toString(),ubi.getText().toString(),valoracion.getText().toString(),comments.getText().toString(),user);
                }

               // disableEditText(izena);
               // disableEditText(ubi);
               // disableEditText(valoracion);
                //disableEditText(comments);


            }
        });
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    private void enableEditText(EditText editText) {
        editText.setFocusable(true);
        editText.setEnabled(true);
        editText.setCursorVisible(true);

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
}