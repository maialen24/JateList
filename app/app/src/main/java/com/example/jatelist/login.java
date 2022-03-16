package com.example.jatelist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class login extends AppCompatActivity implements DialogClass.Listener{

    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState!= null) {
            // cambioOrientacion= savedInstanceState.getInt(“contador");
            user = savedInstanceState.getString("user");

        }

        setContentView(R.layout.activity_login);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("user");
        }

        EditText userEditText=findViewById((R.id.username));
        EditText password=findViewById((R.id.password));
        db dbHelper = new db(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Button login= (Button)findViewById((R.id.loginButton));
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info","CHECK USERNAME AND PASSWORD");
                Log.i("info",userEditText.getText().toString());
                Log.i("info",password.getText().toString());
                if (dbHelper.checkCredentials(userEditText.getText().toString(), password.getText().toString())){
                    //GO TO MAIN ACTIVITY
                    user=userEditText.getText().toString();
                    Log.i("info","LOGIN SUCCESS");
                    Intent i = new Intent (login.this, MainActivity.class);
                    i.putExtra("user",user);
                    startActivity(i);
                }else{
                    //SHOW A INCORRET CREDENTIALS ALERT
                    Log.i("info","LOGIN INCORRECT");
                    AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
                    builder.setTitle("Error");
                    builder.setMessage("Username or password incorrect");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
            }
        });




        ImageButton newUser= (ImageButton)findViewById((R.id.signUp));
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogoalerta= new DialogClass();
                dialogoalerta.show(getSupportFragmentManager(), "etiqueta");






            }
        });
    }


    @Override
    public void alpulsarSI(String user, String password) {
        //SAVE USER AND PASSWORD IN DB
        Log.i("info","AL PULSAR SI METODO");
        Log.i("info",user);
        Log.i("info",password);



        db dbHelper = new db(this);
        //SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.insertUser(user, password);

    }

    @Override
    public void alpulsarNO() {
        //CLOSE DIALOG

    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("user",user );



    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        user = savedInstanceState.getString("user");


    }
}