package com.example.jatelist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;

public class login extends AppCompatActivity implements DialogClass.Listener{

    private String user;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState!= null) {
            // cambioOrientacion= savedInstanceState.getInt(“contador");
            user = savedInstanceState.getString("user");
            language =savedInstanceState.getString("language");

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
                    builder.setMessage(v.getContext().getString(R.string.login_mssg));
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
            }
        });

        Switch mode=(Switch) findViewById(R.id.nightMode);
        mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApplication().setTheme(android.R.style.Theme);

            }
        });

        //euskera button clicked, change language

        TextView eu=(TextView) findViewById(R.id.euskera);
        eu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info-paso","Euskera");
                language="eu";
                Locale nuevaloc = new Locale(language);
                Locale.setDefault(nuevaloc);
                Configuration configuration = getBaseContext().getResources().getConfiguration();
                configuration.setLocale(nuevaloc);
                configuration.setLayoutDirection(nuevaloc);

                Context context = getBaseContext().createConfigurationContext(configuration);
                getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

                finish();
                startActivity(getIntent());
            }
        });

        // english button clicked change language
        TextView en=(TextView) findViewById(R.id.ingles);
        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info-paso","Ingelesa");
                language="en";
                Locale nuevaloc = new Locale(language);
                Locale.setDefault(nuevaloc);
                Configuration configuration = getBaseContext().getResources().getConfiguration();
                configuration.setLocale(nuevaloc);
                configuration.setLayoutDirection(nuevaloc);

                Context context = getBaseContext().createConfigurationContext(configuration);
                getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

                finish();
                startActivity(getIntent());
            }
        });

        //spanish button clicked change language
        TextView es=(TextView) findViewById(R.id.español);
        es.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info-paso","Castellano");
                language="es";
                Locale nuevaloc = new Locale(language);
                Locale.setDefault(nuevaloc);
                Configuration configuration = getBaseContext().getResources().getConfiguration();
                configuration.setLocale(nuevaloc);
                configuration.setLayoutDirection(nuevaloc);

                Context context = getBaseContext().createConfigurationContext(configuration);
                getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

                finish();
                startActivity(getIntent());
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

        NotificationManager elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(this, "1");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("1", "login",
                    NotificationManager.IMPORTANCE_DEFAULT);
            elManager.createNotificationChannel(elCanal);
            elCanal.setDescription("Descripción del canal");
            elCanal.enableLights(true);
            elCanal.setLightColor(Color.RED);
            elCanal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            elCanal.enableVibration(true);
        }
        elBuilder.setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle("Sign up")
                .setContentText(getString(R.string.usercreate_mssg))
                .setSubText("Users")
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setAutoCancel(true);
        elManager.notify(1, elBuilder.build());



    }

    @Override
    public void alpulsarNO() {
        //CLOSE DIALOG

    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("user",user );
        savedInstanceState.putString("language",language );



    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        user = savedInstanceState.getString("user");
        language = savedInstanceState.getString("language");


    }
}