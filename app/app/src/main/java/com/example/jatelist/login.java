package com.example.jatelist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;

public class login extends AppCompatActivity implements DialogClass.Listener, settingsDialog.Listener,infoDialog.Listener{

    private String user;
    private String language;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private Boolean night=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState!= null) {

            user = savedInstanceState.getString("user");
            language =savedInstanceState.getString("language");
            night=savedInstanceState.getBoolean("mode");

        }




        setContentView(R.layout.activity_login);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        TextView eu=(TextView) findViewById(R.id.euskera);
        TextView en=(TextView) findViewById(R.id.ingles);
        TextView es=(TextView) findViewById(R.id.español);

        Switch mode=(Switch) findViewById(R.id.nightMode);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        language = sharedpreferences.getString("language", "es");
        night = sharedpreferences.getBoolean("mode",false);

        mode.setChecked(night);
        if(mode.isChecked()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        if(language.equals("eu")){
            eu.performClick();
        }else if (language.equals("en")){
            en.performClick();
        }else{
            es.performClick();
        }







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


        mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        //euskera button clicked, change language


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
        savedInstanceState.putBoolean("modo",night );



    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        user = savedInstanceState.getString("user");
        language = savedInstanceState.getString("language");
        night = savedInstanceState.getBoolean("modo");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_about:
                DialogFragment dialogAbout= new infoDialog();
                dialogAbout.show(getSupportFragmentManager(), "info dialog");

                return true;
            case R.id.action_settings:
                Bundle bundle = new Bundle();
                bundle.putString("language",language);
                bundle.putBoolean("mode",night);

                DialogFragment dialogSettings= new settingsDialog();
                dialogSettings.setArguments(bundle);
                dialogSettings.show(getSupportFragmentManager(), "settings dialog");

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void alpulsarSave(Boolean mode, String language) {
        //save preferences (settings dialog)
        SharedPreferences.Editor myEdit = sharedpreferences.edit();

        // Storing the key and its value as the data fetched from edittext
        myEdit.putBoolean("mode", mode);
        myEdit.putString("language", language);

        this.language=language;
        this.night=mode;

        // Once the changes have been made,
        // we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.commit();
        startActivity(this.getIntent());





    }



    @Override
    public void alpulsarOK() {
        //clos info dialog
    }
}