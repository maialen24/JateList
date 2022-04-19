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
import android.preference.PreferenceManager;
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

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class login extends AppCompatActivity implements DialogClass.Listener, settingsDialog.Listener,infoDialog.Listener{

    /* This class control login activity is used to login, sign up and to set preferences via toolbar */

    private String user;
    private String language;
    private Boolean night;

    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedpreferences;
    private Boolean spNight;
    private String spLanguage;
    private Boolean firstTime;





    @Override
    protected void onCreate(Bundle savedInstanceState) {



       /* if (firstTime) {
            Locale locale = new Locale(spLanguage);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }*/
        super.onCreate(savedInstanceState);
        Log.i("CONTROL","ON CREATE");

        //get user preferences

        preferences();
        if (savedInstanceState!= null) {

            user = savedInstanceState.getString("user");
            language =savedInstanceState.getString("language");
            night=savedInstanceState.getBoolean("mode");
          //  firstTime=false;
       /*     SharedPreferences.Editor myEdit = sharedpreferences.edit();

            // Storing the key and its value as the data fetched from edittext
            myEdit.putBoolean("firstTime", firstTime);

            // Once the changes have been made,
            // we need to commit to apply those changes made,
            // otherwise, it will throw an error
            myEdit.commit();*/



        }else{

            language=spLanguage;
            night=spNight;
         //   onSaveInstanceState(savedInstanceState);
            changeTheme();
          //  changeLanguage();
      /*      SharedPreferences.Editor myEdit = sharedpreferences.edit();

            // Storing the key and its value as the data fetched from edittext
            myEdit.putBoolean("firstTime", firstTime);

            // Once the changes have been made,
            // we need to commit to apply those changes made,
            // otherwise, it will throw an error
            myEdit.commit();*/



        }






        setContentView(R.layout.activity_login);




        //get elements
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        TextView eu=(TextView) findViewById(R.id.euskera);
        TextView en=(TextView) findViewById(R.id.ingles);
        TextView es=(TextView) findViewById(R.id.español);

        EditText userEditText=findViewById((R.id.username));
        EditText password=findViewById((R.id.password));

        Switch mode=(Switch) findViewById(R.id.nightMode);
        //set theme mode
        mode.setChecked(night);










        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("user");
        }


        // get database instance
        db dbHelper = new db(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //login button on click, CHECK CREDENTIALS
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

        // theme mode switch control
        mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                night=isChecked;
                spLanguage=language;

                changeTheme();


            }
        });

        //euskera button (textView) clicked, change language
        eu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info-paso","Euskera");
                language="eu";
                spNight=night;
                changeLanguage();
                changeTheme();
                finish();
                startActivity(getIntent());
            }
        });

        // english button (textview) clicked change language
        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info-paso","Ingelesa");
                language="en";
                spNight=night;
                changeLanguage();
                changeTheme();
                finish();
                startActivity(getIntent());
                //onStart();
            }
        });

        //spanish button (textview) clicked change language
        es.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info-paso","Castellano");
                language="es";
                spNight=night;
                changeLanguage();
                changeTheme();
                finish();
                startActivity(getIntent());
               // onStart();
            }
        });



        // sign up button, shows sing up dialog (DialogClass)
        ImageButton newUser= (ImageButton)findViewById((R.id.signUp));
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogoalerta= new DialogClass();
                dialogoalerta.show(getSupportFragmentManager(), "etiqueta");

            }
        });

        db.close();
        dbHelper.close();
    }


    // implements alpulsarSI method from DialogClass listener interface
    // on click positive button in DialogClass, sing up dialog
    @Override
    public void alpulsarSI(String user, String password) {
        //SAVE USER AND PASSWORD IN DB
        Log.i("info","AL PULSAR SI METODO");
        Log.i("info",user);
        Log.i("info",password);

        //insert new user in db
        db dbHelper = new db(this);
        //SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.insertUser(user, password);

        // Sends local notification with user ok created message
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

        dbHelper.close();



    }

    //implements alpusarNO method of DialogClass interface
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
        // Handle action bar item clicks here.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {

            // click info option, shows infoDialog
            case R.id.action_about:
                DialogFragment dialogAbout= new infoDialog();
                dialogAbout.show(getSupportFragmentManager(), "info dialog");
                return true;

                //click settings option, shows settingsDialog
            case R.id.action_settings:
                Bundle bundle = new Bundle();
                bundle.putString("language",spLanguage);
                bundle.putBoolean("mode",spNight);

                DialogFragment dialogSettings= new settingsDialog();
                dialogSettings.setArguments(bundle);
                dialogSettings.show(getSupportFragmentManager(), "settings dialog");

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    //implements alpulsarSave method of settingsDialog interface
    // save user preferences
    @Override
    public void alpulsarSave(Boolean pmode, String planguage) {
        //save preferences (settings dialog)
        SharedPreferences.Editor myEdit = sharedpreferences.edit();

        // Storing the key and its value as the data fetched from edittext
        myEdit.putBoolean("mode", pmode);
        myEdit.putString("language", planguage);

        // Once the changes have been made,
        // we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.commit();

    }

    // implements infoDialog interface positive button method, close dialog
    @Override
    public void alpulsarOK() {
        //close info dialog
    }

    // metodo laguntzaile to save preferences
    private void preferences(){
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        spLanguage = sharedpreferences.getString("language", "es");
        spNight = sharedpreferences.getBoolean("mode",false);
        language=spLanguage;
     //   firstTime = sharedpreferences.getBoolean("firstTime",true);

    }

    //metodo laguntzaile to change language
    private void changeLanguage(){

        Locale nuevaloc = new Locale(language);
        Locale.setDefault(nuevaloc);
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);

        Context context = getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());



    }

    //metodo laguntzaile to change app mode
    private void changeTheme(){
        if(night){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }



    @Override
    protected void onPause() {
        super.onPause();
        Log.i("CONTROL","login ON PAUSE");

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("CONTROL"," login ON START");


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("CONTROL","login ON RESUME");


    }



    @Override
    protected void onStop() {
        super.onStop();
        Log.i("CONTROL","login ON STOP");

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("CONTROL","login ON DESTROY");

    }


}